package sds.decompile;

import sds.assemble.BaseContent;
import sds.assemble.BaseContent.AnnotationContent;
import sds.assemble.FieldContent;
import sds.util.SDSStringBuilder;

/**
 * This class is for decompiling contents of field.
 * @author inagaki
 */
public class FieldDecompiler extends AbstractDecompiler {
    FieldDecompiler(DecompiledResult result) {
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
        SDSStringBuilder fieldDeclaration = new SDSStringBuilder();
        fieldDeclaration.append(field.getAccessFlag(), field.getDescriptor(), " ", field.getName());
        if(field.getConstVal() != null) {
            fieldDeclaration.append(" = ", field.getConstVal());
        }
        fieldDeclaration.append(";");
        result.write(fieldDeclaration.toString());
    }
}