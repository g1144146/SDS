package sophomore;

import java.io.IOException;
import java.io.PrintStream;

import sophomore.classfile.AccessFlags;
import sophomore.classfile.ConstantPool;
import sophomore.classfile.DescriptorParser;
import sophomore.classfile.FieldInfo;
import sophomore.classfile.MethodInfo;
import sophomore.classfile.MemberInfo;
import sophomore.classfile.attributes.AttributeInfo;
import sophomore.classfile.attributes.BootstrapMethods;
import sophomore.classfile.attributes.Code;
import sophomore.classfile.attributes.ConstantValue;
import sophomore.classfile.attributes.EnclosingMethod;
import sophomore.classfile.attributes.Exceptions;
import sophomore.classfile.attributes.InnerClasses;
import sophomore.classfile.attributes.LineNumberTable;
import sophomore.classfile.attributes.LocalVariable;
import sophomore.classfile.attributes.LocalVariableTable;
import sophomore.classfile.attributes.LocalVariableTypeTable;
import sophomore.classfile.attributes.MethodParameters;
import sophomore.classfile.attributes.Signature;
import sophomore.classfile.attributes.SourceDebugExtension;
import sophomore.classfile.attributes.SourceFile;
import sophomore.classfile.attributes.Synthetic;
import sophomore.classfile.attributes.annotation.Annotation;
import sophomore.classfile.attributes.annotation.AnnotationDefault;
import sophomore.classfile.attributes.annotation.ElementValue;
import sophomore.classfile.attributes.annotation.ElementValuePair;
import sophomore.classfile.attributes.annotation.EnumConstValue;
import sophomore.classfile.attributes.annotation.ParameterAnnotations;
import sophomore.classfile.attributes.annotation.RuntimeInvisibleAnnotations;
import sophomore.classfile.attributes.annotation.RuntimeInvisibleParameterAnnotations;
import sophomore.classfile.attributes.annotation.RuntimeInvisibleTypeAnnotations;
import sophomore.classfile.attributes.annotation.RuntimeVisibleAnnotations;
import sophomore.classfile.attributes.annotation.RuntimeVisibleParameterAnnotations;
import sophomore.classfile.attributes.annotation.RuntimeVisibleTypeAnnotations;
import sophomore.classfile.attributes.annotation.TypeAnnotation;
import sophomore.classfile.attributes.stackmap.StackMapTable;
import sophomore.classfile.bytecode.Bipush;
import sophomore.classfile.bytecode.BranchOpcode;
import sophomore.classfile.bytecode.BranchWideOpcode;
import sophomore.classfile.bytecode.CpRefOpcode;
import sophomore.classfile.bytecode.Iinc;
import sophomore.classfile.bytecode.IndexOpcode;
import sophomore.classfile.bytecode.InvokeDynamic;
import sophomore.classfile.bytecode.InvokeInterface;
import sophomore.classfile.bytecode.LookupSwitch;
import sophomore.classfile.bytecode.MultiANewArray;
import sophomore.classfile.bytecode.NewArray;
import sophomore.classfile.bytecode.OpcodeInfo;
import sophomore.classfile.bytecode.Sipush;
import sophomore.classfile.bytecode.TableSwitch;
import sophomore.classfile.bytecode.Wide;
import sophomore.classfile.constantpool.ClassInfo;
import sophomore.classfile.constantpool.ConstantInfo;
import sophomore.classfile.constantpool.DoubleInfo;
import sophomore.classfile.constantpool.FieldrefInfo;
import sophomore.classfile.constantpool.FloatInfo;
import sophomore.classfile.constantpool.IntegerInfo;
import sophomore.classfile.constantpool.InterfaceMethodrefInfo;
import sophomore.classfile.constantpool.InvokeDynamicInfo;
import sophomore.classfile.constantpool.LongInfo;
import sophomore.classfile.constantpool.MethodHandleInfo;
import sophomore.classfile.constantpool.MethodrefInfo;
import sophomore.classfile.constantpool.MethodTypeInfo;
import sophomore.classfile.constantpool.NameAndTypeInfo;
import sophomore.classfile.constantpool.StringInfo;
import sophomore.classfile.constantpool.Utf8Info;

import static sophomore.classfile.constantpool.ConstantType.*;

/**
 *
 * @author inagaki
 */
public class ClassFilePrinter {
	private ClassFile cf;
	private PrintStream out = System.out;
	private ConstantPool pool;
	private String sep = System.getProperty("line.separator");
	public ClassFilePrinter(ClassFile cf) {
		this.cf = cf;
		this.pool = cf.pool;
	}

	public ClassFilePrinter() {}

	public void print() {
		try {
			printNumber();
			printConstantPool();
			printAccessFlag();
			printThisClass();
			printSuperClass();
			printInterface();
			printFields();
			printMethods();
			printAttributes();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void printNumber() {
		out.println("*** Major Version *** " + sep + cf.majorVersion);
		out.println("*** Minor Version *** " + sep + cf.minorVersion);
		out.println(sep);
	}

	public void printConstantPool() {
		out.println(pool);
		out.println(sep);
	}

	public void printAccessFlag() {
		out.println("*** Access Flag *** ");
		out.println(AccessFlags.get(cf.accessFlag));
		out.println(sep);
	}

	public void printThisClass() {
		out.println("*** This Class *** ");
		if(!checkRange(cf.thisClass)) {
			out.println(sep);
			return;
		}
		out.println(getUtf8Value(pool.get(cf.thisClass-1)));
		out.println(sep);
	}

	public void printSuperClass() {
		out.println("*** Super Class *** ");
		if(!checkRange(cf.superClass)) {
			out.println(sep);
			return;
		}
		out.println(getUtf8Value(pool.get(cf.superClass-1)));
		out.println(sep);
	}

	public void printInterface() {
		out.println("*** Interface *** ");
		if(cf.interfaces.length == 0) {
			out.println(sep);
			return;
		}
		for(int i : cf.interfaces) {
			ClassInfo info = (ClassInfo)pool.get(i-1);
			Utf8Info utf = (Utf8Info)pool.get(info.getNameIndex()-1);
			out.println(utf.getValue());
		}
		out.println(sep);
	}

	public void printFields() throws IOException {
		out.println("*** Fields *** ");
		if(cf.fields.getSize() == 0) {
			out.println(sep);
			return;
		}
		for(FieldInfo info : (FieldInfo[])cf.fields.getAll()) {
			out.println("\t"+AccessFlags.get(info.getAccessFlags()) + getUtf8Value(info));
			for(AttributeInfo attr : info.getAttr().getAll()) {
				printAttributeInfo(attr);
			}
		}
		out.println(sep);
	}

	public void printMethods() throws IOException {
		out.println("*** Methods *** ");
		if(cf.methods.getSize() == 0) {
			out.println(sep);
			return;
		}
		for(MethodInfo info : (MethodInfo[])cf.methods.getAll()) {
			out.println("["+AccessFlags.get(info.getAccessFlags()) + getUtf8Value(info) + "]");
			for(AttributeInfo attr : info.getAttr().getAll()) {
				printAttributeInfo(attr);
			}
		}
		out.println(sep);
	}

	public void printAttributes() throws IOException {
		out.println("*** Attributes *** ");
		if(cf.attr.getSize() == 0) {
			out.println(sep);
			return;
		}
		for(AttributeInfo attr : cf.attr.getAll()) {
			printAttributeInfo(attr);
		}
		out.println(sep);
	}

	public void printAttributeInfo(AttributeInfo info) throws IOException {
		out.println("   [attribute type]: " + info.getType().toString());
		switch(info.getType()) {
			case AnnotationDefault:
				AnnotationDefault ad = (AnnotationDefault)info;
				printElementValue(ad.getDefaultValue());
				break;
			case BootstrapMethods:
				BootstrapMethods bsm = (BootstrapMethods)info;
				for(BootstrapMethods.BSM b : bsm.getBSM()) {
					out.println("\t" + getUtf8Value(pool.get(b.getBSMRef()-1)));
				}
				break;
			case Code:
				Code code = (Code)info;
				out.println("\tmax_stack: " + code.getMaxStack());
				out.println("\tmax_locals: " + code.maxLocals());
				for(OpcodeInfo op : code.getCode().getAll()) {
					out.print("\topcode: "+op.getPc()+" - "+op.getOpcodeType());
					if(op instanceof Bipush) {
						Bipush bi = (Bipush)op;
						out.println(", operand: " + bi.getByte());
					} else if(op instanceof BranchOpcode) {
						BranchOpcode branch = (BranchOpcode)op;
						out.println(", operand: " + branch.getBranch());
					} else if(op instanceof Iinc) {
						Iinc iinc = (Iinc)op;
						out.println(", operand: " + iinc.getIndex() +","+iinc.getConst());
					} else if(op instanceof IndexOpcode) {
						IndexOpcode io = (IndexOpcode)op;
						out.println(", operand: " + io.getIndex());
					} else if(op instanceof InvokeDynamic) {
						InvokeDynamic id = (InvokeDynamic)op;
						out.println(", operand: " + "#" + id.getIndexByte());
					} else if(op instanceof InvokeInterface) {
						InvokeInterface ii = (InvokeInterface)op;
						out.println(", operand: " + "#" + ii.getIndexByte() + ","+ii.getCount());
					} else if(op instanceof LookupSwitch) {
						LookupSwitch ls = (LookupSwitch)op;
					} else if(op instanceof MultiANewArray) {
						MultiANewArray mana = (MultiANewArray)op;
						out.println(", operand: " + "#" +mana.getIndexByte());
					} else if(op instanceof NewArray) {
						NewArray na = (NewArray)op;
						out.println(", operand: " + na.getType());
					} else if(op instanceof Sipush) {
						Sipush si = (Sipush)op;
						out.println(", operand: " + si.getShort());
					} else if(op instanceof TableSwitch) {
						TableSwitch ts = (TableSwitch)op;
					} else if(op instanceof Wide) {
						Wide wide = (Wide)op;
						out.println(", operand: " + "#" +wide.getIndexByte() +","+ wide.getConst());
					} else if(op instanceof CpRefOpcode) {
						CpRefOpcode cp = (CpRefOpcode)op;
						out.println(", operand: #" + cp.getIndexByte());
					} else {
						out.println("");
					}
				}
				code.getExceptionTable();
				for(AttributeInfo ai : code.getAttr().getAll()) {
					printAttributeInfo(ai);
				}
				break;
			case ConstantValue:
				ConstantValue cv = (ConstantValue)info;
				int index = cv.getConstantValueIndex();
				switch(index) {
					case C_LONG:
//						LongInfo longInfo = (LongInfo)pool.
					case C_FLOAT:
					case C_DOUBLE:
					case C_STRING:
					case C_INTEGER:
					default:
				}
				break;
			case Deprecated:
				// do nothing.
				break;
			case EnclosingMethod:
				EnclosingMethod em = (EnclosingMethod)info;
				if(checkRange(em.classIndex()-1)) {
					out.println("\t" + getUtf8Value(pool.get(em.classIndex()-1)));
				}
				if(checkRange(em.methodIndex()-1)) {
					out.println("\t" + getUtf8Value(pool.get(em.methodIndex()-1)));
				}
				break;
			case Exceptions:
				Exceptions ex = (Exceptions)info;
				for(int i : ex.getExceptionIndexTable()) {
					if(checkRange(i-1)) {
						out.println("\t" + getUtf8Value(pool.get(i-1)).replace("/", "."));
					}
				}
				break;
			case InnerClasses:
				InnerClasses ic = (InnerClasses)info;
				for(InnerClasses.Classes c : ic.getClasses()) {
					int inner = c.getNumber("inner");
					int outer = c.getNumber("outer");
					int name = c.getNumber("inner_name");
					int accessFlag = c.getNumber("access_flag");
					if(checkRange(inner-1)) {
						out.println("\tinner_class: " + AccessFlags.get(accessFlag)
								+ getUtf8Value(pool.get(inner-1)));
					}
					if(checkRange(outer-1)) {
						out.println("\touter_class: " + getUtf8Value(pool.get(outer-1)));
					}
					if(checkRange(name-1)) {
						out.println("\t" + getUtf8Value(pool.get(name-1)));
					}
				}
				break;
			case LineNumberTable:
				LineNumberTable lnt = (LineNumberTable)info;
				for(LineNumberTable.LNTable t : lnt.getLineNumberTable()) {
					out.println("\tstart_pc: " + t.getStartPc()
								+ ", line_number: " + t.getLineNumber());
				}
				break;
			case LocalVariableTable:
				LocalVariableTable lvt = (LocalVariableTable)info;
				for(LocalVariable.LVTable t : lvt.getTable()) {
					out.println("\tstart_pc: " + t.getNumber("start_pc")
								+ ", length: " + t.getNumber("length") + "\t");
					out.println("\t" + getUtf8Value(pool.get(t.getNumber("name_index")-1))
								+ getUtf8Value(pool.get(t.getNumber("descriptor")-1)));
					out.println("\tindex: " + t.getNumber("index"));
				}
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable)info;
				for(LocalVariable.LVTable t : lvtt.getTable()) {
					out.println("\tstart_pc: " + t.getNumber("start_pc")
								+ ", length: " + t.getNumber("length"));
					out.println("\t" + getUtf8Value(pool.get(t.getNumber("name_index")-1))
								+ getUtf8Value(pool.get(t.getNumber("descriptor")-1)));
					out.println("\tindex: " + t.getNumber("index"));
				}
				break;
			case MethodParameters:
				MethodParameters mp = (MethodParameters)info;
				for(MethodParameters.Parameters p : mp.getParams()) {
					String flag = AccessFlags.get(p.getAccessFlag());
					out.println(flag + getUtf8Value(pool.get(p.getNameIndex()-1)));
				}
				break;
			case RuntimeInvisibleAnnotations:
				RuntimeInvisibleAnnotations ria = (RuntimeInvisibleAnnotations)info;
				for(Annotation a : ria.getAnnotations()) {
					printAnnotation(a);
				}
				break;
			case RuntimeInvisibleParameterAnnotations:
				RuntimeInvisibleParameterAnnotations ripa = (RuntimeInvisibleParameterAnnotations)info;
				for(ParameterAnnotations pa : ripa.getParamAnnotations()) {
					for(Annotation a : pa.getAnnotations()) {
						printAnnotation(a);
					}
				}
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations)info;
				for(TypeAnnotation pa : rita.getAnnotations()) {
					pa.getTargetInfo();
					pa.getTargetPath();
					for(ElementValuePair evp : pa.getElementValuePairs()) {
						printElementValuePair(evp);
					}
				}
				break;
			case RuntimeVisibleAnnotations:
				RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)info;
				for(Annotation a : rva.getAnnotations()) {
					printAnnotation(a);
				}
				break;
			case RuntimeVisibleParameterAnnotations:
				RuntimeVisibleParameterAnnotations rvpa = (RuntimeVisibleParameterAnnotations)info;
				for(ParameterAnnotations pa : rvpa.getParamAnnotations()) {
					for(Annotation a : pa.getAnnotations()) {
						printAnnotation(a);
					}
				}
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
				for(TypeAnnotation pa : rvta.getAnnotations()) {
					pa.getTargetInfo();
					pa.getTargetPath();
					for(ElementValuePair e : pa.getElementValuePairs()) {
						printElementValuePair(e);
					}
				}
				break;
			case Signature:
				Signature sig = (Signature)info;
				Utf8Info sigUtf = (Utf8Info)pool.get(sig.getSignatureIndex()-1);
				out.println("\t" + sigUtf.getValue());
				break;
			case SourceDebugExtension:
				SourceDebugExtension sde = (SourceDebugExtension)info;
				String de = new String(sde.getDebugExtension(), "UTF-8");
				out.println("\t" + de);
				break;
			case SourceFile:
				SourceFile sf = (SourceFile)info;
				out.println("\tsource file: " + getUtf8Value(pool.get(sf.getSourceFileIndex()-1)));
				break;
			case Synthetic:
				// do nothing.
				break;
			case StackMapTable:
				StackMapTable smt = (StackMapTable)info;
				break;
			default:
				System.out.println("unknow attribute type: " + info.getType().name());
				break;
		}
	}

	private void printElementValuePair(ElementValuePair e) {
		if(e == null) {
			return;
		}
		e.getElementNameIndex();
		printElementValue(e.getValue());
	}

	private void printElementValue(ElementValue e) {
		if(e == null) {
			return;
		}
		printAnnotation(e.getAnnotationValue());
		for(ElementValue ev : e.getArrayValue().getValues()) {
			printElementValue(ev);
		}
	}

	private void printAnnotation(Annotation annotation) {
		if(annotation == null) {
			return;
		}
		for(ElementValuePair evp : annotation.getElementValuePairs()) {
			printElementValuePair(evp);
		}
	}

	private boolean checkRange(int index) {
		return (0 <= index) && (index < pool.getSize());
	}

	private String getUtf8Value(ConstantInfo info) {
		if(info.getTag() == C_UTF8) {
			return ((Utf8Info)info).getValue();
		}
		switch(info.getTag()) {
			case C_INTEGER:
				return ""+((IntegerInfo)info).getBytes();
			case C_FLOAT:
				return ""+((FloatInfo)info).getBytes();
			case C_LONG:
				return ((LongInfo)info).getHighBytes() + "," + ((LongInfo)info).getLowBytes();
			case C_DOUBLE:
				return ((DoubleInfo)info).getHighBytes() + "," + ((DoubleInfo)info).getLowBytes();
			case C_CLASS:
				ClassInfo ci = (ClassInfo)info;
				return getUtf8Value(pool.get(ci.getNameIndex()-1)).replace("/", ".");
			case C_STRING:
				StringInfo si = (StringInfo)info;
				return getUtf8Value(pool.get(si.getStringIndex()-1));
			case C_FIELDREF:
				FieldrefInfo fi = (FieldrefInfo)info;
				return getUtf8Value(pool.get(fi.getClassIndex()-1)) + "."
					+ getUtf8Value(pool.get(fi.getNameAndTypeIndex()-1));
			case C_METHODREF:
				MethodrefInfo mi = (MethodrefInfo)info;
				return getUtf8Value(pool.get(mi.getClassIndex()-1)) + "."
					+ getUtf8Value(pool.get(mi.getNameAndTypeIndex()-1));
			case C_INTERFACE_METHODREF:
				InterfaceMethodrefInfo imi = (InterfaceMethodrefInfo)info;
				return getUtf8Value(pool.get(imi.getClassIndex()-1)) + "." 
					+ getUtf8Value(pool.get(imi.getNameAndTypeIndex()-1));
			case C_NAME_AND_TYPE:
				NameAndTypeInfo nati = (NameAndTypeInfo)info;
				return getUtf8Value(pool.get(nati.getNameIndex()-1))
					+ DescriptorParser.parse(getUtf8Value(pool.get(nati.getDescriptorIndex()-1)));
			case C_METHOD_HANDLE:
				MethodHandleInfo mhi = (MethodHandleInfo)info;
				return mhi.getRefKindValue() + ": "
					+ getUtf8Value(pool.get(mhi.getReferenceIndex()-1));
			case C_METHOD_TYPE:
				MethodTypeInfo mti = (MethodTypeInfo)info;
				return DescriptorParser.parse(getUtf8Value(pool.get(mti.getDescriptorIndex()-1)));
			case C_INVOKE_DYNAMIC:
				InvokeDynamicInfo idi = (InvokeDynamicInfo)info;
				return getUtf8Value(pool.get(idi.getNameAndTypeIndex()));
			default:
				System.out.println("invalid type: " + info);
				return " >>> unknown type <<<";
		}
	}

	private String getUtf8Value(MemberInfo info) {
		return getUtf8Value(pool.get(info.getNameIndex()-1))
			+ DescriptorParser.parse(getUtf8Value(pool.get(info.getDescriptorIndex()-1)));
	}
}