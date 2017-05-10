package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This adapter class is for RuntimeParameterAnnotations Attribute.
 * @author inagaki
 */
public class RuntimeParameterAnnotations extends AttributeInfo {
    private ParameterAnnotations[] parameterAnnotations;

    /**
     * constructor.
     * @param type attribute type
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public RuntimeParameterAnnotations(AttributeType type, ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(type);
        this.parameterAnnotations = new ParameterAnnotations[data.readByte()];
        for(int i = 0; i < parameterAnnotations.length; i++) {
            parameterAnnotations[i] = new ParameterAnnotations(data, pool);
        }
    }

    /**
     * returns runtime parameter annotations.
     * @return runtime parameter annotations
     */
    public ParameterAnnotations[] getParamAnnotations() {
        return parameterAnnotations;
    }
}