package sds.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public enum StackMapFrameType {
	SameFrame("same_rame"),
	SameLocals1StackItemFrame("same_locals_1_stack_item_frame"),
	SameLocals1StackItemFrameExtended("same_locals_1_stack_item_frame_extended"),
	ChopFrame("chop_frame"),
	SameFrameExtended("same_frame_extended"),
	AppendFrame("append_frame"),
	FullFrame("full_frame");

	/**
	 * 
	 */
	String type;

	StackMapFrameType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}
}
