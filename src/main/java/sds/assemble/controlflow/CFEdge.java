package sds.assemble.controlflow;

/**
 * This class is for edge of control flow graph.
 * @author inagaki
 */
public class CFEdge {
	private CFNode dest;
	private CFNode source;

	/**
	 * constructor.
	 * @param source source node
	 * @param dest destination node
	 */
	public CFEdge(CFNode source, CFNode dest) {
		this.source = source;
		this.dest = dest;
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
			.append("-").append(dest.getEnd().getPc());
		return sb.toString();
	}
}