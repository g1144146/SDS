package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.attributes.annotation.AnnotationDefault;
import sds.classfile.attributes.annotation.RuntimeAnnotations;
import sds.classfile.attributes.annotation.RuntimeParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeTypeAnnotations;
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This factory class is for {@link AttributeInfo <code>AttributeInfo</code>}.
 * @author inagaki
 */
public class AttributeInfoFactory {
    /**
     * returns attribute info for stack map table.
     * @param attrName attribute name
     * @param data classfile stream
     * @param pool constant-pool
     * @return attribute info
     * @throws IOException 
     */
    public AttributeInfo create(String attrName, ClassFileStream data, ConstantInfo[] pool, OpcodeInfo[] opcodes)
    throws IOException {
        if(attrName.equals("StackMapTable")) {
            data.readInt();
            return new StackMapTable(data, pool, opcodes);
        }
        return create(attrName, data, pool);
    }

    /**
     * returns attribute info.
     * @param attrName attribute name
     * @param data classfile stream
     * @param pool constant-pool
     * @return attribute info
     * @throws IOException 
     */
    public AttributeInfo create(String attrName, ClassFileStream data, ConstantInfo[] pool) throws IOException {
        int length = data.readInt();
        switch(attrName) {
            case "AnnotationDefault":                    return new AnnotationDefault(data, pool);
            case "BootstrapMethods":                     return new BootstrapMethods(data, pool);
            case "Code":                                 return new Code(data, pool);
            case "ConstantValue":                        return new ConstantValue(data, pool);
            case "Deprecated":                           return new Deprecated();
            case "EnclosingMethod":                      return new EnclosingMethod(data, pool);
            case "Exceptions":                           return new Exceptions(data, pool);
            case "InnerClasses":                         return new InnerClasses(data, pool);
            case "LineNumberTable":                      return new LineNumberTable(data);
            case "MethodParameters":                     return new MethodParameters(data, pool);
            case "Signature":                            return new Signature(data.readShort(), pool);
            case "SourceDebugExtension":                 return new SourceDebugExtension(length, data);
            case "SourceFile":                           return new SourceFile(data, pool);
            case "Synthetic":                            return new Synthetic();
            case "LocalVariableTable":
            case "LocalVariableTypeTable":               return new LocalVariable(attrName, data, pool);
            case "RuntimeInvisibleAnnotations":
            case "RuntimeVisibleAnnotations":            return new RuntimeAnnotations(attrName, data, pool);
            case "RuntimeVisibleParameterAnnotations":
            case "RuntimeInvisibleParameterAnnotations": return new RuntimeParameterAnnotations(attrName, data, pool);
            case "RuntimeVisibleTypeAnnotations":
            case "RuntimeInvisibleTypeAnnotations":      return new RuntimeTypeAnnotations(attrName, data, pool);
            default:                                     throw new AttributeTypeException(attrName);
        }
    }

    class AttributeTypeException extends RuntimeException {
        AttributeTypeException(String type) {
            super(type);
        }
    }
}