package sophomore.classfile.bytecode;

/**
 *
 * @author inagaki
 */
public class LookupSwitch extends OpcodeInfo {
	LookupSwitch(int pc) {
		super(MnemonicTable.lookupswitch, pc);
	}
}
