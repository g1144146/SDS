package sds.classfile;

/**
 *
 * @author inagaki
 * @param <T>
 */
public interface ArrayInfo<T extends Info> {

	/**
	 * 
	 * @return 
	 */
	abstract int size();

	/**
	 * 
	 * @param index
	 * @param element 
	 */
	abstract void add(int index, T element);

	/**
	 * 
	 * @param index
	 * @return 
	 */
	abstract T get(int index);

	/**
	 * 
	 * @return 
	 */
	abstract T[] getAll();
}
