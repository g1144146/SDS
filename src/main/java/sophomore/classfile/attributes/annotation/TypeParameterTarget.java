package sophomore.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class TypeParameterTarget extends AbstractTargetInfo {
	/**
	 * 
	 */
	int typeParameterIndex;

	public TypeParameterTarget(int typeParameterIndex) {
		super(TargetInfoType.TypeParameterTarget);
		this.typeParameterIndex = typeParameterIndex;
	}

	/**
	 * returns type-parameter-index.
	 * @return 
	 */
	public int getIndex() {
		return typeParameterIndex;
	}
}
