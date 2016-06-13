package sds.assemble;

import sds.ClassFile;
import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.Methods;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.BootstrapMethods;
import sds.classfile.attributes.InnerClasses;
import sds.classfile.attributes.SourceFile;
import sds.classfile.attributes.annotation.RuntimeInvisibleTypeAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleTypeAnnotations;
import sds.classfile.attributes.annotation.SuperTypeTarget;
import sds.classfile.attributes.annotation.TargetInfo;
import sds.classfile.attributes.annotation.TypeAnnotation;
import static sds.util.Utf8ValueExtractor.extract;

/**
 * This class is for contents of class.
 * @author inagaki
 */
public class ClassContent extends BaseContent {
	private MethodContent[] methods;
	private FieldContent[] fields;
	private NestedClass[] nested;
	private String sourceFile;
	private String[] bootstrapMethods;
	private String superClass;
	private String[] interfaces;

	/**
	 * constructor.
	 * @param cf classfile
	 */
	public ClassContent(ClassFile cf) {
		this.contentType = Type.Class;
		ConstantPool pool = cf.getPool();
		this.superClass = extract(pool.get(cf.getSuperClass()-1), pool);
		if(cf.getInterfaces().length > 0) {
			int[] interIndex = cf.getInterfaces();
			this.interfaces = new String[interIndex.length];
			for(int i = 0; i < interfaces.length; i++) {
				interfaces[i] = extract(pool.get(interIndex[i]-1), pool);
			}
		}
		Methods method = cf.getMethods();
		this.methods = new MethodContent[method.size()];
		for(int i = 0; i < methods.length; i++) {
			methods[i] = new MethodContent(method.get(i), pool);
		}

		Fields field = cf.getFields();
		this.fields = new FieldContent[field.size()];
		for(int i = 0; i < fields.length; i++) {
			fields[i] = new FieldContent(field.get(i), pool);
		}

		InnerClasses ic = null;
		for(AttributeInfo info : cf.getAttr().getAll()) {
			if(info instanceof InnerClasses) {
				ic = (InnerClasses)info;
			}
			investigateAttribute(info, pool);
		}
		if(ic != null) {
			this.nested = new NestedClass[ic.getClasses().length];
		} else {
			this.nested = new NestedClass[0];
		}
	}

	public ClassContent() {}

	@Override
	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		switch(info.getType()) {
			case BootstrapMethods:
//				BootstrapMethods bsm = (BootstrapMethods)info;
//				this.bootstrapMethods = new String[bsm.getBSM().length];
//				for(BootstrapMethods.BSM b : bsm.getBSM()) {
//					for(int i = 0; i < b.getBSMArgs().length; i++) {
//						bootstrapMethods[i] = extract(pool.get(b.getBSMArgs()[i]-1), pool);
//					}
//				}
				break;
			case EnclosingMethod:
				// to do
				break;
			case InnerClasses:
				InnerClasses ic = (InnerClasses)info;
				for(InnerClasses.Classes c : ic.getClasses()) {
					int inner = c.getNumber("inner");
					int outer = c.getNumber("outer");
					int name = c.getNumber("inner_name");
					int accessFlag = c.getNumber("access_flag");
				}
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
				this.taContent = new ClassTypeAnnotationContent(rvta.getAnnotations(), pool, true);
				System.out.println("<<<Runtime Visible Type Annotation>>>: ");
				for(int i = 0; i < taContent.count; i++) {
					System.out.print(taContent.visible[i]);
					System.out.println(", " + taContent.targets[i]);
				}
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations)info;
				if(taContent == null) {
					this.taContent = new ClassTypeAnnotationContent(rita.getAnnotations(), pool, false);
				} else {
					taContent.setInvisible(rita.getAnnotations(), pool);
				}
				System.out.println("<<<Runtime Invisible Type Annotation>>>: ");
				for(int i = 0; i < taContent.count; i++) {
					System.out.print(taContent.invisible[i]);
					System.out.println(", " + taContent.invTargets[i]);
				}
				break;
			case SourceDebugExtension:
				// todo
				break;
			case SourceFile:
				SourceFile sf = (SourceFile)info;
				this.sourceFile = extract(pool.get(sf.getSourceFileIndex()-1), pool);
				break;
			default:
				super.investigateAttribute(info, pool);
				break;
		}
	}

	/**
	 * This class is
	 * {@link TypeAnnotationContent <code>TypeAnnotationContent</code>}
	 * for class.
	 */
	public class ClassTypeAnnotationContent extends TypeAnnotationContent {
		ClassTypeAnnotationContent(TypeAnnotation[] ta, ConstantPool pool, boolean isVisible) {
			super(ta, pool, isVisible);
		}

		@Override
		String initTarget(TargetInfo target, int annIndex, boolean isVisible) {
			StringBuilder sb = new StringBuilder(target.getType().toString());
			String annotation = isVisible ? visible[annIndex] : invisible[annIndex];
			switch(target.getType()) {
				case SuperTypeTarget:
					SuperTypeTarget stt = (SuperTypeTarget)target;
					if(stt.getIndex() == -1) {
						superClass = annotation + " " + superClass;
						System.out.println(superClass);
					} else {
						interfaces[stt.getIndex()] = annotation + " " + interfaces[stt.getIndex()];
						System.out.println(interfaces[stt.getIndex()]);
					}
					break;
				case TypeParameterTarget:      break;
				case TypeParameterBoundTarget: break;
			}
			return sb.toString();
		}
	}
}