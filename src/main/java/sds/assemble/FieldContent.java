package sds.assemble;

import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.ConstantValue;
import sds.classfile.attributes.Signature;
import sds.classfile.constantpool.ConstantInfo;

import static sds.util.DescriptorParser.parse;

/**
 * This class is for contents of field.
 * @author inagaki
 */
public class FieldContent extends MemberContent {
    private String constVal;

    FieldContent(MemberInfo info, ConstantInfo[] pool) {
        super(info, pool);
        for(AttributeInfo a : info.getAttr()) {
            analyzeAttribute(a, pool);
        }
    }

    @Override
    void analyzeAttribute(AttributeInfo info, ConstantInfo[] pool) {
        switch(info.getClass().getSimpleName()) {
            case "ConstantValue":
                ConstantValue cv = (ConstantValue)info;
                this.constVal = cv.constantValue;
                break;
            case "Signature":
                Signature sig = (Signature)info;
                this.desc = parse(sig.signature);
                break;
            default:
                super.analyzeAttribute(info, pool);
                break;
        }
    }

    /**
     * returns constant value of field.<br>
     * when field type is primitive or String and field access flag doesn't contain final
     * , this method returns null.
     * @return constant value
     */
    public String getConstVal() {
        return constVal;
    }
}