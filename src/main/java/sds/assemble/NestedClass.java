package sds.assemble;

import static sds.util.Printer.println;

/**
 * This class is for contents of nested class.
 * @author inagaki
 */
public class NestedClass extends ClassContent {
    NestedClass(String[] c) {
        super();
        String inner = c[0];
        String outer = c[1];
        String name  = c[2];
        this.accessFlag = c[3];
        println("inner: " + this.accessFlag + inner + ", outer: " + outer + ", name: " + name);
    }
}