package sds.decompile;

import sds.assemble.BaseContent;

/**
 * This interface is for decompiling contents of class and member.
 * @author inagaki
 */
public interface Decompiler {
    /**
     * decompiles specified content.
     * @param content decompile target content
     */
    void decompile(BaseContent content);

    /**
     * decompiles specified contents.
     * @param contents decompile target contents
     */
    void decompile(BaseContent[] contents);
}