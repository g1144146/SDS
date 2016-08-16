package sds.assemble.controlflow;

import sds.assemble.MethodContent.ExceptionContent;
import sds.assemble.LineInstructions;

import static sds.assemble.controlflow.DominatorNodeSearcher.searchCommon;
import static sds.assemble.controlflow.CFNodeType.Entry;
import static sds.assemble.controlflow.CFNodeType.End;
import static sds.assemble.controlflow.CFNodeType.Exit;
import static sds.assemble.controlflow.CFNodeType.LoopEntry;
import static sds.assemble.controlflow.CFNodeType.LoopExit;
import static sds.assemble.controlflow.CFNodeType.SynchronizedEntry;
import static sds.assemble.controlflow.CFNodeType.SynchronizedExit;
import static sds.assemble.controlflow.CFNodeType.StringSwitch;
import static sds.assemble.controlflow.CFNodeType.Switch;
import static sds.assemble.controlflow.CFEdgeType.FalseBranch;
import static sds.assemble.controlflow.CFEdgeType.JumpToCatch;
import static sds.assemble.controlflow.CFEdgeType.JumpToFinally;
import static sds.assemble.controlflow.CFEdgeType.TrueBranch;
import static sds.classfile.bytecode.MnemonicTable.athrow;
import static sds.classfile.bytecode.MnemonicTable._goto;
import static sds.classfile.bytecode.MnemonicTable.goto_w;


/**
 * This builder class is for control flow graph.<br>
 * This class is designed singleton.
 * @author inagaki
 */
public class CFGBuilder {
	private static CFGBuilder builder = null;

	/**
	 * returns own instance.
	 * @return instance
	 */
	public static CFGBuilder getInstance() {
		if(builder == null) {
			builder = new CFGBuilder();
		}
		return builder;
	}

	/**
	 * returns control flow graph.
	 * @param inst method's instructions
	 * @param ex content of method's try-catch-finally
	 * @return control flow graph
	 */
	public CFNode[] build(LineInstructions[] inst, ExceptionContent ex) {
		if(inst == null) {
			return new CFNode[0];
		}

		/** 1. create nodes **/
		CFNode[] nodes = new CFNode[inst.length];
		for(int i = 0; i < nodes.length; i++) {
			nodes[i] = new CFNode(inst[i]);
		}

		/** 2. set parent and child, and investigate try-catch-finally or synchronized statement **/
		int index = 0;
		for(CFNode n : nodes) {
			// processing for try-catch-finally
			boolean isGoto = (n.getStart().getOpcodeType() == _goto)
						  || (n.getStart().getOpcodeType() == goto_w);
			int[] tryIndex= ex.getIndexInRange(n.getStart().getPc(), false, isGoto);
			int[] catchTypeIndex = ex.getIndexInRange(n.getStart().getPc(), true, isGoto);
			if(tryIndex.length > 0) {
				n.inTry = true;
				for(int ti : tryIndex) { // for catch
					int catchIndex = ex.getTarget()[ti];
					for(int i = index; i < nodes.length; i++) {
						if(nodes[i].isInPcRange(catchIndex)) {
							nodes[i].isCatch = true;
							n.addChild(nodes[i], JumpToCatch);
							nodes[i].addParent(n, JumpToCatch);
							break;
						}
					}
				}
			}
			if(catchTypeIndex.length > 0) {
				for(int ci : catchTypeIndex) {
					int finallyIndex = ex.getTarget()[ci];
					for(int i = index; i < nodes.length; i++) { // for finally
						if(nodes[i].isInPcRange(finallyIndex)) {
							if(nodes[i].getType() != SynchronizedExit) {
								// because ExceptionTable attribute contains synchronized statement,
								// excluding in case of node type is SynchronizedExit
								nodes[i].isFinally = true;
								n.addChild(nodes[i], JumpToFinally);
								nodes[i].addParent(n, JumpToFinally);
								if((i + 1) < nodes.length
								&& nodes[i + 1].getEnd().getOpcodeType() == athrow) {
									nodes[i + 1].isFinally = true;
									nodes[i].addChild(nodes[i + 1]);
									nodes[i + 1].addParent(nodes[i]);
								}
								break;
							}
						}
					}
				}
			}
			// processing for setting parent and child.
			if(index > 0) {
				CFNodeType type = nodes[index-1].getType();
				if(type != Exit && type != LoopExit && type != Switch
				&& type != End && type != StringSwitch) {
					if(type == Entry) {
						n.addParent(nodes[index-1], TrueBranch);
						nodes[index-1].addChild(n, TrueBranch);
					} else {
						n.addParent(nodes[index-1]);
						nodes[index-1].addChild(n);
					}
				}
			} else if(index == nodes.length-1) {
				CFNodeType type = n.getType();
				if(type != Exit && type != LoopExit && type != Switch && type != StringSwitch) {
					if(type == Entry) {
						n.addParent(nodes[index], TrueBranch);
						nodes[index].addChild(n, TrueBranch);
					} else {
						n.addChild(nodes[index]);
						nodes[index].addParent(n);
					}
				}
			}
			index++;
		}

		/** 3. set jump point node **/
		index = 0;
		int synchCount = 0;
		for(CFNode n : nodes) {
			CFNodeType type = n.getType();
			if(type == Exit || type == Entry) {
				for(int i = index; i < nodes.length; i++) {
					if(nodes[i].isInPcRange(n.getJumpPoint())) {
						if(type == Entry) {
							n.addChild(nodes[i], FalseBranch);
							nodes[i].addParent(n, FalseBranch);
						} else {
							n.addChild(nodes[i]);
							nodes[i].addParent(n);
						}
						break;
					}
				}
			} else if(type == LoopExit) {
				for(int i = 0; i < index; i++) {
					if(nodes[i].isInPcRange(n.getJumpPoint())) {
						nodes[i].nodeType = LoopEntry;
						n.addChild(nodes[i]);
						break;
					}
				}
			} else if(type == Switch || type == StringSwitch) {
				int[] jumpPoints = n.getSwitchJump();
				int offsetIndex = 0;
				for(int i = 0; i < nodes.length; i++) {
					if(nodes[i].isInPcRange(jumpPoints[offsetIndex])) {
						n.addChild(nodes[i]);
						nodes[i].addParent(n);
						offsetIndex++;
						if(offsetIndex == jumpPoints.length) {
							break;
						}
					}
				}
			} else if((type == SynchronizedEntry) && (nodes[index].synchIndent == 0)) {
				// in case of synchIndent is over zero,
				// the node has already analyzed synchronized statement.
				synchCount++;
				int synchIndex = index + 1;
				while((synchCount > 0) && (synchIndex < nodes.length)) {
					// count > 0: for nested synchronized statement.
					nodes[synchIndex].synchIndent += synchCount;
					if(nodes[synchIndex].getType() == SynchronizedEntry) {
						synchCount++;
					} else if(nodes[synchIndex].getType() == SynchronizedExit) {
						synchCount--;
						int jumpPoint = nodes[synchIndex].getJumpPoint();
						for(int j = synchIndex; j < nodes.length; j++) {
							if(nodes[j].isInPcRange(jumpPoint)) {
								nodes[synchIndex].addChild(nodes[j]);
								nodes[j].addParent(nodes[synchIndex]);
							}
						}
					}
					synchIndex++;
				}
			}
			index++;
		}

		/** 4. decide dominator **/
		for(int i = nodes.length - 1; i > 0; i--) {
			if(nodes[i].getParents().size() == 1) { // immediate dominator
				CFNode n = nodes[i].getParents().iterator().next().getDest();
				nodes[i].setImmediateDominator(n);
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
				nodes[i].setDominator(candidate);
			}
		}
		return nodes;
	}
}