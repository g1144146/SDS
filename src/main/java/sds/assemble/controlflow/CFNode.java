package sds.assemble.controlflow;

import java.util.ArrayList;
import java.util.List;

import sds.assemble.LineInstructions;

/**
 * This class is for node of control flow graph.
 * @author inagaki
 */
public class CFNode {
	private List<CFEdge> dominators;
	private List<CFEdge> parents;
	private List<CFEdge> children;
	private CFNode  immediateDominator;
	private CFNodeType nodeType;
	public CFNode(LineInstructions[] inst) {
		this.nodeType = CFNodeType.getType(inst[0].getOpcodes());
		this.dominators = new ArrayList<>();
		this.parents = new ArrayList<>();
		this.children = new ArrayList<>();
	}

	public CFNode(LineInstructions[] inst, CFNode parent) {
		this.nodeType = CFNodeType.getType(inst[0].getOpcodes());
		CFEdge edge = new CFEdge(parent, this);
	}

	/**
	 * adds parent node of this.
	 * @param parent parent node
	 */
	public void addParent(CFNode parent) {
		CFEdge edge = new CFEdge(this, parent);
		parents.add(edge);
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
	 * @return if this node exist dominator nodes, this method returns TRUE.<br>
	 * Otherwise, it returns FALSE.
	 */
	public boolean existDominators() {
		return dominators.size() > 0;
	}
}