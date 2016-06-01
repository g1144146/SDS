package sds.assemble;

import sds.ClassFile;
import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.Methods;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.BootstrapMethods;
import sds.classfile.attributes.InnerClasses;
import sds.classfile.attributes.SourceFile;
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

	/**
	 * constructor.
	 * @param cf classfile
	 */
	public ClassContent(ClassFile cf) {
		this.contentType = Type.Class;
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
			}
//			investigateAttribute(info, cf.getPool());
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
				BootstrapMethods bsm = (BootstrapMethods)info;
				this.bootstrapMethods = new String[bsm.getBSM().length];
				for(BootstrapMethods.BSM b : bsm.getBSM()) {
					for(int i = 0; i < b.getBSMArgs().length; i++) {
						bootstrapMethods[i] = extract(pool.get(b.getBSMArgs()[i]-1), pool);
					}
				}
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
}