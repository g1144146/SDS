package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.util.Arrays;
import sds.classfile.ClassFileStream;

/**
 * This interface is for
 * same_frame, same_locals_1_stack_item_frame, same_locals_stack_item_frame_extended,
 * chop_frame, append_frame, full_frame and same_frame_extended
 * which {@link StackMapTable <code>StackMapTable</code>} has union.
 * @author inagaki
 */
interface StackMapFrame {
    /**
     * returns type of stack-map-frame.
     * @return type
     */
    abstract StackMapFrameType getFrameType();

    /**
     * returns discrimination tag of item of union.
     * @return discrimination tag
     */
    abstract int getTag();
}

class SameFrame implements StackMapFrame {
    private StackMapFrameType frameType;
    private int tag;
    VerificationTypeInfo stack;

    SameFrame(StackMapFrameType frameType, int tag) {
        this.frameType = frameType;
        this.tag = tag;
    }

    /**
     * returns verification type info.
     * @return verification type info
     */
    public VerificationTypeInfo getStack() {
        if(stack == null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        return stack;
    }

    @Override
    public StackMapFrameType getFrameType() {
        return frameType;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return frameType + "[" + tag + "]"; 
    }
}

class SameLocals1StackItemFrame extends SameFrame {
    SameLocals1StackItemFrame(int tag, ClassFileStream data) throws IOException {
        super(StackMapFrameType.SameLocals1StackItemFrame, tag);
        VerificationTypeInfoFactory factory = new VerificationTypeInfoFactory();
        this.stack = factory.create(data);
    }

    @Override
    public String toString() {
        return super.toString() + ", " + stack; 
    }
}

class SameLocals1StackItemFrameExtended extends SameFrame {
    private int offset;

    SameLocals1StackItemFrameExtended(int tag, ClassFileStream data) throws IOException {
        super(StackMapFrameType.SameLocals1StackItemFrameExtended, tag);
        this.offset = data.readShort();
        VerificationTypeInfoFactory factory = new VerificationTypeInfoFactory();
        this.stack = factory.create(data);
    }

    /**
     * returns offset.
     * @return offset
     */
    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + stack + ", offset: " + offset; 
    }
}

class ChopFrame extends SameFrame {
    private int offset;
    VerificationTypeInfo[] locals;

    ChopFrame(StackMapFrameType type, int tag, int offset) throws IOException {
        super(type, tag);
        this.offset = offset;
    }

    /**
     * returns offset.
     * @return offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * returns verification type info.
     * @return verification type info
     */
    public VerificationTypeInfo[] getLocals() {
        if(locals == null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        return locals;
    }

    @Override
    public String toString() {
        return super.toString() + ": offset: " + offset; 
    }
}

class AppendFrame extends ChopFrame {
    AppendFrame(int tag, ClassFileStream data) throws IOException, VerificationTypeException {
        super(StackMapFrameType.AppendFrame, tag, data.readShort());
        this.locals = new VerificationTypeInfo[tag - 251];
        VerificationTypeInfoFactory factory = new VerificationTypeInfoFactory();
        for(int i = 0; i < locals.length; i++) {
            locals[i] = factory.create(data);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", " + Arrays.toString(locals); 
    }
}

class FullFrame extends ChopFrame {
    private VerificationTypeInfo[] stacks;

    FullFrame(int tag, ClassFileStream data) throws IOException, VerificationTypeException {
        super(StackMapFrameType.FullFrame, tag, data.readShort());
        VerificationTypeInfoFactory factory = new VerificationTypeInfoFactory();
        this.locals = new VerificationTypeInfo[data.readShort()];
        for(int i = 0; i < locals.length; i++) {
            locals[i] = factory.create(data);
        }
        this.stacks = new VerificationTypeInfo[data.readShort()];
        for(int i = 0; i < stacks.length; i++) {
            stacks[i] = factory.create(data);
        }
    }

    /**
     * returns verification type info of stack.
     * @return verificatino type info
     */
    public VerificationTypeInfo[] getStacks() {
        return stacks;
    }

    @Override
    public String toString() {
        return super.toString() + ", locals: " + Arrays.toString(locals) + ", stacks: " + Arrays.toString(locals); 
    }
}

class SameFrameExtended extends ChopFrame {
    SameFrameExtended(int tag, ClassFileStream data) throws IOException {
        super(StackMapFrameType.SameFrameExtended, tag, data.readShort());
    }
}