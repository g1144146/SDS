package sophomore.classfile.bytecode;

/**
 *
 * @author inagaki
 */
public class TableSwitch extends OpcodeInfo {
	TableSwitch(int pc) {
		super(MnemonicTable.tableswitch, pc);
	}
}
