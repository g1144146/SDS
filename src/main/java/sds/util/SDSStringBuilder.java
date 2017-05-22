package sds.util;

/**
 *
 * @author inagakikenichi
 */
public class SDSStringBuilder {
    private StringBuilder sb = new StringBuilder();

    public SDSStringBuilder() {}
    public SDSStringBuilder(String init) {
        sb.append(init);
    }

    public void append(Object... strs) {
        for(Object str : strs) {
            sb.append(str);
        }
    }

    public int length() {
        return sb.length();
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
