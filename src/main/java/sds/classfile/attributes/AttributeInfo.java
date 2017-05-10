package sds.classfile.attributes;

import sds.classfile.Info;

/**
 * This adapter class is for info of class has attribute.
 * @author inagaki
 */
public abstract class AttributeInfo implements Info {
    private AttributeType type;

    /**
     * constructor.
     * @param type attribute type
     */
    public AttributeInfo(AttributeType type) {
        this.type = type;
    }

    /**
     * returns attribute type.
     * @return type
     */
    public AttributeType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[" + type.toString() + "]";
    }
}