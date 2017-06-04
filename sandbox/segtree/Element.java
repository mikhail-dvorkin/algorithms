package sandbox.segtree;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Element<E> implements INode<E> {
	int x;
	E value;

	public Element(int x, E value) {
		this.x = x;
		this.value = value;
	}

	@Override
	public int left() {
		return x;
	}

	@Override
	public int right() {
		return x + 1;
	}

	@Override
	public E value() {
		return value;
	}

	@Override
	public void change(Change<E> change) {
		value = change.apply(value, 1);
	}

	@Override
	public void change(int from, int to, Change<E> change) {
		if (x < from || to <= x) {
			return;
		}
		change(change);
	}

	@Override
	public E get(@SuppressWarnings({"hiding", "unused"}) int x) {
		return value;
	}

	@Override
	public E get(int from, int to, Operation<E> op) {
		if (x < from || to <= x) {
			return op.neutral();
		}
		return value;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			boolean hasNext = true;

			@Override
			public boolean hasNext() {
				return hasNext;
			}

			@Override
			public E next() {
				if (hasNext) {
					return value;
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
