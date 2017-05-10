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
     * @throws StackMapFrameException 
     * @throws sds.classfile.attributes.stackmap.VerificationTypeException 
     */
    public StackMapFrame create(ClassFileStream data) throws IOException {
        int tag = data.readUnsignedByte();
        if(0 <= tag && tag <= 63) {
            return new SameFrame(StackMapFrameType.SameFrame, tag);
        }
        if(64 <= tag && tag <= 127) {
            return new SameLocals1StackItemFrame(tag, data);
        }
        if(128 <= tag && tag <= 246) {
            throw new StackMapFrameException(" the range of [128-246] is reserved for future use.");
        }
        if(tag == 247) {
            return new SameLocals1StackItemFrameExtended(tag, data);
        }
        if(248 <= tag && tag <= 250) {
            return new ChopFrame(StackMapFrameType.ChopFrame, tag, data.readShort());
        }
        if(tag == 251) {
            return new SameFrameExtended(tag, data);
        }
        if(252 <= tag && tag <= 254) {
            return new AppendFrame(tag, data);
        }
        if(tag == 255) {
            return new FullFrame(tag, data);
        }
        throw new StackMapFrameException(tag);
    }
}