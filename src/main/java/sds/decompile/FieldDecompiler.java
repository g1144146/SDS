package sds.decompile;

import sds.assemble.BaseContent;
import sds.assemble.BaseContent.AnnotationContent;
import sds.assemble.FieldContent;

/**
 * This class is for decompiling contents of field.
 * @author inagaki
 */
public class FieldDecompiler extends AbstractDecompiler {
    /**
     * constructor.
     * @param result decompiled source
     */
    public FieldDecompiler(DecompiledResult result) {
        super(result);
    }

    @Override
    public void decompile(BaseContent content) {
        FieldContent field = (FieldContent)content;
        addAnnotation(field.getAnnotation());
        addDeclaration(field);
    }

    @Override
    void addDeclaration(BaseContent content) {
        FieldContent field = (FieldContent)content;
        StringBuilder fieldDeclaration = new StringBuilder();
        fieldDeclaration.append(field.getAccessFlag()).append(field.getDescriptor())
                        .append(" ").append(field.getName());
        if(field.getConstVal() != null) {
            fieldDeclaration.append(" = ").append(field.getConstVal());
        }
        fieldDeclaration.append(";");
        result.write(fieldDeclaration.toString());
    }
}