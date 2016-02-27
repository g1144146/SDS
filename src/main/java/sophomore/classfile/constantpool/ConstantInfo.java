package sophomore.classfile.constantpool;

public abstract class ConstantInfo {
	int tag;
	int index;
	public ConstantInfo(int tag) {
		this.tag = tag;
	}

	public int getTag() {
		return tag;
	}
}