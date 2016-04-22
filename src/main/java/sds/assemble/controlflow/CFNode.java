package sds.assemble.controlflow;

import java.util.Set;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

import sds.assemble.LineInstructions;
import sds.classfile.bytecode.OpcodeInfo;

/**
 * This class is for node of control flow graph.
 * @author inagaki
 */
public class CFNode {
	private Set<CFNode> dominators;
	private Set<CFEdge> parents;
	private Set<CFEdge> children;
	private CFNode  immediateDominator;
	private OpcodeInfo start;
	private OpcodeInfo end;
	CFNodeType nodeType;

	/**
	 * constructor.
	 * @param inst instrcutions of a line.
	 */
	public CFNode(LineInstructions inst) {
		this.nodeType = CFNodeType.getType(inst.getOpcodes());
		Comparator<CFEdge> edgeComparator
			= (CFEdge x, CFEdge y) -> (x.getDest().start.getPc() - y.getDest().start.getPc());
		Comparator<CFNode> nodeComparator
			= (CFNode x, CFNode y) -> (x.start.getPc() - y.start.getPc());
		this.dominators = new ConcurrentSkipListSet<>(nodeComparator);
		this.parents    = new ConcurrentSkipListSet<>(edgeComparator);
		this.children   = new ConcurrentSkipListSet<>(edgeComparator);

		this.start = inst.get(inst.getTable().getStartPc());
		this.end   = inst.get(inst.getTable().getEndPc());
	}

	/**
	 * returns node type.
	 * @return node type
	 */
	public CFNodeType getType() {
		return nodeType;
	}

	/**
	 * adds parent node of this.
	 * @param parent parent node
	 */
	public void addParent(CFNode parent) {
		if(!isRoot()) {
			this.immediateDominator = parent;
		} else {
			if(immediateDominator != null && !immediateDominator.equals(parent)) {
				this.immediateDominator = null;
			}
			if(!parents.contains(new CFEdge(this, parent))) {
				parents.add(new CFEdge(this, parent));
			}
		}
	}

	/**
	 * adds child node of this.
	 * @param child child node
	 */
	public void addChild(CFNode child) {
		CFEdge edge = new CFEdge(this, child);
		children.add(edge);
	}

	/**
	 * returns whether this node exist dominator nodes.
	 * @return if this node exist dominator nodes, this method returns true.<br>
	 * Otherwise, this method returns false.
	 */
	public boolean existDominators() {
		return !dominators.isEmpty();
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
		return immediateDominator == null && parents.isEmpty();
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
}