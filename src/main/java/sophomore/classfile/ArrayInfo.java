package sophomore.classfile;

import java.util.RandomAccess;

/**
 *
 * @author inagaki
 * @param <T>
 */
public interface ArrayInfo<T extends Info> extends RandomAccess {

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
