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

import static sds.classfile.attributes.AttributeType.LocalVariableTable;
import static sds.classfile.attributes.AttributeType.LocalVariableTypeTable;
import static sds.classfile.attributes.AttributeType.RuntimeInvisibleAnnotations;
import static sds.classfile.attributes.AttributeType.RuntimeInvisibleParameterAnnotations;
import static sds.classfile.attributes.AttributeType.RuntimeInvisibleTypeAnnotations;
import static sds.classfile.attributes.AttributeType.RuntimeVisibleAnnotations;
import static sds.classfile.attributes.AttributeType.RuntimeVisibleParameterAnnotations;
import static sds.classfile.attributes.AttributeType.RuntimeVisibleTypeAnnotations;

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
            case "AnnotationDefault":
                return new AnnotationDefault(data, pool);
            case "BootstrapMethods":
                return new BootstrapMethods(data, pool);
            case "Code":
                return new Code(data, pool);
            case "ConstantValue":
                return new ConstantValue(data, pool);
            case "Deprecated":
                return new Deprecated();
            case "EnclosingMethod":
                return new EnclosingMethod(data, pool);
            case "Exceptions":
                return new Exceptions(data, pool);
            case "InnerClasses":
                return new InnerClasses(data, pool);
            case "LineNumberTable":
                return new LineNumberTable(data);
            case "LocalVariableTable":
                return new LocalVariable(LocalVariableTable, data, pool);
            case "LocalVariableTypeTable":
                return new LocalVariable(LocalVariableTypeTable, data, pool);
            case "MethodParameters":
                return new MethodParameters(data, pool);
            case "RuntimeInvisibleAnnotations":
                return new RuntimeAnnotations(RuntimeInvisibleAnnotations, data, pool);
            case "RuntimeInvisibleParameterAnnotations":
                return new RuntimeParameterAnnotations(RuntimeInvisibleParameterAnnotations, data, pool);
            case "RuntimeInvisibleTypeAnnotations":
                return new RuntimeTypeAnnotations(RuntimeInvisibleTypeAnnotations, data, pool);
            case "RuntimeVisibleAnnotations":
                return new RuntimeAnnotations(RuntimeVisibleAnnotations, data, pool);
            case "RuntimeVisibleParameterAnnotations":
                return new RuntimeParameterAnnotations(RuntimeVisibleParameterAnnotations, data, pool);
            case "RuntimeVisibleTypeAnnotations":
                return new RuntimeTypeAnnotations(RuntimeVisibleTypeAnnotations, data, pool);
            case "Signature":
                return new Signature(data.readShort(), pool);
            case "SourceDebugExtension":
                return new SourceDebugExtension(length, data);
            case "SourceFile":
                return new SourceFile(data, pool);
            case "Synthetic":
                return new Synthetic();
            default:
                throw new AttributeTypeException(attrName);
        }
    }

    class AttributeTypeException extends RuntimeException {
        AttributeTypeException(String type) {
            super(type);
        }
    }
}