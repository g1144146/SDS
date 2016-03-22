package sophomore;

import java.io.IOException;
import java.io.PrintStream;

import sophomore.classfile.AccessFlags;
import sophomore.classfile.ConstantPool;
import sophomore.classfile.DescriptorParser;
import sophomore.classfile.FieldInfo;
import sophomore.classfile.MethodInfo;
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
import sophomore.classfile.constantpool.ClassInfo;
import sophomore.classfile.constantpool.MemberRefInfo;
import sophomore.classfile.constantpool.MethodrefInfo;
import sophomore.classfile.constantpool.MethodHandleInfo;
import sophomore.classfile.constantpool.NameAndTypeInfo;
import sophomore.classfile.constantpool.Utf8Info;

import static sophomore.classfile.constantpool.ConstantType.*;
import static sophomore.classfile.constantpool.MethodHandleInfo.*;

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
		ClassInfo info = (ClassInfo)pool.get(cf.thisClass-1);
		Utf8Info utf = (Utf8Info)pool.get(info.getNameIndex()-1);
		out.println(utf.getValue());
	}

	public void printSuperClass() {
		out.println("*** Super Class *** ");
		if(!checkRange(cf.superClass)) {
			out.println(sep);
			return;
		}
		ClassInfo info = (ClassInfo)pool.get(cf.superClass-1);
		Utf8Info utf = (Utf8Info)pool.get(info.getNameIndex()-1);
		out.println(utf.getValue());
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
			out.println("\t"+AccessFlags.get(info.getAccessFlags()));
			Utf8Info name = (Utf8Info)pool.get(info.getNameIndex()-1);
			out.println("\t"+name.getValue());
			Utf8Info desc = (Utf8Info)pool.get(info.getDescriptorIndex()-1);
			out.println("\t"+DescriptorParser.parse(desc.getValue()));
			for(AttributeInfo attr : info.getAttr().getAll()) {
				printAttributeInfo(attr);
			}
		}
	}

	public void printMethods() throws IOException {
		out.println("*** Methods *** ");
		if(cf.methods.getSize() == 0) {
			out.println(sep);
			return;
		}
		for(MethodInfo info : (MethodInfo[])cf.methods.getAll()) {
			out.print("\t"+AccessFlags.get(info.getAccessFlags()));
			Utf8Info name = (Utf8Info)pool.get(info.getNameIndex()-1);
			out.print(", "+name.getValue());
			Utf8Info desc = (Utf8Info)pool.get(info.getDescriptorIndex()-1);
			out.println(", "+DescriptorParser.parse(desc.getValue()));
			for(AttributeInfo attr : info.getAttr().getAll()) {
				printAttributeInfo(attr);
			}
		}
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
	}

	public void printAttributeInfo(AttributeInfo info) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("\t[attribute type]: ").append(info.getType().getType())
			.append(sep);
		switch(info.getType()) {
			case AnnotationDefault:
				AnnotationDefault ad = (AnnotationDefault)info;
				printElementValue(ad.getDefaultValue());
				break;
			case BootstrapMethods:
				BootstrapMethods bsm = (BootstrapMethods)info;
				for(BootstrapMethods.BSM b : bsm.getBSM()) {
					MethodHandleInfo mhi = (MethodHandleInfo)pool.get(b.getBSMRef()-1);
					sb.append("\t");
					MemberRefInfo memInfo = (MemberRefInfo)pool.get(mhi.getReferenceIndex()-1);
					NameAndTypeInfo nati = (NameAndTypeInfo)pool.get(memInfo.getNameAndTypeIndex()-1);
					Utf8Info nameInfo = (Utf8Info)pool.get(nati.getNameIndex()-1);
					Utf8Info descInfo = (Utf8Info)pool.get(nati.getDescriptorIndex()-1);
					String name = nameInfo.getValue();
					String desc = DescriptorParser.parse(descInfo.getValue());
					ClassInfo cInfo = (ClassInfo)pool.get(memInfo.getClassIndex()-1);
					Utf8Info cUtf = (Utf8Info)pool.get(cInfo.getNameIndex()-1);
					String className = cUtf.getValue();
					String memberString = className + "." + name + desc;
					switch(mhi.getReferenceKind()) {
						case REF_GET_FIELD:
							sb.append("REF_getField: "); break;
						case REF_GET_STATIC:
							sb.append("REF_getStatic: "); break;
						case REF_PUT_FIELD:
							sb.append("REF_putField: "); break;
						case REF_PUT_STATIC:
							sb.append("REF_putStatic: "); break;
						case REF_INVOKE_VIRTUAL:
							sb.append("REF_invokeVirtual: "); break;
						case REF_INVOKE_STATIC:
							sb.append("REF_invokeStatic: "); break;
						case REF_INVOKE_SPECIAL:
							sb.append("REF_invokeSpecial: "); break;
						case REF_NEW_INVOKE_SPECIAL:
							sb.append("REF_newInvokeSpecial: "); break;
						case REF_INVOKE_INTERFACE:
							sb.append("REF_invokeInterface: "); break;
						default:
							System.out.println(">>> unknown reference kind <<<"); break;
					}
					sb.append(memberString).append("\n");
				}
				break;
			case Code:
				Code code = (Code)info;
				sb.append("\tmax_stack: ").append(code.getMaxStack());
				sb.append("\n\tmax_locals: ").append(code.maxLocals());
				sb.append("\n\tcode: ").append(new String(code.getCode(), "UTF-8"));
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
					ClassInfo ci = (ClassInfo)pool.get(em.classIndex()-1);
					Utf8Info utf = (Utf8Info)pool.get(ci.getNameIndex()-1);
					sb.append("\t").append(utf.getValue()).append(sep);
				}
				if(checkRange(em.methodIndex()-1)) {
					MethodrefInfo mi = (MethodrefInfo)pool.get(em.methodIndex()-1);
					NameAndTypeInfo nati = (NameAndTypeInfo)pool.get(mi.getNameAndTypeIndex()-1);
					Utf8Info descInfo = (Utf8Info)pool.get(nati.getDescriptorIndex()-1);
					Utf8Info nameInfo = (Utf8Info)pool.get(nati.getNameIndex()-1);
					String desc = DescriptorParser.parse(descInfo.getValue());
					String name = nameInfo.getValue();
					ClassInfo ci = (ClassInfo)pool.get(mi.getClassIndex()-1);
					Utf8Info classInfo = (Utf8Info)pool.get(ci.getNameIndex()-1);
					sb.append("\t").append(classInfo.getValue())
						.append(".").append(name).append(desc).append(sep);
				}
				break;
			case Exceptions:
				Exceptions ex = (Exceptions)info;
				for(int i : ex.getExceptionIndexTable()) {
					if(checkRange(i-1)) {
						ClassInfo ci = (ClassInfo)pool.get(i-1);
						Utf8Info utf = (Utf8Info)pool.get(ci.getNameIndex());
						sb.append("\t").append(utf.getValue()).append(sep);
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
					sb.append("\t").append(AccessFlags.get(accessFlag));
					if(checkRange(inner-1)) {
						ClassInfo inClass = (ClassInfo)pool.get(inner-1);
						Utf8Info inUtf = (Utf8Info)pool.get(inClass.getNameIndex()-1);
						sb.append(inUtf.getValue()).append("\n");
					}
					if(checkRange(outer-1)) {
						ClassInfo outClass = (ClassInfo)pool.get(outer-1);
						Utf8Info outUtf = (Utf8Info)pool.get(outClass.getNameIndex()-1);
						sb.append("\touter_class: ").append(outUtf.getValue()).append("\n");
					}
					if(checkRange(name-1)) {
						Utf8Info nameInfo = (Utf8Info)pool.get(name-1);
						sb.append("\t").append(nameInfo.getValue());
					}
				}
				break;
			case LineNumberTable:
				LineNumberTable lnt = (LineNumberTable)info;
				for(LineNumberTable.LNTable t : lnt.getLineNumberTable()) {
					sb.append("\tstart_pc: ").append(t.getStartPc())
						.append(", line_number: ").append(t.getLineNumber())
						.append("\n");
				}
				break;
			case LocalVariableTable:
				LocalVariableTable lvt = (LocalVariableTable)info;
				for(LocalVariable.LVTable t : lvt.getTable()) {
					sb.append("start_pc: ").append(t.getNumber("start_pc"))
						.append(", length: ").append(t.getNumber("length")).append("\n\t");
					Utf8Info nameInfo = (Utf8Info)pool.get(t.getNumber("name_index"));
					Utf8Info descInfo = (Utf8Info)pool.get(t.getNumber("descriptor"));
					sb.append(nameInfo.getValue()).append(descInfo.getValue()).append("\n\t");
					sb.append("index: ").append(t.getNumber("index"));
				}
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable)info;
				for(LocalVariable.LVTable t : lvtt.getTable()) {
					sb.append("start_pc: ").append(t.getNumber("start_pc"))
						.append(", length: ").append(t.getNumber("length")).append("\n\t");
					Utf8Info nameInfo = (Utf8Info)pool.get(t.getNumber("name_index"));
					Utf8Info descInfo = (Utf8Info)pool.get(t.getNumber("descriptor"));
					sb.append(nameInfo.getValue()).append(descInfo.getValue()).append("\n\t");
					sb.append("index: ").append(t.getNumber("index"));
				}
				break;
			case MethodParameters:
				MethodParameters mp = (MethodParameters)info;
				for(MethodParameters.Parameters p : mp.getParams()) {
					String flag = AccessFlags.get(p.getAccessFlag());
					Utf8Info nameInfo = (Utf8Info)pool.get(p.getNameIndex()-1);
					sb.append(flag).append(nameInfo.getValue());
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
					pa.getTargetType();
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
					pa.getTargetType();
					for(ElementValuePair e : pa.getElementValuePairs()) {
						printElementValuePair(e);
					}
				}
				break;
			case Signature:
				Signature sig = (Signature)info;
				Utf8Info sigUtf = (Utf8Info)pool.get(sig.getSignatureIndex()-1);
				sb.append("\t").append(sigUtf.getValue());
				break;
			case SourceDebugExtension:
				SourceDebugExtension sde = (SourceDebugExtension)info;
				String de = new String(sde.getDebugExtension(), "UTF-8");
				sb.append("\t").append(de);
				break;
			case SourceFile:
				SourceFile sf = (SourceFile)info;
				Utf8Info sfUtf = (Utf8Info)pool.get(sf.getSourceFileIndex()-1);
				sb.append("\tsource file: ").append(sfUtf.getValue());
				break;
			case Synthetic:
				// do nothing.
				break;
			case StackMapTable:
				StackMapTable smt = (StackMapTable)info;
				break;
			default:
				System.out.println("unknow attribute type: " + info.getType().name());
		}
		out.println(sb.toString());
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
}