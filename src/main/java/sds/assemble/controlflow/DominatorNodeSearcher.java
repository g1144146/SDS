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
		Set<CFNode> nodeSet = new HashSet<>();
		nodeSet.add(node1);
		while((node1 = node1.getImmediateDominator()) != null) {
			nodeSet.add(node1);
		}
		nodeSet.add(node2);
		while((node2 = node2.getImmediateDominator()) != null) {
			if(nodeSet.contains(node2)) {
				return node2;
			}
		}
		throw new CFNodeException("not found common dominator node.");
	}
}