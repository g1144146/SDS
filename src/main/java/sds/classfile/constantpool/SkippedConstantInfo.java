package sds.classfile.constantpool;

/**
 * This class is next ConstantInfo of
 * CONSTANT_DOUBLE_INFO's and CONSTANT_LONG_INFO's.<br>
 * These constants take up two entries in the constant-pool table.<br>
 * Then, this class is in order to put not null into these constants' next in constant-pool.
 * @author inagaki
 */
public class SkippedConstantInfo implements ConstantInfo {
    @Override
    public String toString() { return "null"; }
}
