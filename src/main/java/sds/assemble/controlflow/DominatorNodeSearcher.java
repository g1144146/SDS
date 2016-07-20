package sds.assemble.controlflow;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is for searching dominator node.
 * @author inagaki
 */
public class DominatorNodeSearcher {
	/**
	 * returns common dominator node from edges.
	 * @param edge1 first edge
	 * @param edge2 second edge
	 * @return common dominator node
	 */
	public static CFNode searchCommon(CFEdge edge1, CFEdge edge2) {
		return searchCommon(edge1.getDest(), edge2.getDest());
	}

	/**
	 * returns common dominator node from node and edge.
	 * @param node target node
	 * @param edge target edge
	 * @return common dominator node
	 */
	public static CFNode searchCommon(CFNode node, CFEdge edge) {
		return searchCommon(node, edge.getDest());
	}

	/**
	 * returns common dominator node from nodes.
	 * @param node1 first node
	 * @param node2 second node
	 * @return common dominator node
	 */
	public static CFNode searchCommon(CFNode node1, CFNode node2) {
		if(node2.isRoot()) {
			return node2;
		}
		Set<CFNode> nodeSet = new HashSet<>();
		nodeSet.add(node1);
		CFNode n1 = node1;
		while((n1 = n1.getImmediateDominator()) != null) {
			nodeSet.add(n1);
		}
		nodeSet.add(node2);
		CFNode n2 = node2;
		while((n2 = n2.getImmediateDominator()) != null) {
			if(nodeSet.contains(n2)) {
				return n2;
			}
		}
		throw new CFNodeException("not found common dominator node between "
					+ node1.getStart().getPc() + "-" + node1.getEnd().getPc()
					+ " and "
					+ node2.getStart().getPc() + "-" + node2.getEnd().getPc());
	}
}