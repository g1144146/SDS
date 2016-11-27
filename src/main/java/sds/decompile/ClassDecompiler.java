package sds.decompile;

import sds.assemble.BaseContent;
import sds.assemble.ClassContent;

/**
 * This class is for decompiling contents of class.
 * @author inagaki
 */
public class ClassDecompiler extends AbstractDecompiler {
	/**
	 * constructor.
	 * @param result decompiled source
	 */
	public ClassDecompiler(DecompiledResult result) {
		super(result);
	}

	@Override
	public void decompile(BaseContent content) {
		ClassContent cc = (ClassContent)content;
		addAnnotation(cc.getAnnotation());
		addDeclaration(cc);
		result.changeIndent(DecompiledResult.INCREMENT);

		FieldDecompiler fieldDecom = new FieldDecompiler(result);
		fieldDecom.decompile(cc.getFields());

		MethodDecompiler methodDecom = new MethodDecompiler(fieldDecom.getResult());
		methodDecom.decompile(cc.getMethods());

		// end
		result.writeEndScope();
	}

	@Override
	void addDeclaration(BaseContent content) {
		ClassContent cc = (ClassContent)content;
		StringBuilder classDeclaration = new StringBuilder();
		classDeclaration.append(cc.getAccessFlag()).append(cc.getThisClass())
						.append(" extends ").append(cc.getSuperClass());
		if(cc.getInterfaces().length > 0) {
			classDeclaration.append(" implements ");
			String[] interfaces = cc.getInterfaces();
			for(int i = 0; i < interfaces.length - 1; i++) {
				classDeclaration.append(interfaces[i]).append(", ");
			}
			classDeclaration.append(interfaces[interfaces.length - 1]);
		}
		classDeclaration.append(" {");
		result.write(classDeclaration.toString());
	}
}
