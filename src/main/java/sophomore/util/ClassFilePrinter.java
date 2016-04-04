package sophomore.util;

import java.io.IOException;
import java.io.PrintStream;

import sophomore.classfile.Attributes;
import sophomore.classfile.ConstantPool;
import sophomore.classfile.Fields;
import sophomore.classfile.Methods;
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
import sophomore.classfile.attributes.annotation.TargetInfo;
//import sophomore.classfile.attributes.annotation.TargetInfoType;
import sophomore.classfile.attributes.annotation.TypePath;
import sophomore.classfile.attributes.annotation.CatchTarget;
import sophomore.classfile.attributes.annotation.EmptyTarget;
import sophomore.classfile.attributes.annotation.LocalVarTarget;
import sophomore.classfile.attributes.annotation.MethodFormalParameterTarget;
import sophomore.classfile.attributes.annotation.OffsetTarget;
import sophomore.classfile.attributes.annotation.SuperTypeTarget;
import sophomore.classfile.attributes.annotation.ThrowsTarget;
import sophomore.classfile.attributes.annotation.TypeArgumentTarget;
import sophomore.classfile.attributes.annotation.TypeParameterTarget;
import sophomore.classfile.attributes.annotation.TypeParameterBoundTarget;
import sophomore.classfile.attributes.annotation.TypeAnnotation;
import sophomore.classfile.attributes.stackmap.AppendFrame;
import sophomore.classfile.attributes.stackmap.ChopFrame;
import sophomore.classfile.attributes.stackmap.FullFrame;
import sophomore.classfile.attributes.stackmap.ObjectVariableInfo;
import sophomore.classfile.attributes.stackmap.SameFrameExtended;
import sophomore.classfile.attributes.stackmap.SameLocals1StackItemFrame;
import sophomore.classfile.attributes.stackmap.SameLocals1StackItemFrameExtended;
import sophomore.classfile.attributes.stackmap.StackMapTable;
import sophomore.classfile.attributes.stackmap.StackMapFrame;
import sophomore.classfile.attributes.stackmap.UninitializedVariableInfo;
import sophomore.classfile.attributes.stackmap.VerificationTypeInfo;
import sophomore.classfile.bytecode.Bipush;
import sophomore.classfile.bytecode.BranchOpcode;
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
 * This class for debugging.
 * @author inagaki
 */
public class ClassFilePrinter {
	private PrintStream out = System.out;
	private ConstantPool pool;
	private String sep = System.getProperty("line.separator");
	public ClassFilePrinter(ConstantPool pool) {
		this.pool = pool;
	}

	/**
	 * 
	 * @param majorVersion
	 * @param minorVersion
	 */
	public void printNumber(int majorVersion, int minorVersion) {
		out.println("*** Major Version *** " + sep + majorVersion);
		out.println("*** Minor Version *** " + sep + minorVersion);
		out.println(sep);
	}

	/**
	 * 
	 */
	public void printConstantPool() {
		out.println(pool);
		out.println(sep);
	}

	/**
	 * 
	 * @param accessFlag
	 */
	public void printAccessFlag(int accessFlag) {
		out.println("*** Access Flag *** ");
		out.println(AccessFlags.get(accessFlag, "class"));
		out.println(sep);
	}

	/**
	 * 
	 * @param thisClass
	 */
	public void printThisClass(int thisClass) {
		out.println("*** This Class *** ");
		if(!checkRange(thisClass)) {
			out.println(sep);
			return;
		}
		out.println(getUtf8Value(pool.get(thisClass-1)));
		out.println(sep);
	}

	/**
	 * 
	 * @param superClass
	 */
	public void printSuperClass(int superClass) {
		out.println("*** Super Class *** ");
		if(!checkRange(superClass)) {
			out.println(sep);
			return;
		}
		out.println(getUtf8Value(pool.get(superClass-1)));
		out.println(sep);
	}

	/**
	 * 
	 * @param interfaces
	 */
	public void printInterface(int[] interfaces) {
		out.println("*** Interface *** ");
		if(interfaces.length == 0) {
			out.println(sep);
			return;
		}
		for(int i : interfaces) {
			out.println(getUtf8Value(pool.get(i-1)));
		}
		out.println(sep);
	}

	/**
	 * 
	 * @param fields
	 * @throws IOException 
	 */
	public void printFields(Fields fields) throws IOException {
		out.println("*** Fields *** ");
		if(fields.size() == 0) {
			out.println(sep);
			return;
		}

		for(int i = 0; i < fields.size(); i++) {
			MemberInfo field = fields.get(i);
			out.println("\t"+AccessFlags.get(field.getAccessFlags(), "field")
						+ getUtf8Value(field));
			Attributes attr = field.getAttr();
			for(int j = 0; j < attr.size(); j++) {
				printAttributeInfo(attr.get(j));
			}
		}
		out.println(sep);
	}

	/**
	 * 
	 * @param methods
	 * @throws IOException 
	 */
	public void printMethods(Methods methods) throws IOException {
		out.println("*** Methods *** ");
		if(methods.size() == 0) {
			out.println(sep);
			return;
		}
		for(int i = 0; i < methods.size(); i++) {
			MemberInfo method = methods.get(i);
			out.println("["+AccessFlags.get(method.getAccessFlags(), "method")
						+ getUtf8Value(method) + "]");
			Attributes attr = method.getAttr();
			for(int j = 0; j < attr.size(); j++) {
				printAttributeInfo(attr.get(i));
			}
		}
		out.println(sep);
	}

	/**
	 * 
	 * @param attr
	 * @throws IOException 
	 */
	public void printAttributes(Attributes attr) throws IOException {
		out.println("*** Attributes *** ");
		if(attr.size() == 0) {
			out.println(sep);
			return;
		}
		for(int i = 0; i < attr.size(); i++) {
			printAttributeInfo(attr.get(i));
		}
		out.println(sep);
	}

	/**
	 * 
	 * @param info
	 * @throws IOException 
	 */
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
				out.println("\tconstant_value"+getUtf8Value(pool.get(index-1)));
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
						out.println("\tinner_class: " + AccessFlags.get(accessFlag, "nested")
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
					out.println("\tpc    : " + t.getNumber("start_pc")
							+ "-" + (t.getNumber("start_pc")+t.getNumber("length")-1));
					out.println("\tname  : " + getUtf8Value(pool.get(t.getNumber("name_index")-1)));
					out.println("\tdesc  : "
							+ DescriptorParser.parse(getUtf8Value(pool.get(t.getNumber("descriptor")-1))));
					out.println("\tindex : " + t.getNumber("index"));
				}
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable)info;
				for(LocalVariable.LVTable t : lvtt.getTable()) {
					out.println("\tpc    : " + t.getNumber("start_pc")
							+ "-" + (t.getNumber("start_pc")+t.getNumber("length")-1));
					out.println("\tname  : " + getUtf8Value(pool.get(t.getNumber("name_index")-1)));
					out.println("\tdesc  : "
							+ DescriptorParser.parse(getUtf8Value(pool.get(t.getNumber("descriptor")-1))));
					out.println("\tindex : " + t.getNumber("index"));
				}
				break;
			case MethodParameters:
				MethodParameters mp = (MethodParameters)info;
				for(MethodParameters.Parameters p : mp.getParams()) {
					String flag = AccessFlags.get(p.getAccessFlag(), "method");
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
					printTargetInfo(pa.getTargetInfo());
					printTypePath(pa.getTargetPath());
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
					printTargetInfo(pa.getTargetInfo());
					printTypePath(pa.getTargetPath());
					for(ElementValuePair e : pa.getElementValuePairs()) {
						printElementValuePair(e);
					}
				}
				break;
			case Signature:
				Signature sig = (Signature)info;
				out.println("\t" + getUtf8Value(pool.get(sig.getSignatureIndex()-1)));
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
				for(StackMapFrame frame : smt.getEntries()) {
					printStackMapFrame(frame);
				}
				break;
			default:
				System.out.println("unknown attribute type: " + info.getType().name());
				break;
		}
	}

	/**
	 * 
	 * @param e 
	 */
	private void printElementValuePair(ElementValuePair e) {
		if(e == null) {
			return;
		}
		out.println("\t  element_name: "+getUtf8Value(pool.get(e.getElementNameIndex()-1)));
		printElementValue(e.getValue());
	}

	/**
	 * 
	 * @param e 
	 */
	private void printElementValue(ElementValue e) {
		if(e == null) {
			return;
		}
		switch((char)e.getTag()) {
			case 'B':
			case 'C':
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'Z':
			case 's':
				out.println("\t\t  const_value: " + getUtf8Value(pool.get(e.getConstValueIndex()-1)));
				break;
			case 'e':
				EnumConstValue ecv = e.getEnumConstValue();
				out.println("\t\t  type_name : "+getUtf8Value(pool.get(ecv.getTypeNameIndex()-1)));
				out.println("\t\t  const_name: "+getUtf8Value(pool.get(ecv.getConstNameIndex()-1)));
				break;
			case 'c':
				out.println("\t\t  type_name : "+getUtf8Value(pool.get(e.getClassInfoIndex()-1)));
				break;
			case '@':
				printAnnotation(e.getAnnotationValue());
				break;
			case '[':
				for(ElementValue ev : e.getArrayValue().getValues()) {
					printElementValue(ev);
				}
				break;
		}
	}

	/**
	 * 
	 * @param annotation 
	 */
	private void printAnnotation(Annotation annotation) {
		if(annotation == null) {
			return;
		}
		out.println("\t\ttype_name : "+
					DescriptorParser.parse(getUtf8Value(pool.get(annotation.getTypeIndex()-1))));
		for(ElementValuePair evp : annotation.getElementValuePairs()) {
			printElementValuePair(evp);
		}
	}

	/**
	 * 
	 * @param frame 
	 */
	private void printStackMapFrame(StackMapFrame frame) {
		out.println("\t  stack_map_frame_type: " + frame.getFrameType());
		switch(frame.getFrameType()) {
			case SameFrame: break;
			case SameLocals1StackItemFrame:
				SameLocals1StackItemFrame slsif = (SameLocals1StackItemFrame)frame;
				printVerificationTypeInfo(slsif.getStack());
				break;
			case SameLocals1StackItemFrameExtended:
				SameLocals1StackItemFrameExtended slsife = (SameLocals1StackItemFrameExtended)frame;
				printVerificationTypeInfo(slsife.getStack());
				break;
			case ChopFrame:
				ChopFrame cFrame = (ChopFrame)frame;
				out.println("\t  offset_delta: " + cFrame.getOffset());
				break;
			case SameFrameExtended:
				SameFrameExtended sfe = (SameFrameExtended)frame;
				sfe.getOffset();
				break;
			case AppendFrame:
				AppendFrame af = (AppendFrame)frame;
				out.println("\t  offset_delta: " + af.getOffset());
				for(VerificationTypeInfo info : af.getLocals()) {
					printVerificationTypeInfo(info);
				}
				break;
			case FullFrame:
				FullFrame ff = (FullFrame)frame;
				out.println("\t  offset_delta: " + ff.getOffset());
				for(VerificationTypeInfo info : ff.getLocals()) {
					printVerificationTypeInfo(info);
				}
				for(VerificationTypeInfo info : ff.getStack()) {
					printVerificationTypeInfo(info);
				}
				break;
			default:
				System.out.println("unknown stack-map-frame type: " + frame.getFrameType());
				break;
		}
	}

	/**
	 * 
	 * @param info 
	 */
	private void printVerificationTypeInfo(VerificationTypeInfo info) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t\tverification_info_type: ").append(info.getType()).append(sep);
		switch(info.getType()) {
			case TopVariableInfo:
			case IntegerVariableInfo:
			case FloatVariableInfo:
			case LongVariableInfo:
			case DoubleVariableInfo:
			case NullVariableInfo:
			case UninitializedThisVariableInfo: break;
			case ObjectVariableInfo:
				ObjectVariableInfo ovi = (ObjectVariableInfo)info;
				sb.append("\t\tcpool_index: ").append(ovi.getCPoolIndex()).append(sep);
				sb.append("\t\t\tvalue: ").append(getUtf8Value(pool.get(ovi.getCPoolIndex()-1))).append(sep);
				break;
			case UninitializedVariableInfo:
				UninitializedVariableInfo uvi = (UninitializedVariableInfo)info;
				sb.append("\t\toffset: ").append(uvi.getOffset()).append(sep);
				break;
			default:
				System.out.println("unknown varification-info type: " + info.getType());
				break;
		}
		out.print(sb.toString());
	}

	/**
	 * 
	 * @param info 
	 */
	private void printTargetInfo(TargetInfo info) {
		out.println("\t  target_info_type: " + info.getType());
		switch(info.getType()) {
			case TypeParameterTarget:
				TypeParameterTarget tpt = (TypeParameterTarget)info;
				out.println("\t\tindex: "+tpt.getIndex());
				break;
			case SuperTypeTarget:
				SuperTypeTarget stt = (SuperTypeTarget)info;
				out.println("\t\tindex: "+stt.getIndex());
				break;
			case TypeParameterBoundTarget:
				TypeParameterBoundTarget tpbt = (TypeParameterBoundTarget)info;
				out.println("\t\tbound_index         : "+tpbt.getBoundIndex());
				out.println("\t\ttype_parameter_index: "+tpbt.getTPI());
				break;
			case EmptyTarget:
				EmptyTarget et = (EmptyTarget)info;
				break;
			case MethodFormalParameterTarget:
				MethodFormalParameterTarget mfpt = (MethodFormalParameterTarget)info;
				out.println("\t\tindex: "+mfpt.getIndex());
				break;
			case ThrowsTarget:
				ThrowsTarget tt = (ThrowsTarget)info;
				out.println("\t\tindex: "+tt.getIndex());
				break;
			case LocalVarTarget:
				LocalVarTarget lvt = (LocalVarTarget)info;
				for(LocalVarTarget.LVTTable t : lvt.getTable()) {
					out.println("\t\tindex: " + t.getIndex());
					out.println("\t\tpc   : " + t.getStartPc()
								+ (t.getStartPc()+t.getLen()-1));
				}
				break;
			case CatchTarget:
				CatchTarget ct = (CatchTarget)info;
				out.println("\t\tindex: "+ct.getIndex());
				break;
			case OffsetTarget:
				OffsetTarget ot = (OffsetTarget) info;
				out.println("\t\toffset: "+ot.getOffset());
				break;
			case TypeArgumentTarget:
				TypeArgumentTarget tat = (TypeArgumentTarget)info;
				out.println("\t\tindex : "+tat.getIndex());
				out.println("\t\toffset: "+tat.getOffset());
				break;
			default:
				System.out.println("unknown target-info type: " + info.getType());
				break;
		}
	}

	/**
	 * 
	 * @param path 
	 */
	private void printTypePath(TypePath path) {
		out.print("\t  type_path_type: ");
		for(int i = 0; i < path.getArgIndex().length; i++) {
			int pk = path.getPathKind()[i];
			int ai = path.getArgIndex()[i];
			switch(pk) {
				case 0:  out.println("Annotation is deeper in an array type."); break;
				case 1:  out.println("Annotation is deeper in a nested type."); break;
				case 2:  out.println("Annotation is on the bound of a "
									+ "wildcard type argument of a parameterized type."); break;
				case 3:  out.println("Annotation is on a type argument of a parameterized type."); break;
				default: out.println("unknown type_path_kind: " + pk); break;
			}
			out.println("\t  type_arg_index: " + ai);
		}
	}

	/**
	 * 
	 * @param index
	 * @return 
	 */
	private boolean checkRange(int index) {
		return (0 <= index) && (index < pool.size());
	}

	/**
	 * 
	 * @param info
	 * @return 
	 */
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

	/**
	 * 
	 * @param info
	 * @return 
	 */
	private String getUtf8Value(MemberInfo info) {
		return getUtf8Value(pool.get(info.getNameIndex()-1))
			+ DescriptorParser.parse(getUtf8Value(pool.get(info.getDescriptorIndex()-1)));
	}
}