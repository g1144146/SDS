package sds.util;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import static sds.util.AccessFlags.*;

public class AccessFlagsTest {
    @Test
    public void getTest() {
        // class and nested.
        assertThat(get(PUBLIC.key | FINAL.key | SYNTHETIC.key | INTERFACE.key, "class")
                , is("public final synthetic interface "));
        assertThat(get(PUBLIC.key | FINAL.key | ENUM.key, "class"), is("public final enum "));
        assertThat(get(PUBLIC.key | ANNOTATION.key, "class"), is("public @interface "));
        assertThat(get(PUBLIC.key | FINAL.key | STATIC.key, "nested"), is("public static final class "));

        // field.
        assertThat(get(PUBLIC.key | FINAL.key | STATIC.key | VOLATILE.key | TRANSIENT.key | SYNTHETIC.key | ENUM.key,
                        "field"), is("public static final volatile transient synthetic enum "));
        assertThat(get(PRIVATE.key | FINAL.key | STATIC.key, "field"), is("private static final "));
        assertThat(get(PROTECTED.key | FINAL.key, "field"), is("protected final "));

        // method
        assertThat(get(PUBLIC.key | FINAL.key | STATIC.key | SYNCHRONIZED.key | SYNTHETIC.key |
                        BRIDGE | NATIVE.key | ABSTRACT.key | STRICT.key , "method")
                    , is("public static final synchronized native abstract strict synthetic "));
    }

    @Test(expected = RuntimeException.class)
    public void testException() throws Exception {
        get(PUBLIC.key | STATIC.key | FINAL.key, "class");
        get(PROTECTED.key | FINAL.key | INTERFACE.key, "field");
        get(PRIVATE.key | FINAL.key | STATIC.key | ANNOTATION.key, "method");
    }
}