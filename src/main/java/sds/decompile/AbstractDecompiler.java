package sds.decompile;

import sds.assemble.BaseContent;
import sds.assemble.BaseContent.AnnotationContent;

/**
 * This adapter class is for decompiler.
 * @author inagaki
 */
public abstract class AbstractDecompiler implements Decompiler {
    DecompiledResult result;

    AbstractDecompiler(DecompiledResult result) {
        this.result = result;
    }

    abstract void addDeclaration(BaseContent content);

    void addAnnotation(AnnotationContent annotation) {
        if(annotation != null) {
            // runtime visible annotation
            for(String ann : annotation.getAnnotations(true)) {
                result.write(ann);
            }
            // runtime invisible annotation
            for(String ann : annotation.getAnnotations(false)) {
                result.write(ann);
            }
        }
    }

    /**
     * retunrs decompiled source.
     * @return decompiled source
     */
    public DecompiledResult getResult() {
        return result;
    }

    @Override
    public void decompile(BaseContent[] contents) {
        for(BaseContent content : contents) {
            decompile(content);
        }
    }
}