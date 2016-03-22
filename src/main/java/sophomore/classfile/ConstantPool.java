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
			throw new ArrayIndexOutOfBoundsException(index);
		}
		pool[index] = element;
	}

	@Override
	public ConstantInfo get(int index) {
		if(index >= pool.length) {
			System.out.println("constant-pool size: " + pool.length);
			throw new ArrayIndexOutOfBoundsException(index);
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
			sb.append("[").append(i+1).append("]: ").append(pool[i]).append(sep);
		}
		return sb.toString();
	}
}