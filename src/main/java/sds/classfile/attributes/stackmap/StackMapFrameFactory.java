package sds.classfile.attributes.stackmap;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This factory class is for {@link StackMapFrame <code>StackMapFrame</code>}.
 * @author inagaki
 */
public class StackMapFrameFactory {
    /**
     * returns stack-map-frame.
     * @param data classfile stream
     * @return stack-map-frame
     * @throws IOException
     */
    public StackMapFrame create(ClassFileStream data) throws IOException {
        int tag = data.readUnsignedByte();
        if(check(tag, 0,   63))  return new SameFrame(StackMapFrameType.SameFrame, tag);
        if(check(tag, 64,  127)) return new SameLocals1StackItemFrame(tag, data);
        if(tag == 247)           return new SameLocals1StackItemFrameExtended(tag, data);
        if(check(tag, 248, 250)) return new ChopFrame(StackMapFrameType.ChopFrame, tag, data.readShort());
        if(tag == 251)           return new SameFrameExtended(tag, data);
        if(check(tag, 252, 254)) return new AppendFrame(tag, data);
        if(tag == 255)           return new FullFrame(tag, data);
        if(check(tag, 128, 246)) {
            throw new StackMapFrameException(" the range of [128-246] is reserved for future use.");
        }
        throw new StackMapFrameException(tag);
    }
    
    private boolean check(int tag, int start, int end) { return start <= tag && tag <= end; }

    class StackMapFrameException extends RuntimeException {
        StackMapFrameException(int tag) {
            super(Integer.toString(tag));
        }

        StackMapFrameException(String message) {
            super(message);
        }
    }
}