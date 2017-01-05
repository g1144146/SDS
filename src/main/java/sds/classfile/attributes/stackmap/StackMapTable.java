package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;
import sds.classfile.bytecode.Opcodes;

import static sds.util.StackMapFrameParser.parseFrame;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.4">
 * StackMapTable Attribute</a>.
 * @author inagaki
 */
public class StackMapTable extends AttributeInfo {
	private Map<Integer, Map<String, List<String>>> entries;

	/**
	 * constructor.
	 */
	public StackMapTable() {
		super(AttributeType.StackMapTable);
	}

	/**
	 * returns entries of stack-map-table.
	 * @return entries
	 */
	public Map<Integer, Map<String, List<String>>> getEntries() {
		return entries;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {}

	public void read(ClassFileStream data, ConstantPool pool, Opcodes opcodes)
	throws IOException, VerificationTypeException, StackMapFrameException {
		StackMapFrame[] frames = new StackMapFrame[data.readShort()];
		StackMapFrameFactory factory = new StackMapFrameFactory();
		for(int i = 0; i < frames.length; i++) {
			frames[i] = factory.create(data);
		}
		entries = parseFrame(frames, pool, opcodes);
	}
}