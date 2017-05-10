package sds.assemble;

/**
 * This class is for contents of nested class.
 * @author inagaki
 */
public class NestedClass extends ClassContent {
    public NestedClass(String[] c) {
        super();
        String inner = c[0];
        String outer = c[1];
        String name  = c[2];
        this.accessFlag = c[3];
    }
}