package sds.classfile.attributes.annotation;

/**
 * This interface is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a>.<br>
 * {@link TypeAnnotation <code>TypeAnnotation</code>} has union.
 * @author inagaki
 */
public interface TargetInfo {
    /**
     * returns target info type.
     * @return target info type
     */
    abstract TargetInfoType getType();
}