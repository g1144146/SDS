package sds.assemble;

import sds.ClassFile;
import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.Methods;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.BootstrapMethods;
import sds.classfile.attributes.InnerClasses;
import sds.classfile.attributes.SourceFile;
import sds.util.ClassFilePrinter;

/**
 *
 * @author inagaki
 */
public class ClassContent extends BaseContent {
	/**
	 * 
	 */
	MethodContent[] methods;
	/**
	 * 
	 */
	FieldContent[] fields;
	/**
	 * 
	 */
	NestedClass[] nested;
	/**
	 * 
	 */
	String sourceFile;
	/**
	 * 
	 */
	String[] bootstrapMethods;

	public ClassContent(ClassFile cf) {
		Methods method = cf.getMethods();
		this.methods = new MethodContent[method.size()];
		for(int i = 0; i < methods.length; i++) {
			methods[i] = new MethodContent(method.get(i), cf.getPool());
		}

		Fields field = cf.getFields();
		this.fields = new FieldContent[field.size()];
		for(int i = 0; i < fields.length; i++) {
			fields[i] = new FieldContent(field.get(i), cf.getPool());
		}

		InnerClasses ic = null;
		for(AttributeInfo info : cf.getAttr().getAll()) {
			if(info instanceof InnerClasses) {
				ic = (InnerClasses)info;
				break;
			}
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
		super.investigateAttribute(info, pool);
		ClassFilePrinter cfp = new ClassFilePrinter();
		switch(info.getType()) {
			case BootstrapMethods:
				BootstrapMethods bsm = (BootstrapMethods)info;
				this.bootstrapMethods = new String[bsm.getBSM().length];
				for(BootstrapMethods.BSM b : bsm.getBSM()) {
					for(int i = 0; i < b.getBSMArgs().length; i++) {
						bootstrapMethods[i] = cfp.getUtf8Value(pool.get(b.getBSMArgs()[i]-1));
					}
				}
				break;
			case InnerClasses:
				InnerClasses ic = (InnerClasses)info;
				for(InnerClasses.Classes c : ic.getClasses()) {
//					int inner = c.getNumber("inner");
//					int outer = c.getNumber("outer");
//					int name = c.getNumber("inner_name");
//					int accessFlag = c.getNumber("access_flag");
//					if(checkRange(inner-1)) {
//						out.println("\tinner_class: " + AccessFlags.get(accessFlag, "nested")
//								+ getUtf8Value(pool.get(inner-1)));
//					}
//					if(checkRange(outer-1)) {
//						out.println("\touter_class: " + getUtf8Value(pool.get(outer-1)));
//					}
//					if(checkRange(name-1)) {
//						out.println("\t" + getUtf8Value(pool.get(name-1)));
//					}
				}
			case SourceFile:
				SourceFile sf = (SourceFile)info;
				this.sourceFile = cfp.getUtf8Value(pool.get(sf.getSourceFileIndex()-1));
				break;
		}
	}
}
