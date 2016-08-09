package sds.classfile;

import java.io.IOException;

import static sds.util.AccessFlags.get;
import static sds.util.DescriptorParser.parse;
import static sds.util.Utf8ValueExtractor.extract;

/**
 * This adapter class is for info of class has member.
 * @author inagaki
 */
public class MemberInfo implements Info {
	private String accessFlags;
	private String name;
	private String descriptor;
	private Attributes attr;
	private String type;

	/**
	 * constructor.
	 * @param type type of member.
	 */
	MemberInfo(String type) {
		this.type = type;
	}

	/**
	 * returns access flag of member.
	 * @return access flag
	 */
	public String getAccessFlags() {
		return accessFlags;
	}

	/**
	 * returns member name.
	 * @return member name
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns member descriptor.
	 * @return member descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * returns attributes of member.
	 * @return attributes
	 */
	public Attributes getAttr() {
		return attr;
	}

	/**
	 * sets attributes of member.
	 * @param attr attributes
	 */
	public void setAttr(Attributes attr) {
		this.attr = attr;
	}

	/**
	 * returns type of member.
	 * @return type
	 */
	public String getType() {
		return type;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		int acc = data.readShort();
		int nameIndex = data.readShort();
		int descIndex = data.readShort();
		this.accessFlags = get(acc, type);
		this.name = extract(pool.get(nameIndex-1), pool);
		this.descriptor = parse(extract(pool.get(descIndex-1), pool));
	}
}