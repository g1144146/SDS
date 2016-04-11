/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sds.classfile.attributes.stackmap;

/**
 *
 * @author inagakikenichi
 */
public abstract class AbstractVerificationTypeInfo implements VerificationTypeInfo {
	/**
	 * 
	 */
	int tag;
	/**
	 * 
	 */
	VerificationInfoType type;

	AbstractVerificationTypeInfo(int tag, VerificationInfoType type) {
		this.tag = tag;
		this.type = type;
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public VerificationInfoType getType() {
		return type;
	}
}
