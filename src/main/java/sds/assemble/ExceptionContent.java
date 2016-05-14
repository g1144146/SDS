package sds.assemble;

import sds.classfile.attributes.Code.ExceptionTable;

/**
 * This class is for contents of method's try-catch-finally statement.
 * @author inagaki
 */
public class ExceptionContent {
	private int[] from, to, target;
	private String[] exception;

	public ExceptionContent(ExceptionTable[] table, String[] exception) {
		this.from   = new int[table.length];
		this.to     = new int[table.length];
		this.target = new int[table.length];
		for(int i = 0; i < table.length; i++) {
			from[i]   = table[i].getNumber("start_pc");
			to[i]     = table[i].getNumber("end_pc");
			target[i] = table[i].getNumber("handler_pc");
		}
		this.exception = exception;
	}

	/**
	 * returns from-indexes into code array.
	 * @return from-indexes
	 */
	public int[] getFrom() {
		return from;
	}

	/**
	 * returns to-indexes into code array.
	 * @return to-indexes
	 */
	public int[] getTo() {
		return to;
	}

	/**
	 * returns target indexes into code array.
	 * @return target indexes
	 */
	public int[] getTarget() {
		return target;
	}

	/**
	 * returns exception class names.
	 * @return exception class names
	 */
	public String[] getException() {
		return exception;
	}

	/**
	 * returns array index which specified pc is
	 * in range between from-index and (to-index - 1).<br>
	 * if the array index didn't find, this method returns -1.
	 * @param pc index into code array
	 * @param isAny whether the range has no exception
	 * @return array index
	 */
	public int getIndexInRange(int pc, boolean isAny) {
		for(int i = 0; i < from.length; i++) {
			if(from[i] <= pc && pc < to[i]) {
				if(isAny) {
					if(exception[i].equals("any")) {
						return i;
					}
				} else {
					if(! exception[i].equals("any")) {
						return i;
					}
				}
			}
		}
		return -1;
	}
}