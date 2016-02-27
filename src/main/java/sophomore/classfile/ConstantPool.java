package sophomore.classfile;

import sophomore.classfile.constantpool.ConstantInfo;

public class ConstantPool {
	private ConstantInfo[] pool;
	private int currentIndex;
	public ConstantPool(int size) {
		this.pool = new ConstantInfo[size];
	}

	public void addElement(ConstantInfo element) {
		if(currentIndex == pool.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		pool[currentIndex++] = element;
	}

	public ConstantInfo[] getPool() {
		return pool;
	}
}