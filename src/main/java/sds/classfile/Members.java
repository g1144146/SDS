package sds.classfile;

import java.util.Arrays;
import java.util.Iterator;

/**
 * This adapter class is for class has members.
 * @author inagaki
 * @param <T> type extends {@link MemberInfo <code>MemberInfo</code>}
 */
public class Members<T extends MemberInfo> extends AbstractArrayInfo<MemberInfo> implements Iterable<MemberInfo> {
	@Override
	public Iterator<MemberInfo> iterator() {
		return Arrays.asList((MemberInfo[])elements).iterator();
	}

	@Override
	public String toString() {
		if(elements.length == 0) {
			return "this class has no " + ((this instanceof Methods) ? "method" : "field");
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < elements.length; i++) {
			sb.append("[").append(i).append("]: ").append(elements[i].toString()).append("\n");
		}
		return sb.toString();
	}
}