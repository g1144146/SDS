package sds.util;

import java.io.IOException;
import java.io.PrintStream;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

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
 * This class is for debugging.
 * @author inagaki
 */
public class ClassFilePrinter {
	private PrintStream out = System.out;
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
		out.println(extract(pool.get(thisClass - 1), pool));
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
		out.println(extract(pool.get(superClass - 1), pool));
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
			out.println(extract(pool.get(i - 1), pool));
		}
		out.print(sep);
	}

	/**
	 * prints fields of this class.
	 * @param fields field
	 * @throws IOException
	 */
	public void printFields(Fields fields) throws Exception {
		out.println("*** Fields *** ");
		if(fields.size() == 0) {
			out.println("has no fields." + sep);
			return;
		}

		for(int i = 0; i < fields.size(); i++) {
			MemberInfo field = fields.get(i);
			out.println(i + 1 + ". " + field.getAccessFlags()
					+ field.getDescriptor() + " " + field.getName());
			Attributes attr = field.getAttr();
			for(int j = 0; j < attr.size(); j++) {
				AttributeInfo info = attr.get(j);
				if(info.getType() != AttributeType.Signature) {
					printAttributeInfo(attr.get(j));
				} else {
					out.println("  " + info.getType().toString());
					Signature sig = (Signature)info;
					out.println("     " + parse(sig.getSignature()));
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
	public void printMethods(Methods methods) throws Exception {
		out.println("*** Methods *** ");
		if(methods.size() == 0) {
			out.print("has no methods." + sep);
			return;
		}
		for(int i = 0; i < methods.size(); i++) {
			MemberInfo method = methods.get(i);
			out.println(i + 1 + ". " + method.getAccessFlags()
					+ method.getDescriptor() + " " + method.getName());
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
	public void printAttributes(Attributes attr) throws Exception {
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
	 * @throws Exception
	 */
	public void printAttributeInfo(AttributeInfo info) throws Exception {
		out.println("  " + info.getType().toString());
		switch(info.getType()) {
			case AnnotationDefault:
				AnnotationDefault ad = (AnnotationDefault)info;
				out.println("     " + ad.getDefaultValue());
				break;
			case BootstrapMethods:
				BootstrapMethods bsm = (BootstrapMethods)info;
				for(BootstrapMethods.BSM b : bsm.getBSM()) {
					out.println("     bsm ref: " + b.getBSMRef());
					for(String arg : b.getBSMArgs()) {
						out.println("     bsm args: " + arg);
					}
				}
				break;
			case Code:
				Code code = (Code)info;
				out.print("     max_stack: " + code.getMaxStack());
				out.println(", max_locals: " + code.maxLocals());
				this.opcodes = code.getCode();
				for(OpcodeInfo op : opcodes.getAll()) {
					out.println("     " + op.getPc() + " - " + op);
				}
				if(code.getExceptionTable().length > 0) {
					out.println("  ExceptionTable");
				}
				int codeIndex = 1;
				for(Code.ExceptionTable t : code.getExceptionTable()) {
					out.print("     " + codeIndex + ". pc: " + t.getNumber("start_pc")
							+ "-" + t.getNumber("end_pc"));
					out.print(", handler: " + t.getNumber("handler_pc"));
					out.println(", catch_type: " + t.getCatchType());
					codeIndex++;
				}
				for(AttributeInfo ai : code.getAttr().getAll()) {
					printAttributeInfo(ai);
				}
				break;
			case ConstantValue:
				ConstantValue cv = (ConstantValue)info;
				out.println("     " + cv.getConstantValue());
				break;
			case Deprecated:
				// do nothing.
				break;
			case EnclosingMethod:
				EnclosingMethod em = (EnclosingMethod)info;
				out.println("     " + em.getEncClass());
				out.println("     " + em.getEncMethod());
				break;
			case Exceptions:
				Exceptions ex = (Exceptions)info;
				int excepIndex = 1;
				for(String exception : ex.getExceptionTable()) {
					out.println("     " + excepIndex + ". " + exception);
					excepIndex++;
				}
				break;
			case InnerClasses:
				InnerClasses ic = (InnerClasses)info;
				int classIndex = 1;
				for(InnerClasses.Classes c : ic.getClasses()) {
					String inner = c.getNumber("inner");
					String accessFlag = c.getNumber("access_flag");
					out.println("     " + classIndex + ". " + accessFlag + inner);
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
							+ "-" + (t.getNumber("start_pc") + t.getNumber("length") - 1));
					out.print(", name: " + t.getName());
					out.print(", index : " + t.getNumber("index"));
					out.println(", desc: " + t.getDesc());
					localIndex++;
				}
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable)info;
				int varIndex = 1;
				for(LocalVariable.LVTable t : lvtt.getTable()) {
					out.print("     " + varIndex + ". pc: " + t.getNumber("start_pc")
							+ "-" + (t.getNumber("start_pc") + t.getNumber("length") - 1));
					out.println(", name: " + t.getName() + ", index: " + t.getNumber("index"));
					out.println("     desc: " + t.getDesc());
					varIndex++;
				}
				break;
			case MethodParameters:
				MethodParameters mp = (MethodParameters)info;
				int methodIndex = 1;
				for(MethodParameters.Parameters p : mp.getParams()) {
					out.println("   " + methodIndex + ". " + p.getAccessFlag() + p.getName());
					methodIndex++;
				}
				break;
			case RuntimeInvisibleAnnotations:
				RuntimeInvisibleAnnotations ria = (RuntimeInvisibleAnnotations) info;
				int riaIndex = 1;
				for(String a : ria.getAnnotations()) {
					out.println("     " + riaIndex + "." + a);
					riaIndex++;
				}
				break;
			case RuntimeInvisibleParameterAnnotations:
				RuntimeInvisibleParameterAnnotations ripa = (RuntimeInvisibleParameterAnnotations)info;
				int ripaIndex = 1;
				for(ParameterAnnotations pa : ripa.getParamAnnotations()) {
					out.println("     " + ripaIndex + ".");
					for(String a : pa.getAnnotations()) {
						out.println("       " + a);
					}
					ripaIndex++;
				}
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations)info;
				int ritaIndex = 1;
				for(TypeAnnotation ta : rita.getTypes()) {
					out.println("     " + ritaIndex + "." + parseAnnotation(ta, new StringBuilder(), pool));
					printTargetInfo(ta.getTargetInfo());
					printTypePath(ta.getTargetPath());
					ritaIndex++;
				}
				break;
			case RuntimeVisibleAnnotations:
				RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)info;
				int rvaIndex = 1;
				for(String a : rva.getAnnotations()) {
					out.println("     " + rvaIndex + "." + a);
					rvaIndex++;
				}
				break;
			case RuntimeVisibleParameterAnnotations:
				RuntimeVisibleParameterAnnotations rvpa = (RuntimeVisibleParameterAnnotations)info;
				int rvpaIndex = 1;
				for(ParameterAnnotations pa : rvpa.getParamAnnotations()) {
					out.println("     " + rvpaIndex + ".");
					for(String a : pa.getAnnotations()) {
						out.println("       " + a);
					}
					rvpaIndex++;
				}
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
				int rvtaIndex = 1;
				for(TypeAnnotation pa : rvta.getTypes()) {
					out.println("     " + rvtaIndex + "." + parseAnnotation(pa, new StringBuilder(), pool));
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
				out.println("     " + genericsType + returnType);
				break;
			case SourceDebugExtension:
				SourceDebugExtension sde = (SourceDebugExtension)info;
				int sdeIndex = 1;
				for(int i : sde.getDebugExtension()) {
					out.println("     " + sdeIndex + ". " + extract(pool.get(i - 1), pool));
					sdeIndex++;
				}
				break;
			case SourceFile:
				SourceFile sf = (SourceFile)info;
				out.println("     " + sf.getSourceFile());
				break;
			case Synthetic:
				// do nothing.
				break;
			case StackMapTable:
				StackMapTable smt = (StackMapTable)info;
				int stackIndex = 1;
				IntObjectHashMap<UnifiedMap<String, MutableList<String>>> stackMap = smt.getEntries();
				for(int i : stackMap.keySet().toArray()) {
					UnifiedMap<String, MutableList<String>> map = stackMap.get(i);
					out.println("     " + i + ": " + map);
				}
				break;
			default:
				System.out.println("unknown attribute type: " + info.getType().name());
				break;
		}
	}

	private void printTargetInfo(TargetInfo info) {
		out.println("       " + info.getType());
		switch(info.getType()) {
			case TypeParameterTarget:
				TypeParameterTarget tpt = (TypeParameterTarget)info;
				out.println("       index: " + tpt.getIndex());
				break;
			case SuperTypeTarget:
				SuperTypeTarget stt = (SuperTypeTarget)info;
				out.println("       index: " + stt.getIndex());
				break;
			case TypeParameterBoundTarget:
				TypeParameterBoundTarget tpbt = (TypeParameterBoundTarget)info;
				out.print("       bound_index: " + tpbt.getBoundIndex());
				out.println(", type_parameter_index: " + tpbt.getTPI());
				break;
			case EmptyTarget:
				EmptyTarget et = (EmptyTarget)info;
				break;
			case MethodFormalParameterTarget:
				MethodFormalParameterTarget mfpt = (MethodFormalParameterTarget)info;
				out.println("       index: " + mfpt.getIndex());
				break;
			case ThrowsTarget:
				ThrowsTarget tt = (ThrowsTarget)info;
				out.println("       index: " + tt.getIndex());
				break;
			case LocalVarTarget:
				LocalVarTarget lvt = (LocalVarTarget)info;
				for(LocalVarTarget.LVTTable t : lvt.getTable()) {
					out.print("       index: " + t.getIndex());
					out.println(", pc: " + t.getStartPc() + "-" + (t.getStartPc() + t.getLen() - 1));
				}
				break;
			case CatchTarget:
				CatchTarget ct = (CatchTarget)info;
				out.println("       index: " + ct.getIndex());
				break;
			case OffsetTarget:
				OffsetTarget ot = (OffsetTarget)info;
				out.println("       offset: " + ot.getOffset());
				break;
			case TypeArgumentTarget:
				TypeArgumentTarget tat = (TypeArgumentTarget)info;
				out.print("          index: " + tat.getIndex());
				out.println(", offset: " + tat.getOffset());
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
				case 0:
					out.println("Annotation is deeper in an array type.");
					break;
				case 1:
					out.println("Annotation is deeper in a nested type.");
					break;
				case 2:
					out.println("Annotation is on the bound of a "
							+ "wildcard type argument of a parameterized type.");
					break;
				case 3:
					out.println("Annotation is on a type argument of a parameterized type.");
					break;
				default:
					out.println("unknown type_path_kind: " + pk);
					break;
			}
			out.println("       type_arg_index: " + ai);
		}
	}

	private boolean checkRange(int index) {
		return (0 <= index) && (index < pool.size());
	}
}