package sophomore.classfile.bytecode;

import java.util.Arrays;

import sophomore.classfile.ArrayInfo;

/**
 *
 * @author inagaki
 */
public class Opcodes implements ArrayInfo<OpcodeInfo> {

	OpcodeInfo[] opcodes;
	int position;

	public Opcodes(int size) {
		this.opcodes = new OpcodeInfo[size];
		this.position = 0;
	}

	public Opcodes() {
		// default size is 10.
		this(10);
	}

	@Override
	public int getSize() {
		return opcodes.length;
	}

	public void add(OpcodeInfo element) {
		add(position, element);
	}

	@Override
	public void add(int index, OpcodeInfo element) {
		if(index >= opcodes.length) {
			this.opcodes = Arrays.copyOf(opcodes, opcodes.length+1, opcodes.getClass());
		}
		opcodes[position++] = element;
	}

	@Override
	public OpcodeInfo get(int index) {
		if(index >= opcodes.length) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return opcodes[index];
	}

	@Override
	public OpcodeInfo[] getAll() {
		return Arrays.copyOfRange(opcodes, 0, position);
	}
	
}
