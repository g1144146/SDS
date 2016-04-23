package sds.assemble.controlflow;

import sds.assemble.LineInstructions;
import sds.classfile.bytecode.BranchOpcode;

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
	 * @return control flow graph
	 */
	public CFNode[] build(LineInstructions[] inst) {
		/** create nodes **/
		CFNode[] nodes = new CFNode[inst.length];
		for(int i = 0; i < nodes.length; i++) {
			nodes[i] = new CFNode(inst[i]);
		}

		/** set parent and child **/
		int index = 0;
		for(CFNode n : nodes) {
			if(index > 0) {
				if(nodes[index-1].getType() != CFNodeType.Exit
				&& nodes[index-1].getType() != CFNodeType.LoopExit) {
					n.addParent(nodes[index-1]);
					nodes[index-1].addChild(n);
				}
			} else if(index == nodes.length-1) {
				if(n.getType() != CFNodeType.Exit && n.getType() != CFNodeType.LoopExit) {
					n.addChild(nodes[index]);
					nodes[index].addParent(n);
				}
			}
			index++;
		}

		/** set jump point node **/
		index = 0;
		for(CFNode n : nodes) {
			if(n.getEnd() instanceof BranchOpcode) {
				BranchOpcode op = (BranchOpcode)n.getEnd();
				int jumpPoint = op.getBranch() + op.getPc();
				if(n.getType() == CFNodeType.Exit
				|| n.getType() == CFNodeType.Entry
				|| n.getType() == CFNodeType.LoopEntry) {
					for(int i = index; i < nodes.length; i++) {
						if(nodes[i].isInPcRange(jumpPoint)) {
							n.addChild(nodes[i]);
							nodes[i].addParent(n);
							break;
						}
					}
				} else if(n.getType() == CFNodeType.LoopExit) {
					for(int i = 0; i < index; i++) {
						if(nodes[i].isInPcRange(jumpPoint)) {
							nodes[i].nodeType = CFNodeType.LoopEntry;
							n.addChild(nodes[i]);
							break;
						}
					}
				}
			}
			index++;
		}

		/** decide dominator **/
		for(int i = nodes.length - 1; i > 0; i--) {
			if(nodes[i].getParents().size() == 1) { // immediate dominator
				CFNode n = nodes[i].getParents().iterator().next().getDest();
				nodes[i].setImmediateDominator(n);
			} else if(nodes[i].getParents().size() > 1) { // dominator
				CFEdge[] parents = nodes[i].getParents().toArray(new CFEdge[0]);
				CFNode cand = null;
				for(int j = 0; j < parents.length; j++) {
					if(j < parents.length - 1) {
						if(cand == null) {
							cand = DominatorNodeSearcher.searchCommon(parents[j], parents[j+1]);
						} else {
							cand = DominatorNodeSearcher.searchCommon(cand, parents[j+1]);
						}
					}
				}
				nodes[i].setDominator(cand);
			}
		}
		return nodes;
	}
}