package sds.assemble;

import sds.ClassFile;
import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.Methods;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.BootstrapMethods;
import sds.classfile.attributes.EnclosingMethod;
import sds.classfile.attributes.InnerClasses;
import sds.classfile.attributes.SourceFile;
import sds.classfile.attributes.annotation.RuntimeInvisibleTypeAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleTypeAnnotations;
import sds.classfile.attributes.annotation.SuperTypeTarget;
import sds.classfile.attributes.annotation.TargetInfo;
import static sds.util.AccessFlags.get;
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
	private String accessFlag;
	private String thisClass;
	private String superClass;
	private String[] interfaces;
	private boolean isEnclosed;
	private String enclosingClass;
	private String enclosingMethod;

	/**
	 * constructor.
	 * @param cf classfile
	 */
	public ClassContent(ClassFile cf) {
		ConstantPool pool = cf.getPool();
		this.accessFlag = get(cf.getAccessFlag(), "class");
		this.thisClass  = extract(pool.get(cf.getThisClass()-1),  pool);
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
			examineAttribute(info, pool);
		}
		if(ic != null) {
			this.nested = new NestedClass[ic.getClasses().length];
		}
	}

	public ClassContent() {}

	@Override
	public void examineAttribute(AttributeInfo info, ConstantPool pool) {
		switch(info.getType()) {
			case BootstrapMethods:
				BootstrapMethods bsm = (BootstrapMethods)info;
				this.bootstrapMethods = new String[bsm.getBSM().length];
				int index = 0;
				for(BootstrapMethods.BSM b : bsm.getBSM()) {
					for(String arg : b.getBSMArgs()) {
						if(! arg.startsWith("(")) {
							bootstrapMethods[index++] = arg;
						}
					}
				}
				break;
			case EnclosingMethod:
				EnclosingMethod em = (EnclosingMethod)info;
				this.enclosingClass  = em.getEncClass();
				this.enclosingMethod = em.getEncMethod();
				break;
			case InnerClasses:
				InnerClasses ic = (InnerClasses)info;
				for(InnerClasses.Classes c : ic.getClasses()) {
//					int inner = c.getNumber("inner");
//					int outer = c.getNumber("outer");
//					int name = c.getNumber("inner_name");
//					int accessFlag = c.getNumber("access_flag");
				}
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
				this.taContent = new ClassTypeAnnotationContent(rvta.getAnnotations(), true);
				System.out.println("<<<Runtime Visible Type Annotation>>>: ");
				for(int i = 0; i < taContent.count; i++) {
					System.out.print(taContent.visible[i]);
					System.out.println(", " + taContent.targets[i]);
				}
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations)info;
				if(taContent == null) {
					this.taContent = new ClassTypeAnnotationContent(rita.getAnnotations(), false);
				} else {
					taContent.setInvisible(rita.getAnnotations());
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
				this.sourceFile = sf.getSourceFile();
				break;
			default:
				super.examineAttribute(info, pool);
				break;
		}
	}

	/**
	 * returns assembled methods which this class has.
	 * @return assembled methods
	 */
	public MethodContent[] getMethods() {
		return methods != null ? methods : new MethodContent[0];
	}

	/**
	 * returns assembled fields which this class has.
	 * @return assembled fields
	 */
	public FieldContent[] getFields() {
		return fields != null ? fields : new FieldContent[0];
	}

	/**
	 * returns assembled nested classes which this class has.
	 * @return assembled nested classes
	 */
	public NestedClass[] getNested() {
		return nested != null ? nested : new NestedClass[0];
	}

	/**
	 * return source file name of this class.
	 * @return source file name
	 */
	public String getSourceFile() {
		return sourceFile;
	}

	/**
	 * returns access flag of this class.
	 * @return access flag
	 */
	public String getAccessFlag() {
		return accessFlag;
	}

	/**
	 * returns this class.
	 * @return 
	 */
	public String getThisClass() {
		return thisClass;
	}

	/**
	 * returns super class of this class.
	 * @return 
	 */
	public String getSuperClass() {
		return superClass;
	}

	/**
	 * returns interfaces which this class implements.
	 * @return 
	 */
	public String[] getInterfaces() {
		return interfaces != null ? interfaces : new String[0];
	}

	/**
	 * This class is
	 * {@link TypeAnnotationContent <code>TypeAnnotationContent</code>}
	 * for class.
	 */
	public class ClassTypeAnnotationContent extends TypeAnnotationContent {
		ClassTypeAnnotationContent(String[] ta, boolean isVisible) {
			super(ta, isVisible);
		}

		@Override
		void initTarget(TargetInfo target, int annIndex, boolean isVisible) {
			String annotation = isVisible ? visible[annIndex] : invisible[annIndex];
			switch(target.getType()) {
				case SuperTypeTarget:
					SuperTypeTarget stt = (SuperTypeTarget)target;
					if(stt.getIndex() == -1) {
						superClass = annotation + " " + superClass;
					} else {
						interfaces[stt.getIndex()] = annotation + " " + interfaces[stt.getIndex()];
					}
					break;
				case TypeParameterTarget:      break;
				case TypeParameterBoundTarget: break;
			}
		}
	}
}