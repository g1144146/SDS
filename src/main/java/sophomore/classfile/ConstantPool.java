package sophomore.classfile;

import sophomore.classfile.constantpool.ConstantInfo;

/**
 * 
 * @author inagaki
 */
public class ConstantPool {
	/**
	 * 
	 */
	private ConstantInfo[] pool;
	/**
	 * 
	 */
	private int currentIndex;

	/**
	 * 
	 * @param size 
	 */
	public ConstantPool(int size) {
		this.pool = new ConstantInfo[size];
	}

	/**
	 * 
	 * @param element 
	 */
	public void add(ConstantInfo element) {
		if(currentIndex == pool.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		pool[currentIndex++] = element;
	}

	/**
	 * 
	 * @return 
	 */
	public ConstantInfo[] getPool() {
		return pool;
	}
}