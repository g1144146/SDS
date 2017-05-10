package sds.decompile;

import sds.assemble.ClassContent;

/**
 * This class is for to process decompiling.
 * @author inagaki
 */
public class DecompileProcessor {
    /**
     * returns result of decompiled contents of class.
     * @param content contents of class
     * @return decompiled result
     */
    public DecompiledResult process(ClassContent content) {
        DecompiledResult result = new DecompiledResult(content.getSourceFile());
        ClassDecompiler cd = new ClassDecompiler(result);
        cd.decompile(content);
        result = cd.getResult();
        result.save();
        return result;
    }
}