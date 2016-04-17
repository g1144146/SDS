package sds.classfile.attributes.stackmap;

/**
 * This enum class is for
 * {@link StackMapFrame <code>StackMapFrame</code>}.
 * @author inagaki
 */
public enum StackMapFrameType {
	SameFrame,
	SameLocals1StackItemFrame,
	SameLocals1StackItemFrameExtended,
	ChopFrame,
	SameFrameExtended,
	AppendFrame,
	FullFrame;
}