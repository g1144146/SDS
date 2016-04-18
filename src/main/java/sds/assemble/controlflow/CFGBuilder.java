package sds.assemble.controlflow;

import sds.assemble.LineInstructions;

/**
 * This builder class is for control flow graph.<br>
 * This class is designed singleton.
 * @author inagaki
 */
public class CFGBuilder {
	private static CFGBuilder builder = null;
	public static CFGBuilder getInstance() {
		if(builder == null) {
			builder = new CFGBuilder();
		}
		return builder;
	}

	public CFNode build(LineInstructions[] inst) {
		CFNode root = new CFNode(inst);
		return root;
	} 
}
