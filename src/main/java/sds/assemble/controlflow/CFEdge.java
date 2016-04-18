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
}