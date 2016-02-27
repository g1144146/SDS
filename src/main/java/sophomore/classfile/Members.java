package sophomore.classfile;

import java.util.Iterator;
import java.util.NoSuchElementException;

abstract class Members <T> {
	
	T[] elements;
	int currentIndex = 0;

	public Members() {}

	public int getSize() {
		return elements.length;
	}

	public void addElement(T element) {
		if(currentIndex == elements.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		elements[currentIndex++] = element;
	}

	public T[] getElements() {
		return elements;
	}	
}