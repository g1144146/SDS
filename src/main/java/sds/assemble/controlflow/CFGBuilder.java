package sds.assemble.controlflow;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.ArrayDeque;
import java.util.Deque;

import sds.assemble.ExceptionContent;
import sds.assemble.LineInstructions;

import static sds.assemble.controlflow.DominatorNodeSearcher.searchCommon;
import static sds.assemble.controlflow.CFNodeType.Entry;
import static sds.assemble.controlflow.CFNodeType.End;
import static sds.assemble.controlflow.CFNodeType.Exit;
import static sds.assemble.controlflow.CFNodeType.LoopEntry;
import static sds.assemble.controlflow.CFNodeType.LoopExit;
import static sds.assemble.controlflow.CFNodeType.Switch;
import static sds.assemble.controlflow.CFEdgeType.JumpCatch;
import static sds.assemble.controlflow.CFEdgeType.JumpFinally;

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
		/** 1. create nodes **/
		CFNode[] nodes = new CFNode[inst.length];
		for(int i = 0; i < nodes.length; i++) {
			nodes[i] = new CFNode(inst[i]);
		}
		
		/** 2. set parent and child, and investigate try-catch-finally **/
		int index = 0;
		for(CFNode n : nodes) {
			// processing for try-catch-finally
			int[] tryIndex = ex.getIndexInRange(n.getStart().getPc(), false);
			int[] catchTypeIndex = ex.getIndexInRange(n.getStart().getPc(), true);
			if(tryIndex.length > 0) { // for catch
				n.inTry = true;
				for(int ti : tryIndex) {
					int catchIndex = ex.getTarget()[ti];
					for(int i = index; i < nodes.length; i++) {
						if(nodes[i].isInPcRange(catchIndex)) {
							nodes[i].inCatch = true;
							n.addChild(nodes[i], JumpCatch);
							nodes[i].addParent(n, JumpCatch);
							break;
						}
					}
				}
			}
			if(catchTypeIndex.length > 0) { // for finally
				for(int ci : catchTypeIndex) {
					int finallyIndex = ex.getTarget()[ci];
					for(int i = index; i < nodes.length; i++) {
						if(nodes[i].isInPcRange(finallyIndex)) {
							nodes[i].inFinally = true;
							n.addChild(nodes[i], JumpFinally);
							nodes[i].addParent(n, JumpFinally);
							break;
						}
					}
				}
			}
			// processing for setting parent and child.
			if(index > 0) {
				CFNodeType type = nodes[index-1].getType();
				if(type != Exit && type != LoopExit && type != Switch) {
					n.addParent(nodes[index-1]);
					nodes[index-1].addChild(n);
				}
			} else if(index == nodes.length-1) {
				CFNodeType type = n.getType();
				if(type != Exit && type != LoopExit && type != Switch) {
					n.addChild(nodes[index]);
					nodes[index].addParent(n);
				}
			}
			index++;
		}

		/** 3. set jump point node **/
		index = 0;
		for(CFNode n : nodes) {
			CFNodeType type = n.getType();
			if(type == Exit || type == Entry) {
				for(int i = index; i < nodes.length; i++) {
					if(nodes[i].isInPcRange(n.getJumpPoint())) {
						n.addChild(nodes[i]);
						nodes[i].addParent(n);
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
			} else if(type == Switch) {
				int[] jumpPoints = n.getSwitchJump();
				int offsetIndex = 0;
				for(int i = index; i < nodes.length; i++) {
					if(nodes[i].isInPcRange(jumpPoints[offsetIndex])) {
						n.addChild(nodes[i]);
						nodes[i].addParent(n);
						offsetIndex++;
						if(offsetIndex == jumpPoints.length) {
							break;
						}
					}
				}
			}
			index++;
		}

		/** 4. set parent and child for try-catch-finally **/
		for(int i = 0; i < nodes.length; i++) {
			if(nodes[i].inCatch) {
				for(CFNode child : getChildren(nodes[i])) {
					if(! child.inFinally) {
						for(int j = i; j < nodes.length; j++) {
							if(nodes[j].equals(child)) {
								nodes[j].inCatch = true;
								break;
							}
						}
					}
				}
			}
			if((! nodes[i].isRoot()) && nodes[i].getParents().isEmpty()) {
				if(nodes[i-1].getType() != Exit && nodes[i-1].getType() != LoopExit) {
					nodes[i].inCatch = true;
					nodes[i].addParent(nodes[i-1]);
					nodes[i-1].addChild(nodes[i]);
				}
			}
			if(nodes[i].inFinally) {
				for(CFNode child : getChildren(nodes[i])) {
					child.inFinally = true;
				}
			}
		}

		/** 5. decide dominator **/
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

	private Set<CFNode> getChildren(CFNode node) {
		Set<CFNode> set = new LinkedHashSet();
		Deque<CFNode> queue = new ArrayDeque();
		queue.push(node);
		while(! queue.isEmpty()) {
			CFNode n = queue.pop();
			if(n.getType() == End) {
				set.add(n);
				break;
			}
			set.add(n);
			for(CFEdge e : n.getChildren()) {
				CFNode dest = e.getDest();
				if(! set.contains(dest)) {
					set.add(dest);
					queue.push(dest);
				}
			}
		}
		return set;
	}
}