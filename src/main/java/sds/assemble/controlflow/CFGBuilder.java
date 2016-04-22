package sds.assemble.controlflow;

import sds.assemble.LineInstructions;
import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.OpcodeInfo;

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

	public CFNode build(LineInstructions[] inst) {
		/** create nodes **/
		CFNode[] nodes = new CFNode[inst.length];
		for(int i = 0; i < nodes.length; i++) {
			nodes[i] = new CFNode(inst[i]);
		}

		/** set parent  **/
		int index = 0;
		for(CFNode n : nodes) {
			if(index != 0) {
				CFNode n2 = nodes[index-1];
				if(n2.getType() != CFNodeType.Exit && n2.getType() != CFNodeType.LoopExit) {
					n.addParent(n2);
				}
			}
			index++;
		}

		/** set child **/
		index = 0;
		for(CFNode n : nodes) {
			if(index != nodes.length-1) {
				CFNode n2 = nodes[index+1];
				if(n2.getType() != CFNodeType.Exit && n2.getType() != CFNodeType.LoopExit) {
					n.addParent(n2);
				}
			}
			index++;
		}

		/** set jump point node **/
		index = 0;
		for(CFNode n : nodes) {
			if(n.getEnd() instanceof BranchOpcode) {
				BranchOpcode op = (BranchOpcode)n.getEnd();
				int jumpPoint = op.getBranch();
				if(n.getType() == CFNodeType.Exit) {
					for(int i = index; i < nodes.length; i++) {
						if(nodes[i].isInPcRange(jumpPoint)) {
							n.addChild(nodes[i]);
							nodes[i].addParent(n);
							break;
						}
					}
				} else if(n.getType() == CFNodeType.LoopExit) {
					for(int i = index; i < nodes.length; i++) {
						if(nodes[i].isInPcRange(jumpPoint)) {
							nodes[i].nodeType = CFNodeType.LoopEntry;
							n.addParent(nodes[i]);
							nodes[i].addChild(n);
							break;
						}
					}
				}
			}
			index++;
		}
		
		CFNode root = new CFNode(inst[0]);
		return root;
	}
}