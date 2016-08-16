package sds.decompile;

import sds.assemble.BaseContent;

/**
 * This interface is for decompiling contents of class and member.
 * @author inagaki
 */
public interface Decompiler {
	/**
	 * decompiles specified contents.
	 * @param content decompile target content
	 */
	public void decompile(BaseContent content);

	/**
	 * decompiles specified contents.
	 * @param contents decompile target contents
	 */
	public void decompile(BaseContent[] contents);
}