package sds.classfile;

/**
 * This interface is for array of {@link Info <code>Info</code>}.
 * @author inagaki
 * @param <T> type extends {@link Info <code>Info</code>}
 */
public interface ArrayInfo<T extends Info> {

	/**
	 * returns size of array.
	 * @return size
	 */
	abstract int size();

	/**
	 * adds element to array.
	 * @param index index of array.
	 * @param element element
	 */
	abstract void add(int index, T element);

	/**
	 * returns element of array.
	 * @param index index of array.
	 * @return element
	 */
	abstract T get(int index);

	/**
	 * returns array.
	 * @return array
	 */
	abstract T[] getAll();
}
