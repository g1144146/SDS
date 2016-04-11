package sds.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class CatchTarget extends AbstractTargetInfo {
	/**
	 * 
	 */
	int exceptionTableIndex;

	CatchTarget(int index) {
		super(TargetInfoType.CatchTarget);
		this.exceptionTableIndex = index;
	}

	/**
	 * returns exception-table-index.
	 * @return 
	 */
	public int getIndex() {
		return exceptionTableIndex;
	}
}
