package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
class VerificationTypeInfo {
	int topVariableInfo;
	int integerVariableInfo;
	int floatVariableInfo;
	int longVariableInfo;
	int doubleVariableInfo;
	int nullVariableInfo;
	
	int uninitializedThisVariableInfo;
	int objectVariableInfo;
	int uninitializedVariableInfo;
	VerificationTypeInfo(RandomAccessFile raf) throws IOException {
		
	}
}
