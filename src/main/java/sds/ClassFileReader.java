package sds;

import java.io.InputStream;
import java.io.IOException;

import sds.classfile.ClassFileStream;
import sds.classfile.ClassFileStreamFactory;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeInfoFactory;
import sds.classfile.constantpool.ConstantInfo;
import sds.classfile.constantpool.ConstantInfoFactory;
import sds.classfile.constantpool.SkippedConstantInfo;
import static sds.classfile.constantpool.Utf8ValueExtractor.extract;

/**
 * This class is for reading classfile contents.
 * @author inagaki
 */
public class ClassFileReader {
    private ClassFileStream stream;
    private final ClassFile cf = new ClassFile();

    ClassFileReader(String fileName) {
        try {
            ClassFileStreamFactory factory = new ClassFileStreamFactory();
            this.stream = factory.create(fileName);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    ClassFileReader(InputStream input) {
        try {
            ClassFileStreamFactory factory = new ClassFileStreamFactory();
            this.stream = factory.create(input);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    void read() {
        try {
            cf.magicNumber = stream.readInt(); // 4byte
            cf.minorVersion = stream.readShort(); // 2byte
            cf.majorVersion = stream.readShort(); // 2byte
            readConstantPool();
            cf.accessFlag = stream.readShort();
            cf.thisClass = stream.readShort();
            cf.superClass = stream.readShort();
            readInterfaces();
            readMembers("field");
            readMembers("method");
            readAttributes();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void readConstantPool() throws Exception {
        ConstantInfo[] pool = new ConstantInfo[stream.readShort() - 1];
        ConstantInfoFactory factory = new ConstantInfoFactory();
        for(int i = 0; i < pool.length; i++) {
            int tag = stream.readByte(); // 1 byte
            pool[i] = factory.create(tag, stream);
            if(tag == ConstantInfoFactory.C_DOUBLE || tag == ConstantInfoFactory.C_LONG) {
                i++;
                pool[i] = new SkippedConstantInfo();
            }
        }
        cf.pool = pool;
    }

    private void readInterfaces() throws IOException {
        int[] interfaces = new int[stream.readShort()];
        for(int i = 0; i < interfaces.length; i++) {
            interfaces[i] = stream.readShort();
        }
        cf.interfaces = interfaces;
    }

    private void readMembers(String type) throws IOException {
        MemberInfo[] members = new MemberInfo[stream.readShort()];
        for(int i = 0; i < members.length; i++) {
            members[i] = new MemberInfo(stream, cf.pool);
        }
        if(type.equals("field")) {
            cf.fields = members;
            return;
        }
        cf.methods = members;
    }

    private void readAttributes() throws Exception {
        AttributeInfo[] attrs = new AttributeInfo[stream.readShort()];
        AttributeInfoFactory factory = new AttributeInfoFactory();
        for(int i = 0; i < attrs.length; i++) {
            String value = extract(stream.readShort(), cf.pool);
            attrs[i] = factory.create(value, stream, cf.pool);
        }
        cf.attr = attrs;
    }

    /**
     * returns classfile contents.
     * @return classfile
     */
    public ClassFile getClassFile() {
        return cf;
    }
}