package sds.classfile.attributes.annotation;

/**
 * This class is for empty_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class EmptyTarget extends AbstractTargetInfo {
    EmptyTarget() {
        super(TargetInfoType.EmptyTarget);
    }
}
