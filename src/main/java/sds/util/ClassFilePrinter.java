package sds.util;

import java.io.IOException;
import java.io.PrintStream;

import sds.classfile.Attributes;
import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.Methods;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;
import sds.classfile.attributes.BootstrapMethods;
import sds.classfile.attributes.Code;
import sds.classfile.attributes.ConstantValue;
import sds.classfile.attributes.EnclosingMethod;
import sds.classfile.attributes.Exceptions;
import sds.classfile.attributes.InnerClasses;
import sds.classfile.attributes.LineNumberTable;
import sds.classfile.attributes.LocalVariable;
import sds.classfile.attributes.LocalVariableTable;
import sds.classfile.attributes.LocalVariableTypeTable;
import sds.classfile.attributes.MethodParameters;
import sds.classfile.attributes.Signature;
import sds.classfile.attributes.SourceDebugExtension;
import sds.classfile.attributes.SourceFile;
import sds.classfile.attributes.annotation.Annotation;
import sds.classfile.attributes.annotation.AnnotationDefault;
import sds.classfile.attributes.annotation.ElementValue;
import sds.classfile.attributes.annotation.ElementValuePair;
import sds.classfile.attributes.annotation.EnumConstValue;
import sds.classfile.attributes.annotation.ParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleTypeAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleTypeAnnotations;
import sds.classfile.attributes.annotation.TargetInfo;
import sds.classfile.attributes.annotation.TypePath;
import sds.classfile.attributes.annotation.CatchTarget;
import sds.classfile.attributes.annotation.EmptyTarget;
import sds.classfile.attributes.annotation.LocalVarTarget;
import sds.classfile.attributes.annotation.MethodFormalParameterTarget;
import sds.classfile.attributes.annotation.OffsetTarget;
import sds.classfile.attributes.annotation.SuperTypeTarget;
import sds.classfile.attributes.annotation.ThrowsTarget;
import sds.classfile.attributes.annotation.TypeArgumentTarget;
import sds.classfile.attributes.annotation.TypeParameterTarget;
import sds.classfile.attributes.annotation.TypeParameterBoundTarget;
import sds.classfile.attributes.annotation.TypeAnnotation;
import sds.classfile.attributes.stackmap.AppendFrame;
import sds.classfile.attributes.stackmap.ChopFrame;
import sds.classfile.attributes.stackmap.FullFrame;
import sds.classfile.attributes.stackmap.ObjectVariableInfo;
import sds.classfile.attributes.stackmap.SameFrameExtended;
import sds.classfile.attributes.stackmap.SameLocals1StackItemFrame;
import sds.classfile.attributes.stackmap.SameLocals1StackItemFrameExtended;
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.classfile.attributes.stackmap.StackMapFrame;
import sds.classfile.attributes.stackmap.UninitializedVariableInfo;
import sds.classfile.attributes.stackmap.VerificationTypeInfo;
import sds.classfile.bytecode.PushOpcode;
import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.CpRefOpcode;
import sds.classfile.bytecode.Iinc;
import sds.classfile.bytecode.IndexOpcode;
import sds.classfile.bytecode.InvokeDynamic;
import sds.classfile.bytecode.InvokeInterface;
import sds.classfile.bytecode.LookupSwitch;
import sds.classfile.bytecode.MultiANewArray;
import sds.classfile.bytecode.NewArray;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.TableSwitch;
import sds.classfile.bytecode.Wide;

import static sds.util.AccessFlags.get;
import static sds.util.DescriptorParser.parse;
import static sds.util.Utf8ValueExtractor.extract;

/**
 * This class is for debugging.
 * @author inagaki
 */
public class ClassFilePrinter {
	private PrintStream out = System.out;
	private ConstantPool pool;
	private String sep = System.getProperty("line.separator");

	/**
	 * constructor.
	 * @param pool constant-pool
	 */
	public ClassFilePrinter(ConstantPool pool) {
		this.pool = pool;
	}

	public ClassFilePrinter() {}

	/**
	 * prints major and minor version.
	 * @param magicNum magic number
	 * @param majorVersion major version
	 * @param minorVersion minor version
	 */
	public void printNumber(int magicNum, int majorVersion, int minorVersion) {
		out.println("*** Magic Number  *** " + sep + Integer.toHexString(magicNum) + sep);
		out.println("*** Major Version *** " + sep + majorVersion + sep);
		out.println("*** Minor Version *** " + sep + minorVersion + sep);
	}

	/**
	 * prints constant-pool.
	 */
	public void printConstantPool() {
		out.println(pool);
	}

	/**
	 * prints access flag of this class.
	 * @param accessFlag
	 */
	public void printAccessFlag(int accessFlag) {
		out.println("*** Access Flag *** ");
		out.println(AccessFlags.get(accessFlag, "class"));
		out.print(sep);
	}

	/**
	 * prints this class.
	 * @param thisClass
	 */
	public void printThisClass(int thisClass) {
		out.println("*** This Class *** ");
		if(!checkRange(thisClass)) {
			out.print(sep);
			return;
		}
		out.println(extract(pool.get(thisClass-1), pool));
		out.print(sep);
	}

	/**
	 * prints super class.
	 * @param superClass
	 */
	public void printSuperClass(int superClass) {
		out.println("*** Super Class *** ");
		if(!checkRange(superClass)) {
			out.print("has no super class." + sep);
			return;
		}
		out.println(extract(pool.get(superClass-1), pool));
		out.print(sep);
	}

	/**
	 * prints interfaces.
	 * @param interfaces
	 */
	public void printInterface(int[] interfaces) {
		out.println("*** Interfaces *** ");
		if(interfaces.length == 0) {
			out.println("has no interfaces." + sep);
			return;
		}
		for(int i : interfaces) {
			out.println(extract(pool.get(i-1), pool));
		}
		out.print(sep);
	}

	/**
	 * prints fields of this class.
	 * @param fields field
	 * @throws IOException
	 */
	public void printFields(Fields fields) throws IOException {
		out.println("*** Fields *** ");
		if(fields.size() == 0) {
			out.println("has no fields." + sep);
			return;
		}

		for(int i = 0; i < fields.size(); i++) {
			MemberInfo field = fields.get(i);
			out.println(i+1 + ". " + get(field.getAccessFlags(), "field") + extract(field, pool));
			Attributes attr = field.getAttr();
			for(int j = 0; j < attr.size(); j++) {
				AttributeInfo info = attr.get(j);
				if(info.getType() != AttributeType.Signature) {
					printAttributeInfo(attr.get(j));
				} else {
					out.println("  " + info.getType().toString());
					Signature sig = (Signature)info;
					out.println("     " + extract(pool.get(sig.getSignatureIndex()-1), pool));
				}
			}
			out.print(sep);
		}
	}

	/**
	 * prints methods of this class.
	 * @param methods methods
	 * @throws IOException
	 */
	public void printMethods(Methods methods) throws IOException {
		out.println("*** Methods *** ");
		if(methods.size() == 0) {
			out.print("has no methods." + sep);
			return;
		}
		for(int i = 0; i < methods.size(); i++) {
			MemberInfo method = methods.get(i);
			out.println(i+1 + ". " + get(method.getAccessFlags(), "method") + extract(method, pool));
			Attributes attr = method.getAttr();
			for(int j = 0; j < attr.size(); j++) {
				printAttributeInfo(attr.get(j));
			}
			out.print(sep);
		}
	}

	/**
	 * prints attributes of this class.
	 * @param attr
	 * @throws IOException
	 */
	public void printAttributes(Attributes attr) throws IOException {
		out.println("*** Attributes *** ");
		if(attr.size() == 0) {
			out.print("has no attributes." + sep);
			return;
		}
		for(int i = 0; i < attr.size(); i++) {
			printAttributeInfo(attr.get(i));
		}
		out.println(sep);
	}

	/**
	 * print an attribute.
	 * @param info
	 * @throws IOException
	 */
	public void printAttributeInfo(AttributeInfo info) throws IOException {
		out.println("  " + info.getType().toString());
		switch(info.getType()) {
			case AnnotationDefault:
				AnnotationDefault ad = (AnnotationDefault)info;
				printElementValue(ad.getDefaultValue());
				break;
			case BootstrapMethods:
				BootstrapMethods bsm = (BootstrapMethods)info;
				for(BootstrapMethods.BSM b : bsm.getBSM()) {
					out.println("     bsm ref: " + extract(pool.get(b.getBSMRef()-1), pool));
					for(int i : b.getBSMArgs()) {
						out.println("     bsm args: " + extract(pool.get(i-1), pool));
					}
				}
				break;
			case Code:
				Code code = (Code)info;
				out.print("     max_stack: " + code.getMaxStack());
				out.println(", max_locals: " + code.maxLocals());
				for(OpcodeInfo op : code.getCode().getAll()) {
					out.print("     "+op.getPc()+" - "+op.getOpcodeType());
					if(op instanceof PushOpcode) {
						PushOpcode push = (PushOpcode)op;
						out.println("  " + push.getValue());
					} else if(op instanceof BranchOpcode) {
						BranchOpcode branch = (BranchOpcode)op;
						out.println("  " + branch.getBranch());
					} else if(op instanceof Iinc) {
						Iinc iinc = (Iinc)op;
						out.println("  " + iinc.getIndex() +","+iinc.getConst());
					} else if(op instanceof IndexOpcode) {
						IndexOpcode io = (IndexOpcode)op;
						out.println("  " + io.getIndex());
					} else if(op instanceof InvokeDynamic) {
						InvokeDynamic id = (InvokeDynamic)op;
						out.println("  " + "#" + id.getIndexByte());
					} else if(op instanceof InvokeInterface) {
						InvokeInterface ii = (InvokeInterface)op;
						out.println("  " + "#" + ii.getIndexByte() + ","+ii.getCount());
					} else if(op instanceof LookupSwitch) {
						LookupSwitch ls = (LookupSwitch)op;
						int[] match = ls.getMatch();
						int[] offset = ls.getOffset();
						out.print(sep);
						for(int i = 0; i < match.length; i++) {
							out.println("            " + match[i] + ", " + (offset[i]+ls.getPc()));
						}
						out.println("          default: " + (ls.getDefault()+ls.getPc()));
					} else if(op instanceof MultiANewArray) {
						MultiANewArray mana = (MultiANewArray)op;
						out.println("  " + "#" +mana.getIndexByte());
					} else if(op instanceof NewArray) {
						NewArray na = (NewArray)op;
						out.println("  " + na.getType());
					} else if(op instanceof TableSwitch) {
						TableSwitch ts = (TableSwitch)op;
						int[] jump = ts.getJumpOffsets();
						out.print(sep);
						for(int i = 0; i < jump.length; i++) {
							out.println("            " + (jump[i]+ts.getPc()));
						}
						out.println("          default: " + (ts.getDefault()+ts.getPc()));
					} else if(op instanceof Wide) {
						Wide wide = (Wide)op;
						out.println("  " + "#" +wide.getIndexByte() +","+ wide.getConst());
					} else if(op instanceof CpRefOpcode) {
						CpRefOpcode cp = (CpRefOpcode)op;
						out.println("  #" + cp.getIndexByte());
					} else {
						out.println("");
					}
				}
				if(code.getExceptionTable().length > 0) {
					out.println("  ExceptionTable");
				}
				int codeIndex = 1;
				for(Code.ExceptionTable t : code.getExceptionTable()) {
					out.print("     " + codeIndex + ". pc: " + t.getNumber("start_pc")
								+ "-" + t.getNumber("end_pc"));
					out.print(", handler: " + t.getNumber("handler_pc"));
					if(checkRange(t.getNumber("catch_type")-1))
						out.println(", catch_type: " + extract(pool.get(t.getNumber("catch_type")-1), pool));
					else
						out.print(sep);
					codeIndex++;
				}
				for(AttributeInfo ai : code.getAttr().getAll()) {
					printAttributeInfo(ai);
				}
				break;
			case ConstantValue:
				ConstantValue cv = (ConstantValue)info;
				int index = cv.getConstantValueIndex();
				out.println("     " + extract(pool.get(index-1), pool));
				break;
			case Deprecated:
				// do nothing.
				break;
			case EnclosingMethod:
				EnclosingMethod em = (EnclosingMethod)info;
				if(checkRange(em.classIndex()-1)) {
					out.println("     " + extract(pool.get(em.classIndex()-1), pool));
				}
				if(checkRange(em.methodIndex()-1)) {
					out.println("     " + extract(pool.get(em.methodIndex()-1), pool));
				}
				break;
			case Exceptions:
				Exceptions ex = (Exceptions)info;
				int excepIndex = 1;
				for(int i : ex.getExceptionIndexTable()) {
					if(checkRange(i-1)) {
						out.println("     " + excepIndex + ". "
								+ extract(pool.get(i-1), pool).replace("/", "."));
					}
					excepIndex++;
				}
				break;
			case InnerClasses:
				InnerClasses ic = (InnerClasses)info;
				int classIndex = 1;
				for(InnerClasses.Classes c : ic.getClasses()) {
					int inner = c.getNumber("inner");
					int accessFlag = c.getNumber("access_flag");
					if(checkRange(inner-1)) {
						out.println("     " + classIndex + ". " + get(accessFlag, "nested")
								+ extract(pool.get(inner-1), pool));
					}
					classIndex++;
				}
				break;
			case LineNumberTable:
				LineNumberTable lnt = (LineNumberTable)info;
				int lineIndex = 1;
				for(LineNumberTable.LNTable t : lnt.getLineNumberTable()) {
					out.println("     " + lineIndex + ". start_pc: " + t.getStartPc()
							+ ", line_number: " + t.getLineNumber());
					lineIndex++;
				}
				break;
			case LocalVariableTable:
				LocalVariableTable lvt = (LocalVariableTable)info;
				int localIndex = 1;
				for(LocalVariable.LVTable t : lvt.getTable()) {
					out.print("     " + localIndex + ". pc: " + t.getNumber("start_pc")
							+ "-" + (t.getNumber("start_pc")+t.getNumber("length")-1));
					out.print(", name: " + extract(pool.get(t.getNumber("name_index")-1), pool));
					out.print(", index : " + t.getNumber("index"));
					out.println(", desc: " + parse(extract(pool.get(t.getNumber("descriptor")-1), pool)));
					localIndex++;
				}
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable)info;
				int varIndex = 1;
				for(LocalVariable.LVTable t : lvtt.getTable()) {
					out.print("     "+ varIndex + ". pc: " + t.getNumber("start_pc")
							+ "-" + (t.getNumber("start_pc")+t.getNumber("length")-1));
					out.print(", name: " + extract(pool.get(t.getNumber("name_index")-1), pool));
					out.println(", index: " + t.getNumber("index"));
					out.println("     desc: " + parse(extract(pool.get(t.getNumber("descriptor")-1), pool)));
					varIndex++;
				}
				break;
			case MethodParameters:
				MethodParameters mp = (MethodParameters)info;
				int methodIndex = 1;
				for(MethodParameters.Parameters p : mp.getParams()) {
					String flag = get(p.getAccessFlag(), "method");
					out.println("   " + methodIndex + ". " + flag
							+ extract(pool.get(p.getNameIndex()-1), pool));
					methodIndex++;
				}
				break;
			case RuntimeInvisibleAnnotations:
				RuntimeInvisibleAnnotations ria = (RuntimeInvisibleAnnotations)info;
				int riaIndex = 1;
				for(Annotation a : ria.getAnnotations()) {
					out.println("     " + riaIndex + ".");
					printAnnotation(a);
					riaIndex++;
				}
				break;
			case RuntimeInvisibleParameterAnnotations:
				RuntimeInvisibleParameterAnnotations ripa = (RuntimeInvisibleParameterAnnotations)info;
				int ripaIndex = 1;
				for(ParameterAnnotations pa : ripa.getParamAnnotations()) {
					out.println("     " + ripaIndex + ".");
					for(Annotation a : pa.getAnnotations()) {
						printAnnotation(a);
					}
					ripaIndex++;
				}
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations)info;
				int ritaIndex = 1;
				for(TypeAnnotation pa : rita.getAnnotations()) {
					out.println("     " + ritaIndex + ".");
					printTargetInfo(pa.getTargetInfo());
					printTypePath(pa.getTargetPath());
					for(ElementValuePair evp : pa.getElementValuePairs()) {
						printElementValuePair(evp);
					}
					ritaIndex++;
				}
				break;
			case RuntimeVisibleAnnotations:
				RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)info;
				int rvaIndex = 1;
				for(Annotation a : rva.getAnnotations()) {
					out.println("     " + rvaIndex + ".");
					printAnnotation(a);
					rvaIndex++;
				}
				break;
			case RuntimeVisibleParameterAnnotations:
				RuntimeVisibleParameterAnnotations rvpa = (RuntimeVisibleParameterAnnotations)info;
				int rvpaIndex = 1;
				for(ParameterAnnotations pa : rvpa.getParamAnnotations()) {
					out.println("     " + rvpaIndex + ".");
					for(Annotation a : pa.getAnnotations()) {
						printAnnotation(a);
					}
					rvpaIndex++;
				}
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
				int rvtaIndex = 1;
				for(TypeAnnotation pa : rvta.getAnnotations()) {
					out.println("     " + rvtaIndex + ".");
					printTargetInfo(pa.getTargetInfo());
					printTypePath(pa.getTargetPath());
					for(ElementValuePair e : pa.getElementValuePairs()) {
						printElementValuePair(e);
					}
					rvtaIndex++;
				}
				break;
			case Signature:
				Signature sig = (Signature)info;
				String desc = extract(pool.get(sig.getSignatureIndex()-1), pool);
				String genericsType = parse(desc.substring(0, desc.lastIndexOf(">") + 1), true);
				String returnType = parse(desc.substring(desc.lastIndexOf(">") + 1));
				out.println("     " + genericsType + returnType);
				break;
			case SourceDebugExtension:
				SourceDebugExtension sde = (SourceDebugExtension)info;
				int sdeIndex = 1;
				for(int i : sde.getDebugExtension()) {
					out.println("     " + sdeIndex + ". " + extract(pool.get(i-1), pool));
					sdeIndex++;
				}
				break;
			case SourceFile:
				SourceFile sf = (SourceFile)info;
				out.println("     " + extract(pool.get(sf.getSourceFileIndex()-1), pool));
				break;
			case Synthetic:
				// do nothing.
				break;
			case StackMapTable:
				StackMapTable smt = (StackMapTable)info;
				int stackIndex = 1;
				for(StackMapFrame frame : smt.getEntries()) {
					printStackMapFrame(stackIndex, frame);
					stackIndex++;
				}
				break;
			default:
				System.out.println("unknown attribute type: " + info.getType().name());
				break;
		}
	}

	private void printElementValuePair(ElementValuePair e) {
		out.println("       element_name: " + extract(pool.get(e.getElementNameIndex()-1), pool));
		printElementValue(e.getValue());
	}

	private void printElementValue(ElementValue e) {
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
				out.println("       const_value: " + extract(pool.get(e.getConstValueIndex()-1), pool));
				break;
			case 'e':
				EnumConstValue ecv = e.getEnumConstValue();
				out.println("       type_name : " + extract(pool.get(ecv.getTypeNameIndex()-1), pool));
				out.println("       const_name: " + extract(pool.get(ecv.getConstNameIndex()-1), pool));
				break;
			case 'c':
				out.println("       type_name : " + extract(pool.get(e.getClassInfoIndex()-1), pool));
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

	private void printAnnotation(Annotation annotation) {
		out.println("       type_name : " + parse(extract(pool.get(annotation.getTypeIndex()-1), pool)));
		for(ElementValuePair evp : annotation.getElementValuePairs()) {
			printElementValuePair(evp);
		}
	}

	private void printStackMapFrame(int index, StackMapFrame frame) {
		out.println("     " + index + ". " + frame.getFrameType());
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
				out.println("       offset_delta: " + cFrame.getOffset());
				break;
			case SameFrameExtended:
				SameFrameExtended sfe = (SameFrameExtended)frame;
				sfe.getOffset();
				break;
			case AppendFrame:
				AppendFrame af = (AppendFrame)frame;
				out.println("       offset_delta: " + af.getOffset());
				for(VerificationTypeInfo info : af.getLocals()) {
					printVerificationTypeInfo(info);
				}
				break;
			case FullFrame:
				FullFrame ff = (FullFrame)frame;
				out.println("       offset_delta: " + ff.getOffset());
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

	private void printVerificationTypeInfo(VerificationTypeInfo info) {
		StringBuilder sb = new StringBuilder();
		sb.append("       ").append(info.getType()).append(sep);
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
				sb.append("            cpool_index: ").append(ovi.getCPoolIndex());
				sb.append(", value: ")
					.append(extract(pool.get(ovi.getCPoolIndex()-1), pool))
					.append(sep);
				break;
			case UninitializedVariableInfo:
				UninitializedVariableInfo uvi = (UninitializedVariableInfo)info;
				sb.append("            offset: ").append(uvi.getOffset()).append(sep);
				break;
			default:
				System.out.println("unknown varification-info type: " + info.getType());
				break;
		}
		out.print(sb.toString());
	}

	private void printTargetInfo(TargetInfo info) {
		out.println("     " + info.getType());
		switch(info.getType()) {
			case TypeParameterTarget:
				TypeParameterTarget tpt = (TypeParameterTarget)info;
				out.println("       index: "+tpt.getIndex());
				break;
			case SuperTypeTarget:
				SuperTypeTarget stt = (SuperTypeTarget)info;
				out.println("       index: "+stt.getIndex());
				break;
			case TypeParameterBoundTarget:
				TypeParameterBoundTarget tpbt = (TypeParameterBoundTarget)info;
				out.print("       bound_index: "+tpbt.getBoundIndex());
				out.println(", type_parameter_index: "+tpbt.getTPI());
				break;
			case EmptyTarget:
				EmptyTarget et = (EmptyTarget)info;
				break;
			case MethodFormalParameterTarget:
				MethodFormalParameterTarget mfpt = (MethodFormalParameterTarget)info;
				out.println("       index: "+mfpt.getIndex());
				break;
			case ThrowsTarget:
				ThrowsTarget tt = (ThrowsTarget)info;
				out.println("       index: "+tt.getIndex());
				break;
			case LocalVarTarget:
				LocalVarTarget lvt = (LocalVarTarget)info;
				for(LocalVarTarget.LVTTable t : lvt.getTable()) {
					out.print("     index: " + t.getIndex());
					out.println(", pc: " + t.getStartPc() + "-" + (t.getStartPc()+t.getLen()-1));
				}
				break;
			case CatchTarget:
				CatchTarget ct = (CatchTarget)info;
				out.println("       index: "+ct.getIndex());
				break;
			case OffsetTarget:
				OffsetTarget ot = (OffsetTarget) info;
				out.println("       offset: "+ot.getOffset());
				break;
			case TypeArgumentTarget:
				TypeArgumentTarget tat = (TypeArgumentTarget)info;
				out.print("          index: "+tat.getIndex());
				out.println(", offset: "+tat.getOffset());
				break;
			default:
				System.out.println("unknown target-info type: " + info.getType());
				break;
		}
	}

	private void printTypePath(TypePath path) {
		for(int i = 0; i < path.getArgIndex().length; i++) {
			out.print("       type_path_type: ");
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
			out.println("       type_arg_index: " + ai);
		}
	}

	private boolean checkRange(int index) {
		return (0 <= index) && (index < pool.size());
	}
}