package sds.assemble.controlflow;

import java.util.Set;
import java.util.HashSet;
import sds.assemble.MethodContent.ExceptionContent;
import sds.assemble.LineInstructions;
import sds.classfile.bytecode.MnemonicTable;

import static sds.assemble.controlflow.DominatorNodeSearcher.searchCommon;
import static sds.assemble.controlflow.NodeTypeChecker.check;
import static sds.assemble.controlflow.NodeTypeChecker.checkNone;
import static sds.assemble.controlflow.CFNodeType.Entry;
import static sds.assemble.controlflow.CFNodeType.End;
import static sds.assemble.controlflow.CFNodeType.Exit;
import static sds.assemble.controlflow.CFNodeType.LoopEntry;
import static sds.assemble.controlflow.CFNodeType.LoopExit;
import static sds.assemble.controlflow.CFNodeType.OneLineEntry;
import static sds.assemble.controlflow.CFNodeType.OneLineEntryBreak;
import static sds.assemble.controlflow.CFNodeType.SynchronizedEntry;
import static sds.assemble.controlflow.CFNodeType.SynchronizedExit;
import static sds.assemble.controlflow.CFNodeType.StringSwitch;
import static sds.assemble.controlflow.CFNodeType.Switch;
import static sds.assemble.controlflow.CFEdgeType.FalseBranch;
import static sds.assemble.controlflow.CFEdgeType.JumpToCatch;
import static sds.assemble.controlflow.CFEdgeType.JumpToFinally;
import static sds.assemble.controlflow.CFEdgeType.Normal;
import static sds.assemble.controlflow.CFEdgeType.TrueBranch;
import static sds.classfile.bytecode.MnemonicTable._goto;
import static sds.classfile.bytecode.MnemonicTable.goto_w;

/**
 * This builder class is for control flow graph.<br>
 * @author inagaki
 */
public class CFGBuilder {
	private CFNode[] nodes;
	private ExceptionContent ex;

	/**
	 * constructor.
	 * @param inst method's instructions
	 * @param ex content of method's try-catch-finally
	 */
	public CFGBuilder(LineInstructions[] inst, ExceptionContent ex) {
		this.ex = ex;
		if(inst == null) {
			this.nodes = new CFNode[0];
			return;
		}
		this.nodes = new CFNode[inst.length];
		for(int i = 0; i < nodes.length; i++) {
			nodes[i] = new CFNode(inst[i]);
		}
	}

	/**
	 * returns control flow graph.
	 * @return control flow graph
	 */
	public CFNode[] build() {
		if(nodes.length == 0) {
			return nodes;
		}
		/** 1. set parent and child, and investigate try-catch-finally or synchronized statement **/
		setParentAndChildNode();

		/** 2. set jump point node **/
		setJumpPointNode();

		/** 3. decide dominator **/
		decideDominatorNode();
		return nodes;
	}

	private void setParentAndChildNode() {
		int index = 0;
		for(CFNode n : nodes) {
			// processing for setting parent and child.
			if(index > 0) {
				if(checkNone(nodes[index - 1], Exit, LoopExit, End, Switch, StringSwitch)) {
					if(check(nodes[index - 1], Entry)) {
						addParentAndChild(index, index - 1, TrueBranch);
					} else {
						addParentAndChild(index, index - 1, Normal);
					}
				}
			} else if(index == nodes.length - 1) {
				if(checkNone(n, Exit, LoopExit, Switch, StringSwitch)) {
					if(check(n, Entry)) {
						addParentAndChild(index, index, TrueBranch);
					} else {
						addParentAndChild(index, index, Normal);
					}
				}
			}
			index++;
		}
		// for try-catch-finally
		for(int i = 0; i < nodes.length; i++) {
			MnemonicTable type = nodes[i].getStart().getOpcodeType();
			boolean isGoto = ((type == _goto) || (type == goto_w));
			if(setParentAndChildNodeForTryCatchFinally(isGoto, i)) {
				break;
			}
		}
	}

	private boolean setParentAndChildNodeForTryCatchFinally(boolean isGoto, int index) {
		int[] tryIndex = ex.getIndexInRange(nodes[index].getStart().getPc(), isGoto);
		if(tryIndex.length > 0) {
			nodes[index].inTry = true;
			Set<Integer> jumpSet = new HashSet<>();
			jumpSet.add(index);
			for(int ti : tryIndex) {
				// for catch
				int[][] table = ex.getTable();
				int catchTarget = table[ti][2];
				int finallyTarget = -1;
				for(int i = index; i < nodes.length; i++) {
					if(nodes[i].isInPcRange(catchTarget)) {
						nodes[i].inCatch = true;
						jumpSet.add(i);
						addParentAndChild(i, index, JumpToCatch);
						int from = 0;
						int to   = 0;
						for(int j = 0; j < table.length; j++) {
							if(table[j][0] == catchTarget) {
								from = table[j][0];
								to   = table[j][1];
								finallyTarget = table[j][2];
								break;
							}
						}
						for(int j = i; j < nodes.length; j++) {
							int start = nodes[j].getStart().getPc();
							int end   = nodes[j].getEnd().getPc();
							if((from <= start) && (end < to)) {
								jumpSet.add(j);
							}
						}
						break;
					}
				}
				// for finally
				for(int[] from : table) {
					if(from[0] == finallyTarget) {
						for(int i = index; i < nodes.length; i++) {
							int start = nodes[i].getStart().getPc();
							if(start == finallyTarget) {
								nodes[i].inFinally = true;
								for(int jump : jumpSet) {
									addParentAndChild(i, jump, JumpToFinally);
								}
								break;
							}
						}
						break;
					}
				}
				// for finally (part2)
				String[] exceptions = ex.getException();
				for(int i = 0; i < table.length; i++) {
					if((table[i][0] == table[ti][0]) && (table[i][1] == table[ti][1])) {
						if(exceptions[i].equals("any")) {
							int target = table[i][2];
							for(int j = index; j < nodes.length; j++) {
								int start = nodes[j].getStart().getPc();
								if(start == target) {
									nodes[j].inFinally = true;
									for(int jump : jumpSet) {
										addParentAndChild(j, jump, JumpToFinally);
									}
									break;
								}
							}
							break;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	private void setJumpPointNode() {
		int index = 0;
		int synchCount = 0;
		for(CFNode n : nodes) {
			if(check(n, Exit, Entry, OneLineEntry, OneLineEntryBreak)) {
				for(int i = index; i < nodes.length; i++) {
					// jump for IF opcode
					for(int jump : n.getJumpPoints()) {
						// in case of nodes[i] has some if_xx opcode,
						// the node has some jump points.
						for(int k = i; k < nodes.length; k++) {
							if(nodes[k].isInPcRange(jump)) {
								addParentAndChild(k, index, FalseBranch);
								break;
							}
						}
					}
					 // jump for GOTO opcode
					if(nodes[i].isInPcRange(n.getGotoPoint())) {
						if(check(n, OneLineEntry, OneLineEntryBreak)) {
							addParentAndChild(i, index, TrueBranch);
						} else {
							addParentAndChild(i, index, Normal);
						}
					}
				}
			} else if(check(n, LoopExit)) {
				for(int i = 0; i < index; i++) {
					if(nodes[i].isInPcRange(n.getGotoPoint())) {
						nodes[i].nodeType = LoopEntry;
						n.addChild(nodes[i]);
						break;
					}
				}
			} else if(check(n, Switch, StringSwitch)) {
				int[] jumpPoints = n.getSwitchJump();
				int offsetIndex = 0;
				for(int i = 0; i < nodes.length; i++) {
					if(nodes[i].isInPcRange(jumpPoints[offsetIndex])) {
						addParentAndChild(i, index, Normal);
						offsetIndex++;
						if(offsetIndex == jumpPoints.length) {
							break;
						}
					}
				}
			} else if(check(n, SynchronizedEntry) && (nodes[index].synchIndent == 0)) {
				// in case of synchIndent is over zero,
				// the node has already analyzed synchronized statement.
				synchCount++;
				int synchIndex = index + 1;
				while((synchCount > 0) && (synchIndex < nodes.length)) {
					// count > 0: for nested synchronized statement.
					nodes[synchIndex].synchIndent += synchCount;
					if(check(nodes[synchIndex], SynchronizedEntry)) {
						synchCount++;
					} else if(check(nodes[synchIndex], SynchronizedExit)) {
						synchCount--;
						int jumpPoint = nodes[synchIndex].getGotoPoint();
						for(int j = synchIndex; j < nodes.length; j++) {
							if(nodes[j].isInPcRange(jumpPoint)) {
								addParentAndChild(j, synchIndex, Normal);
							}
						}
					}
					synchIndex++;
				}
			}
			index++;
		}
	}

	private void addParentAndChild(int x, int y, CFEdgeType type) {
		nodes[x].addParent(nodes[y], type);
		nodes[y].addChild( nodes[x], type);
	}

	private void decideDominatorNode() {
		for(int i = nodes.length - 1; i > 0; i--) {
			if(nodes[i].getParents().size() == 1) { // immediate dominator
				CFNode n = nodes[i].getParents().iterator().next().getDest();
				nodes[i].immediateDominator = n;
			} else if(nodes[i].getParents().size() > 1) { // dominator
				CFEdge[] parents = nodes[i].getParents().toArray(new CFEdge[0]);
				CFNode candidate = null;
				for(int j = 0; j < parents.length; j++) {
					if(j < parents.length - 1) {
						if(candidate == null) {
							candidate = searchCommon(parents[j], parents[j+1]);
						} else {
							candidate = searchCommon(candidate, parents[j+1]);
						}
					}
				}
				nodes[i].dominator = candidate;
			}
		}
	}
}