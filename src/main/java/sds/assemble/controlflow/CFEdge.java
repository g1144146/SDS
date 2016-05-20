package sds.assemble.controlflow;

import static sds.assemble.controlflow.CFEdgeType.Normal;
import static sds.assemble.controlflow.CFEdgeType.JumpToCatch;
import static sds.assemble.controlflow.CFEdgeType.JumpToFinally;

/**
 * This class is for edge of control flow graph.
 * @author inagaki
 */
public class CFEdge {
	private CFNode dest;
	private CFNode source;
	private CFEdgeType type;

	/**
	 * constructor.
	 * @param source source node
	 * @param dest destination node
	 */
	public CFEdge(CFNode source, CFNode dest) {
		this(source, dest, Normal);
	}

	/**
	 * constructor.
	 * @param source source node
	 * @param dest destination node
	 * @param type edge type
	 */
	public CFEdge(CFNode source, CFNode dest, CFEdgeType type) {
		this.source = source;
		this.dest = dest;
		this.type = type;
	}

	/**
	 * returns destination node of this edge.
	 * @return destination node
	 */
	public CFNode getDest() {
		return dest;
	}

	/**
	 * returns source node of this edge.
	 * @return source node
	 */
	public CFNode getSource() {
		return source;
	}

	/**
	 * returns this edge type.
	 * @return edge type
	 */
	public CFEdgeType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return source.hashCode() + dest.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CFEdge)) {
			return false;
		}
		CFEdge edge = (CFEdge)obj;
		CFNode s = edge.getSource();
		CFNode d = edge.getDest();
		return source.getStart().getPc() == s.getStart().getPc()
			&& source.getEnd().getPc()   == s.getEnd().getPc()
			&& dest.getStart().getPc()   == d.getStart().getPc()
			&& dest.getEnd().getPc()     == d.getEnd().getPc();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(dest.getStart().getPc())
			.append("-").append(dest.getEnd().getPc())
			.append("(").append(type).append(")");
		return sb.toString();
	}
}