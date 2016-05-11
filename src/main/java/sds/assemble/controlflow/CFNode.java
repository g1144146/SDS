package sds.assemble.controlflow;

import java.util.LinkedHashSet;
import java.util.Set;

import sds.assemble.LineInstructions;
import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.LookupSwitch;
import sds.classfile.bytecode.TableSwitch;

/**
 * This class is for node of control flow graph.
 * @author inagaki
 */
public class CFNode {
	private Set<CFEdge> parents;
	private Set<CFEdge> children;
	private CFNode dominator;
	private CFNode  immediateDominator;
	private OpcodeInfo start;
	private OpcodeInfo end;
	private int jumpPoint = -1;
	private int[] switchJump = new int[0];
	CFNodeType nodeType;

	/**
	 * constructor.
	 * @param inst instrcutions of a line.
	 */
	public CFNode(LineInstructions inst) {
		int size = inst.getOpcodes().size();
		this.start = inst.getOpcodes().getAll()[0];
		if(size == 1) {
			this.end = start;
		} else {
			this.end = inst.getOpcodes().getAll()[size-1];
		}
		
		this.nodeType = CFNodeType.getType(inst.getOpcodes(), end);
		if(nodeType == CFNodeType.Entry) { // if_xx
			for(OpcodeInfo op : inst.getOpcodes().getAll()) {
				if(op instanceof BranchOpcode) {
					this.jumpPoint = ((BranchOpcode)op).getBranch() + op.getPc();
				}
			}
		} else if(nodeType == CFNodeType.Exit || nodeType == CFNodeType.LoopExit) { // goto
			this.jumpPoint = ((BranchOpcode)end).getBranch() + end.getPc();
		} else if(nodeType == CFNodeType.Switch) { // switch
			for(OpcodeInfo op : inst.getOpcodes().getAll()) {
				if(op instanceof LookupSwitch) {
					LookupSwitch look = (LookupSwitch)op;
					this.switchJump = new int[look.getMatch().length + 1];
					int[] offsets = look.getOffset();
					for(int i = 0; i < switchJump.length-1; i++) {
						switchJump[i] = offsets[i] + look.getPc();
					}
					switchJump[switchJump.length - 1] = look.getDefault() + look.getPc();
					break;
				} else if(op instanceof TableSwitch) {
					TableSwitch table = (TableSwitch)op;
					this.switchJump = new int[table.getJumpOffsets().length + 1];
					int[] offsets = table.getJumpOffsets();
					for(int i = 0; i < switchJump.length-1; i++) {
						switchJump[i] = offsets[i] + table.getPc();
					}
					switchJump[switchJump.length - 1] = table.getDefault() + table.getPc();
					break;
				}
			}
 		}
		
		this.parents    = new LinkedHashSet<>();
		this.children   = new LinkedHashSet<>();
	}

	/**
	 * returns index into code array of jump point.<br>
	 * if this node type is Entry or Exit (and that of Loop), this method returns -1.
	 * @return jump point index
	 */
	public int getJumpPoint() {
		return jumpPoint;
	}

	/**
	 * returns indexes into code array of jump point.
	 * @return jump point indexes
	 */
	public int[] getSwitchJump() {
		return switchJump;
	}

	/**
	 * returns node type.
	 * @return node type
	 */
	public CFNodeType getType() {
		return nodeType;
	}

	/**
	 * returns parent nodes.
	 * @return parent nodes
	 */
	public Set<CFEdge> getParents() {
		return parents;
	}

	/**
	 * returns immediate dominator node.
	 * @return immediate dominator node
	 */
	public CFNode getImmediateDominator() {
		return immediateDominator;
	}

	/**
	 * returns dominator node.
	 * @return dominator node
	 */
	public CFNode getDominator() {
		return dominator;
	}

	/**
	 * sets immediate dominator node.
	 * @param node immediate dominator node
	 */
	public void setImmediateDominator(CFNode node) {
		this.immediateDominator = node;
	}

	/**
	 * sets dominator node.
	 * @param node dominator node
	 */
	public void setDominator(CFNode node) {
		this.dominator = node;
	}

	/**
	 * adds parent node of this.
	 * @param parent parent node
	 */
	public void addParent(CFNode parent) {
		if(!isRoot()) {
			CFEdge edge = new CFEdge(this, parent);
			if(parents.isEmpty()) {
				this.immediateDominator = parent;
				this.parents.add(edge);
			} else {
				if(!parents.contains(edge)) {
					parents.add(edge);
				}
			}
		}
	}

	/**
	 * adds child node of this.
	 * @param child child node
	 */
	public void addChild(CFNode child) {
		CFEdge edge = new CFEdge(this, child);
		if(!children.contains(edge)) {
			children.add(edge);
		}
	}

	/**
	 * returns whether specified pc is in range of opcode pc of this opcodes.
	 * @param pc index into code array
	 * @return if specified pc is in range of opcode pc, this method returns true.<br>
	 * Otherwise, this method returns false.
	 */
	public boolean isInPcRange(int pc) {
		return start.getPc() <= pc && pc <= end.getPc();
	}

	/**
	 * returns whether this node is root.
	 * @return if this node is root, this method returns true.<br>
	 * Otherwise, this method returns false.
	 */
	public boolean isRoot() {
		return start.getPc() == 0;
	}

	/**
	 * returns start opcode of instructions of this node.
	 * @return start opcode
	 */
	public OpcodeInfo getStart() {
		return start;
	}

	/**
	 * returns end opcode of instructions of this node.
	 * @return end opcode
	 */
	public OpcodeInfo getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		int h = 0;
		char[] val1 = String.valueOf(start.getPc()).toCharArray();
		char[] val2 = String.valueOf(end.getPc()).toCharArray();
		char[] val3 = start.getOpcodeType().toString().toCharArray();
		char[] val4 = end.getOpcodeType().toString().toCharArray();
		for(int i = 0; i < val1.length; i++) {
			h = 31 * h + val1[i];
		}
		for(int i = 0; i < val2.length; i++) {
			h = 31 * h + val2[i];
		}
		for(int i = 0; i < val3.length; i++) {
			h = 31 * h + val3[i];
		}
		for(int i = 0; i < val4.length; i++) {
			h = 31 * h + val4[i];
		}
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CFNode)) {
			return false;
		}
		CFNode node = (CFNode)obj;
		return start.getPc() == node.getStart().getPc()
			&& end.getPc() == node.getEnd().getPc();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#").append(start.getPc()).append("-").append(end.getPc())
			.append(" [").append(nodeType).append("]").append("\n");
		if(parents.size() == 1) {
		 	sb.append("  immediate dominator: ")
		 		.append(immediateDominator.getStart().getPc()).append("-")
		 		.append(immediateDominator.getEnd().getPc());
		} else if(parents.size() > 1) {
			sb.append("  dominator: ")
				.append(dominator.getStart().getPc()).append("-")
		 		.append(dominator.getEnd().getPc())
				.append("\n  parents: ");
			for(CFEdge edge : parents) {
				sb.append(edge.toString()).append(" ");
			}
		} else {
			sb.append("  parents: not exist");
		}
		sb.append("\n  children: ");
		for(CFEdge edge : children) {
			sb.append(edge.toString()).append(" ");
		}
		return sb.toString();
	}
}