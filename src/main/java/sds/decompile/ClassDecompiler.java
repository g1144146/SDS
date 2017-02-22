package sds.decompile;

import java.util.Map;
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

		MethodDecompiler methodDecom = new MethodDecompiler(fieldDecom.getResult(), cc.getThisClass());
		methodDecom.decompile(cc.getMethods());

		// end
		result.writeEndScope();
	}

	@Override
	void addDeclaration(BaseContent content) {
		ClassContent cc = (ClassContent)content;
		StringBuilder classDeclaration = new StringBuilder();
		classDeclaration.append(cc.getAccessFlag()).append(cc.getThisClass());

		// generics
		if(cc.getGenericsMap().size() > 0) {
			classDeclaration.append("<");
			StringBuilder generics = new StringBuilder();
			for(Map.Entry<String, String> e : cc.getGenericsMap().entrySet()) {
				generics.append(e.getKey()).append(" extends ").append(e.getValue()).append(",");
			}
			classDeclaration.append(generics.toString().substring(0, generics.length()-1 ));
			classDeclaration.append(">");
		}

		// extends classes
		String extendClass = cc.getSuperClass();
		if(! extendClass.equals("Object")) {
			classDeclaration.append(" extends ").append(extendClass);
		}

		// implements interfaces
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