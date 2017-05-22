package sds.classfile;

import sds.classfile.constantpool.ConstantInfo;
import sds.classfile.constantpool.Utf8ValueExtractor;

/**
 * This interface is for info which classfile has.
 * @author inagaki
 */
public interface Info {
    /**
     * returns value of
     * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
     * which is refered by sub-class of
     * {@link sds.classfile.constantpool.ConstantInfo <code>ConstantInfo</code>}
     * from index.
     * @param index constant-pool index
     * @param pool constant-pool
     * @return value of
     * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
     */
    default String extract(int index, ConstantInfo[] pool) {
        return Utf8ValueExtractor.extract(pool[index - 1], pool);
    }
}