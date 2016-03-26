package sophomore.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class MethodFormalParameterTarget extends AbstractTargetInfo {
	/**
	 * 
	 */
	int formalParameterIndex;

	MethodFormalParameterTarget(int formalParameterIndex) {
		super(TargetInfoType.MethodFormalParameterTarget);
		this.formalParameterIndex = formalParameterIndex;
	}

	/**
	 * returns formal-parameter-index.
	 * @return 
	 */
	public int getIndex() {
		return formalParameterIndex;
	}
}
