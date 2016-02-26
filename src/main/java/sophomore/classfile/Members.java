package sophomore.classfile;

import java.util.Iterator;
import java.util.NoSuchElementException;

abstract class Members <T> implements Iterable<T> {
	
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

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int index = 0;
			@Override
			public boolean hasNext() {
				return index < elements.length;
			}

			@Override
			public T next() {
				if(hasNext()) {
					return elements[index++];
				}
				throw new NoSuchElementException("" + index);
			}
		};
	}
	
}