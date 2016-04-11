/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sds.classfile.attributes.annotation;

/**
 *
 * @author inagakikenichi
 */
public class TypeParameterBoundTarget extends AbstractTargetInfo {
	/**
	 * 
	 */
	int typeParameterIndex;
	/**
	 * 
	 */
	int boundIndex;

	TypeParameterBoundTarget(int typeParameterIndex, int boundIndex) {
		super(TargetInfoType.TypeParameterBoundTarget);
		this.typeParameterIndex = typeParameterIndex;
		this.boundIndex = boundIndex;
	}

	/**
	 * returns type-parameter-index.
	 * @return 
	 */
	public int getTPI() {
		return typeParameterIndex;
	}

	public int getBoundIndex() {
		return boundIndex;
	}
}
