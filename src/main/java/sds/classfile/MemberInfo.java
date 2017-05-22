package sds.classfile;

import java.io.IOException;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeInfoFactory;
import sds.classfile.constantpool.ConstantInfo;

import static sds.util.AccessFlags.get;
import static sds.util.DescriptorParser.parse;

/**
 * This adapter class is for info of class has member.
 * @author inagaki
 */
public class MemberInfo implements ClassFileInfo {
    private final String[] declaration;
    public final AttributeInfo[] attr;

    /**
     * constructor.
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException
     */
    public MemberInfo(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        int acc = data.readShort();
        int nameIndex = data.readShort();
        int descIndex = data.readShort();
        this.declaration = new String[3];
        declaration[1] = extract(nameIndex, pool);
        declaration[2] = parse(extract(descIndex, pool));
        declaration[0] = get(acc, getType());
        this.attr = readAttributes(data, pool);
    }

    private AttributeInfo[] readAttributes(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        AttributeInfo[] attr = new AttributeInfo[data.readShort()];
        AttributeInfoFactory factory = new AttributeInfoFactory();
        for(int i = 0; i < attr.length; i++) {
            String value = extract(data.readShort(), pool);
            attr[i] = factory.create(value, data, pool);
        }
        return attr;
    }

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
    public AttributeInfo[] getAttr() {
        return attr;
    }

    /**
     * returns type of member.
     * @return type
     */
    public String getType() {
        return declaration[2].contains("(") ? "method" : "field";
    }

    @Override
    public String toString() {
        if(getType().equals("field")) {
            return declaration[0] + declaration[2] + " " +  declaration[1];
        }
        return declaration[0] + declaration[1] + declaration[2];
    }
}