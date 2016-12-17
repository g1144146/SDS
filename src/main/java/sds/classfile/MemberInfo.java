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
	private String[] declaration;
	private Attributes attr;

	/**
	 * returns access flag of member.
	 * @return access flag
	 */
	public String getAccessFlags() {
		return declaration[0];
	}

	/**
	 * returns member name.
	 * @return member name
	 */
	public String getName() {
		return declaration[1];
	}

	/**
	 * returns member descriptor.
	 * @return member descriptor
	 */
	public String getDescriptor() {
		return declaration[2];
	}

	/**
	 * returns attributes of member.
	 * @return attributes
	 */
	public Attributes getAttr() {
		return attr;
	}

	/**
	 * returns type of member.
	 * @return type
	 */
	public String getType() {
		if(declaration[2].contains("(")) {
			return "method";
		}
		return "field";
	}

	/**
	 * sets attributes of member.
	 * @param attr attributes
	 */
	public void setAttr(Attributes attr) {
		this.attr = attr;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		int acc = data.readShort();
		int nameIndex = data.readShort();
		int descIndex = data.readShort();
		this.declaration = new String[3];
		declaration[1] = extract(pool.get(nameIndex - 1), pool);
		declaration[2] = parse(extract(pool.get(descIndex - 1), pool));
		declaration[0] = get(acc, getType());
	}

	@Override
	public String toString() {
		return declaration[0] + declaration[1] + declaration[2];
	}
}