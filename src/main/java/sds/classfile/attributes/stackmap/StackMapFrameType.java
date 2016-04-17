package sds.classfile.attributes.stackmap;

/**
 * This enum class is for
 * {@link StackMapFrame <code>StackMapFrame</code>}.
 * @author inagaki
 */
public enum StackMapFrameType {
	SameFrame("same_frame"),
	SameLocals1StackItemFrame("same_locals_1_stack_item_frame"),
	SameLocals1StackItemFrameExtended("same_locals_1_stack_item_frame_extended"),
	ChopFrame("chop_frame"),
	SameFrameExtended("same_frame_extended"),
	AppendFrame("append_frame"),
	FullFrame("full_frame");

	private String type;

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