package sds.decompile;

/**
 * This class is adapter class for decompiler.
 * @author inagaki
 */
public abstract class AbstractDecompiler implements Decompiler {
	DecompiledResult result;

	AbstractDecompiler(DecompiledResult result) {
		this.result = result;
	}

	/**
	 * retunrs decompiled source.
	 * @return decompiled source
	 */
	public DecompiledResult getResult() {
		return result;
	}
}