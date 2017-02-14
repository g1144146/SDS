package sds.assemble.controlflow;

/**
 * This class is for checking controlflow node type.
 * @author inagaki
 */
public class NodeTypeChecker {

	/**
	 * returns whether there is specified node type which matches specified all types.
	 * @param node target node
	 * @param types node types for checking
	 * @return if there is specified node type matches specified all types,
	 * this method returns true.<br>
	 * otherwise, it returns false.
	 */
	public static boolean check(CFNode node, CFNodeType... types) {
		CFNodeType type = node.getType();
		for(CFNodeType t : types) {
			if(t == type) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns whether there is no specified node type which matches specified all types.
	 * @param node target node
	 * @param types node types for checking
	 * @return if there is no specified node type matches specified all types,
	 * this method returns true.<br>
	 * otherwise, it returns false.
	 */
	public static boolean checkNone(CFNode node, CFNodeType... types) {
		return (! check(node, types));
	}
}