package sds.decompile;

import sds.assemble.ClassContent;
import sds.assemble.FieldContent;

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
		// class
		if(content.getAnnotation() != null) {
			for(String ann : content.getAnnotation().getAnnotations(true)) {
				result.write(ann);
			}
			for(String ann : content.getAnnotation().getAnnotations(false)) {
				result.write(ann);
			}
		}
		StringBuilder classDeclaration = new StringBuilder();
		classDeclaration.append(content.getAccessFlag()).append(content.getThisClass())
						.append(" extends ").append(content.getSuperClass());
		if(content.getInterfaces().length > 0) {
			classDeclaration.append(" implements ");
			String[] interfaces = content.getInterfaces();
			for(int i = 0; i < interfaces.length - 1; i++) {
				classDeclaration.append(interfaces[i]).append(", ");
			}
			classDeclaration.append(interfaces[interfaces.length - 1]);
		}
		classDeclaration.append(" {");
		result.write(classDeclaration.toString());
		result.changeIndent(DecompiledResult.INCREMENT);

		// field
		FieldDecompiler fDcom = new FieldDecompiler(result);
		for(FieldContent field : content.getFields()) {
			fDcom.decompile(field);
		}
		result = fDcom.getResult();

		// end
		result.writeEndScope();
		result.save();
		return result;
	}
}