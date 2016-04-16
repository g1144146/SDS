package sds.classfile.bytecode;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * This class is for
 * {@link OpcodeInfo <code>OpcodeInfo</code>}
 * sequence of method.
 * @author inagaki
 */
public class Opcodes {
	private Map<Integer, OpcodeInfo> opcodeMap;

	/**
	 * constructor.
	 */
	public Opcodes() {
		this.opcodeMap = new ConcurrentSkipListMap<>();
	}

	/**
	 * adds opcode to map.
	 * @param key index into the code array
	 * @param element opcode
	 */
	public void add(int key, OpcodeInfo element) {
		opcodeMap.put(key, element);
	}

	/**
	 * returns opcode of specified pc.
	 * @param pc index into the code array
	 * @return opcode
	 */
	public OpcodeInfo get(int pc) {
		return opcodeMap.get(pc);
	}

	/**
	 * returns opcodes.
	 * @return opcodes.
	 */
	public OpcodeInfo[] getAll() {
		return opcodeMap.values().toArray(new OpcodeInfo[0]);
	}

	/**
	 * returns values of pc of opcode.
	 * @return values
	 */
	public int[] getKeys() {
		return opcodeMap.keySet()
			.stream().mapToInt(Integer::intValue).toArray();
	}
}