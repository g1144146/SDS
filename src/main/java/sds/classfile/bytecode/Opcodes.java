package sds.classfile.bytecode;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

/**
 * This class is for
 * {@link OpcodeInfo <code>OpcodeInfo</code>}
 * sequence of method.
 * @author inagaki
 */
public class Opcodes {
	private IntObjectHashMap<OpcodeInfo> opcodeMap;

	/**
	 * constructor.
	 */
	public Opcodes() {
		this.opcodeMap = new IntObjectHashMap<>();
	}

	/**
	 * returns opcode map size.
	 * @return size
	 */
	public int size() {
		return opcodeMap.size();
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
		OpcodeInfo[] values = opcodeMap.values().toArray(new OpcodeInfo[0]);
		Comparator<OpcodeInfo> c = this::compare;
		Arrays.sort(values, c);
		return values;
	}

	/**
	 * returns values of pc of opcode.
	 * @return values
	 */
	public int[] getKeys() {
		int keys[] = opcodeMap.keySet().toArray();
		Arrays.sort(keys);
		return keys;
	}

	/**
	 * returns opcode map.
	 * @return opcode map
	 */
	public IntObjectHashMap<OpcodeInfo> getMap() {
		return opcodeMap;
	}

	private int compare(OpcodeInfo o1, OpcodeInfo o2) {
		return o1.getPc() - o2.getPc();
	}
}