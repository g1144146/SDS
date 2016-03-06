package sophomore.classfile;

import sophomore.classfile.constantpool.ConstantInfo;

/**
 * 
 * @author inagaki
 */
public class ConstantPool implements ArrayInfo<ConstantInfo> {
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

	@Override
	public int getSize() {
		return pool.length;
	}

	@Override
	public void add(int index, ConstantInfo element) {
		if(index >= pool.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		pool[index] = element;
	}

	@Override
	public ConstantInfo get(int index) {
		if(index >= pool.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return pool[index];
	}

	@Override
	public ConstantInfo[] getAll() {
		return pool;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String sep = System.getProperty("line.separator");
		sb.append("*** Constant Pool ***").append(sep);
		for(int i = 0; i < pool.length; i++) {
			sb.append("[").append(i).append("]: ").append(pool[i]).append(sep);
		}
		return sb.toString();
	}
}