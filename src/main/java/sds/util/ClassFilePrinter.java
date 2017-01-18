package sds.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import sds.classfile.attributes.annotation.AnnotationDefault;
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
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;

import static sds.util.AnnotationParser.parseAnnotation;
import static sds.util.DescriptorParser.parse;
import static sds.util.Utf8ValueExtractor.extract;

/**
 * This class is for debugging classfile.
 * @author inagaki
 */
public class ClassFilePrinter extends Printer {
	private ConstantPool pool;
	private String sep = System.getProperty("line.separator");
	private Opcodes opcodes;

	/**
	 * constructor.
	 * @param pool constant-pool
	 */
	public ClassFilePrinter(ConstantPool pool) {
		this.pool = pool;
	}

	public ClassFilePrinter() {
	}

	/**
	 * prints major and minor version.
	 * @param magicNum magic number
	 * @param majorVersion major version
	 * @param minorVersion minor version
	 */
	public void printNumber(int magicNum, int majorVersion, int minorVersion) {
		println("*** Magic Number  *** " + sep + Integer.toHexString(magicNum) + sep);
		println("*** Major Version *** " + sep + majorVersion + sep);
		println("*** Minor Version *** " + sep + minorVersion + sep);
	}

	/**
	 * prints constant-pool.
	 */
	public void printConstantPool() {
		println(pool);
	}

	/**
	 * prints access flag of this class.
	 * @param accessFlag
	 */
	public void printAccessFlag(int accessFlag) {
		println("*** Access Flag *** ");
		println(AccessFlags.get(accessFlag, "class"));
		print(sep);
	}

	/**
	 * prints this class.
	 * @param thisClass
	 */
	public void printThisClass(int thisClass) {
		println("*** This Class *** ");
		if(!checkRange(thisClass)) {
			print(sep);
			return;
		}
		println(extract(pool.get(thisClass - 1), pool));
		print(sep);
	}

	/**
	 * prints super class.
	 * @param superClass
	 */
	public void printSuperClass(int superClass) {
		println("*** Super Class *** ");
		if(!checkRange(superClass)) {
			print("has no super class." + sep);
			return;
		}
		println(extract(pool.get(superClass - 1), pool));
		print(sep);
	}

	/**
	 * prints interfaces.
	 * @param interfaces
	 */
	public void printInterface(int[] interfaces) {
		println("*** Interfaces *** ");
		if(interfaces.length == 0) {
			println("has no interfaces." + sep);
			return;
		}
		for(int i : interfaces) {
			println(extract(pool.get(i - 1), pool));
		}
		print(sep);
	}

	/**
	 * prints fields of this class.
	 * @param fields field
	 * @throws IOException
	 */
	public void printFields(Fields fields) throws Exception {
		println("*** Fields *** ");
		if(fields.size() == 0) {
			println("has no fields." + sep);
			return;
		}
		int i = 0;
		for(MemberInfo f : fields) {
			println(i + 1 + ". " + f.getAccessFlags() + f.getDescriptor() + " " + f.getName());
			for(AttributeInfo a : f.getAttr()) {
				if(a.getType() != AttributeType.Signature) {
					printAttributeInfo(a);
					continue;
				}
				println("  " + a.getType().toString());
				Signature sig = (Signature)a;
				println("     " + parse(sig.getSignature()));
			}
			print(sep);
			i++;
		}
	}

	/**
	 * prints methods of this class.
	 * @param methods methods
	 * @throws IOException
	 */
	public void printMethods(Methods methods) throws Exception {
		println("*** Methods *** ");
		if(methods.size() == 0) {
			print("has no methods." + sep);
			return;
		}
		int i = 0;
		for(MemberInfo m : methods) {
			println(i + 1 + ". " + m.getAccessFlags() + m.getDescriptor() + " " + m.getName());
			for(AttributeInfo a : m.getAttr()) {
				printAttributeInfo(a);
			}
			print(sep);
			i++;
		}
	}

	/**
	 * prints attributes of this class.
	 * @param attr
	 * @throws IOException
	 */
	public void printAttributes(Attributes attr) throws Exception {
		println("*** Attributes *** ");
		if(attr.size() == 0) {
			print("has no attributes." + sep);
			return;
		}
		for(AttributeInfo a : attr) {
			printAttributeInfo(a);
		}
		println(sep);
	}

	/**
	 * print an attribute.
	 * @param info
	 * @throws Exception
	 */
	public void printAttributeInfo(AttributeInfo info) throws Exception {
		println("  " + info.getType().toString());
		switch(info.getType()) {
			case AnnotationDefault:
				AnnotationDefault ad = (AnnotationDefault)info;
				println("     " + ad.getDefaultValue());
				break;
			case BootstrapMethods:
				BootstrapMethods bsm = (BootstrapMethods)info;
				for(BootstrapMethods.BSM b : bsm.getBSM()) {
					println("     bsm ref: " + b.getBSMRef());
					for(String arg : b.getBSMArgs()) {
						println("     bsm args: " + arg);
					}
				}
				break;
			case Code:
				Code code = (Code)info;
				print("     max_stack: " + code.getMaxStack());
				println(", max_locals: " + code.maxLocals());
				this.opcodes = code.getCode();
				for(OpcodeInfo op : opcodes.getAll()) {
					println("     " + op.getPc() + " - " + op);
				}
				if(code.getExceptionTable().length > 0) {
					println("  ExceptionTable");
				}
				int codeIndex = 1;
				for(Code.ExceptionTable t : code.getExceptionTable()) {
					print("     " + codeIndex + ". pc: " + t.getNumber("start_pc")
							+ "-" + t.getNumber("end_pc"));
					print(", handler: " + t.getNumber("handler_pc"));
					println(", catch_type: " + t.getCatchType());
					codeIndex++;
				}
				for(AttributeInfo a : code.getAttr()) {
					printAttributeInfo(a);
				}
				break;
			case ConstantValue:
				ConstantValue cv = (ConstantValue)info;
				println("     " + cv.getConstantValue());
				break;
			case Deprecated:
				// do nothing.
				break;
			case EnclosingMethod:
				EnclosingMethod em = (EnclosingMethod)info;
				println("     " + em.getEncClass());
				println("     " + em.getEncMethod());
				break;
			case Exceptions:
				Exceptions ex = (Exceptions)info;
				int excepIndex = 1;
				for(String exception : ex.getExceptionTable()) {
					println("     " + excepIndex + ". " + exception);
					excepIndex++;
				}
				break;
			case InnerClasses:
				InnerClasses ic = (InnerClasses)info;
				int classIndex = 1;
				for(InnerClasses.Classes c : ic.getClasses()) {
					String inner = c.getNumber("inner");
					String accessFlag = c.getNumber("access_flag");
					println("     " + classIndex + ". " + accessFlag + inner);
					classIndex++;
				}
				break;
			case LineNumberTable:
				LineNumberTable lnt = (LineNumberTable)info;
				int lineIndex = 1;
				for(LineNumberTable.LNTable t : lnt.getLineNumberTable()) {
					println("     " + lineIndex + ". start_pc: " + t.getStartPc()
							+ ", line_number: " + t.getLineNumber());
					lineIndex++;
				}
				break;
			case LocalVariableTable:
				LocalVariableTable lvt = (LocalVariableTable)info;
				int localIndex = 1;
				for(LocalVariable.LVTable t : lvt.getTable()) {
					print("     " + localIndex + ". pc: " + t.getNumber("start_pc")
							+ "-" + (t.getNumber("start_pc") + t.getNumber("length") - 1));
					print(", name: " + t.getName());
					print(", index : " + t.getNumber("index"));
					println(", desc: " + t.getDesc());
					localIndex++;
				}
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable)info;
				int varIndex = 1;
				for(LocalVariable.LVTable t : lvtt.getTable()) {
					print("     " + varIndex + ". pc: " + t.getNumber("start_pc")
							+ "-" + (t.getNumber("start_pc") + t.getNumber("length") - 1));
					println(", name: " + t.getName() + ", index: " + t.getNumber("index"));
					println("     desc: " + t.getDesc());
					varIndex++;
				}
				break;
			case MethodParameters:
				MethodParameters mp = (MethodParameters)info;
				int methodIndex = 1;
				for(MethodParameters.Parameters p : mp.getParams()) {
					println("   " + methodIndex + ". " + p.getAccessFlag() + p.getName());
					methodIndex++;
				}
				break;
			case RuntimeInvisibleAnnotations:
				RuntimeInvisibleAnnotations ria = (RuntimeInvisibleAnnotations) info;
				int riaIndex = 1;
				for(String a : ria.getAnnotations()) {
					println("     " + riaIndex + "." + a);
					riaIndex++;
				}
				break;
			case RuntimeInvisibleParameterAnnotations:
				RuntimeInvisibleParameterAnnotations ripa = (RuntimeInvisibleParameterAnnotations)info;
				int ripaIndex = 1;
				for(ParameterAnnotations pa : ripa.getParamAnnotations()) {
					println("     " + ripaIndex + ".");
					for(String a : pa.getAnnotations()) {
						println("       " + a);
					}
					ripaIndex++;
				}
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations)info;
				int ritaIndex = 1;
				for(TypeAnnotation ta : rita.getTypes()) {
					println("     " + ritaIndex + "." + parseAnnotation(ta, new StringBuilder(), pool));
					printTargetInfo(ta.getTargetInfo());
					printTypePath(ta.getTargetPath());
					ritaIndex++;
				}
				break;
			case RuntimeVisibleAnnotations:
				RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)info;
				int rvaIndex = 1;
				for(String a : rva.getAnnotations()) {
					println("     " + rvaIndex + "." + a);
					rvaIndex++;
				}
				break;
			case RuntimeVisibleParameterAnnotations:
				RuntimeVisibleParameterAnnotations rvpa = (RuntimeVisibleParameterAnnotations)info;
				int rvpaIndex = 1;
				for(ParameterAnnotations pa : rvpa.getParamAnnotations()) {
					println("     " + rvpaIndex + ".");
					for(String a : pa.getAnnotations()) {
						println("       " + a);
					}
					rvpaIndex++;
				}
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
				int rvtaIndex = 1;
				for(TypeAnnotation pa : rvta.getTypes()) {
					println("     " + rvtaIndex + "." + parseAnnotation(pa, new StringBuilder(), pool));
					printTargetInfo(pa.getTargetInfo());
					printTypePath(pa.getTargetPath());
					rvtaIndex++;
				}
				break;
			case Signature:
				Signature sig = (Signature) info;
				String desc = sig.getSignature();
				String genericsType = parse(desc.substring(0, desc.lastIndexOf(">") + 1), true);
				String returnType = parse(desc.substring(desc.lastIndexOf(">") + 1));
				println("     " + genericsType + returnType);
				break;
			case SourceDebugExtension:
				SourceDebugExtension sde = (SourceDebugExtension)info;
				int sdeIndex = 1;
				for(int i : sde.getDebugExtension()) {
					println("     " + sdeIndex + ". " + extract(pool.get(i - 1), pool));
					sdeIndex++;
				}
				break;
			case SourceFile:
				SourceFile sf = (SourceFile)info;
				println("     " + sf.getSourceFile());
				break;
			case Synthetic:
				// do nothing.
				break;
			case StackMapTable:
				StackMapTable smt = (StackMapTable)info;
				Map<Integer, Map<String, List<String>>> stackMap = smt.getEntries();
				for(Integer i : stackMap.keySet()) {
					Map<String, List<String>> map = stackMap.get(i.intValue());
					println("     " + i + ": " + map);
				}
				break;
			default:
				println("unknown attribute type: " + info.getType().name());
				break;
		}
	}

	private void printTargetInfo(TargetInfo info) {
		println("       " + info.getType());
		switch(info.getType()) {
			case TypeParameterTarget:
				TypeParameterTarget tpt = (TypeParameterTarget)info;
				println("       index: " + tpt.getIndex());
				break;
			case SuperTypeTarget:
				SuperTypeTarget stt = (SuperTypeTarget)info;
				println("       index: " + stt.getIndex());
				break;
			case TypeParameterBoundTarget:
				TypeParameterBoundTarget tpbt = (TypeParameterBoundTarget)info;
				print("       bound_index: " + tpbt.getBoundIndex());
				println(", type_parameter_index: " + tpbt.getTPI());
				break;
			case EmptyTarget:
				EmptyTarget et = (EmptyTarget)info;
				break;
			case MethodFormalParameterTarget:
				MethodFormalParameterTarget mfpt = (MethodFormalParameterTarget)info;
				println("       index: " + mfpt.getIndex());
				break;
			case ThrowsTarget:
				ThrowsTarget tt = (ThrowsTarget)info;
				println("       index: " + tt.getIndex());
				break;
			case LocalVarTarget:
				LocalVarTarget lvt = (LocalVarTarget)info;
				for(LocalVarTarget.LVTTable t : lvt.getTable()) {
					print("       index: " + t.getIndex());
					println(", pc: " + t.getStartPc() + "-" + (t.getStartPc() + t.getLen() - 1));
				}
				break;
			case CatchTarget:
				CatchTarget ct = (CatchTarget)info;
				println("       index: " + ct.getIndex());
				break;
			case OffsetTarget:
				OffsetTarget ot = (OffsetTarget)info;
				println("       offset: " + ot.getOffset());
				break;
			case TypeArgumentTarget:
				TypeArgumentTarget tat = (TypeArgumentTarget)info;
				print("          index: " + tat.getIndex());
				println(", offset: " + tat.getOffset());
				break;
			default:
				println("unknown target-info type: " + info.getType());
				break;
		}
	}

	private void printTypePath(TypePath path) {
		for(int i = 0; i < path.getArgIndex().length; i++) {
			print("       type_path_type: ");
			int pk = path.getPathKind()[i];
			int ai = path.getArgIndex()[i];
			switch(pk) {
				case 0:
					println("Annotation is deeper in an array type.");
					break;
				case 1:
					println("Annotation is deeper in a nested type.");
					break;
				case 2:
					println("Annotation is on the bound of a "
							+ "wildcard type argument of a parameterized type.");
					break;
				case 3:
					println("Annotation is on a type argument of a parameterized type.");
					break;
				default:
					println("unknown type_path_kind: " + pk);
					break;
			}
			println("       type_arg_index: " + ai);
		}
	}

	private boolean checkRange(int index) {
		return (0 <= index) && (index < pool.size());
	}
}
