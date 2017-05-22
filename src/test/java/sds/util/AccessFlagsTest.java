package sds.util;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import static sds.util.AccessFlags.*;

public class AccessFlagsTest {
    @Test
    public void getTest() {
        // class and nested.
        assertThat(get(ACC_PUBLIC | ACC_FINAL | ACC_SYNTHETIC | ACC_INTERFACE, "class")
                , is("public final synthetic interface "));
        assertThat(get(ACC_PUBLIC | ACC_FINAL | ACC_ENUM, "class"), is("public final enum "));
        assertThat(get(ACC_PUBLIC | ACC_ANNOTATION, "class"), is("public @interface "));
        assertThat(get(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "nested"), is("public static final class "));

        // field.
        assertThat(get(ACC_PUBLIC | ACC_FINAL | ACC_STATIC | ACC_VOLATILE | ACC_TRANSIENT | ACC_SYNTHETIC | ACC_ENUM,
                        "field"), is("public static final volatile transient synthetic enum "));
        assertThat(get(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "field"), is("private static final "));
        assertThat(get(ACC_PROTECTED | ACC_FINAL, "field"), is("protected final "));

        // method
        assertThat(get(ACC_PUBLIC | ACC_FINAL | ACC_STATIC | ACC_SYNCHRONIZED | ACC_SYNTHETIC
                        | ACC_BRIDGE | ACC_NATIVE | ACC_ABSTRACT | ACC_STRICT , "method")
                    , is("public static final synchronized bridge native abstract strict synthetic "));
    }

    @Test(expected = RuntimeException.class)
    public void testException() throws Exception {
        get(ACC_PUBLIC | ACC_STATIC | ACC_FINAL, "class");
        get(ACC_PROTECTED | ACC_FINAL | ACC_INTERFACE, "field");
        get(ACC_PRIVATE | ACC_FINAL | ACC_STATIC | ACC_ANNOTATION, "method");
    }
}