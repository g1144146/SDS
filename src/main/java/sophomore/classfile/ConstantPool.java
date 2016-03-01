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
	 * @param size 
	 */
	public ConstantPool(int size) {
		this.pool = new ConstantInfo[size];
	}

	/**
	 * 
	 * @param index
	 * @param element 
	 */
	public void add(int index, ConstantInfo element) {
		if(index >= pool.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		pool[index] = element;
	}

	/**
	 * 
	 * @return 
	 */
	public ConstantInfo[] getPool() {
		return pool;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String sep = System.getProperty("line.separator");
		sb.append("*** Constant Pool ***").append(sep);
		for(int i = 0; i < pool.length; i++) {
			sb.append("[").append(i+1).append("]: ").append(pool[i]).append(sep);
		}
		return sb.toString();
	}
}