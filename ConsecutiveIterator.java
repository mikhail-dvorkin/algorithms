import java.util.*;

public class ConsecutiveIterator<E> implements Iterator<E> {
	Iterator<Iterable<E>> i;
	Iterator<E> current = null;
	
	public ConsecutiveIterator(Iterator<Iterable<E>> i) {
		this.i = i;
	}

	@Override
	public boolean hasNext() {
		while (true) {
			if (current != null && current.hasNext()) {
				return true;
			}
			if (!i.hasNext()) {
				current = null;
				return false;
			}
			current = i.next().iterator();
		}
	}

	@Override
	public E next() {
		while (true) {
			if (current != null && current.hasNext()) {
				return current.next();
			}
			if (!i.hasNext()) {
				current = null;
				throw new NoSuchElementException();
			}
			current = i.next().iterator();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
