package sandbox.segtree;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Node<E> implements INode<E> {
	INode<E> a, b;
	int left, right;
	E value;
	Queue<Change<E>> queue;

	public Node(INode<E> a, INode<E> b, Operation<E> op) {
		this(a, b, op.perform(a.value(), b.value()));
	}
	
	public Node(INode<E> a, INode<E> b, E value) {
		this.a = a;
		this.b = b;
		this.left = a.left();
		this.right = b.right();
		this.value = value;
		this.queue = new ArrayDeque<Change<E>>(2);
	}
	
	@Override
	public int left() {
		return left;
	}

	@Override
	public int right() {
		return right;
	}

	private void relax() {
		while (!queue.isEmpty()) {
			Change<E> change = queue.poll();
			value = change.apply(value, right - left);
			a.change(change);
			b.change(change);
		}
	}

	@Override
	public E value() {
		relax();
		return value;
	}

	@Override
	public void change(Change<E> change) {
		queue.add(change);
	}

	@Override
	public void change(int from, int to, Change<E> change) {
		if (right <= from || to <= left) {
			return;
		}
		if (from <= left && right <= to) {
			queue.add(change);
			return;
		}
		a.change(from, to, change);
		b.change(from, to, change);
	}

	@Override
	public E get(int x) {
		if (x >= b.left()) {
			return b.get(x);
		}
		return a.get(x);
	}

	@Override
	public E get(int from, int to, Operation<E> op) {
		if (right <= from || to <= left) {
			return op.neutral();
		}
		if (from <= left && right <= to) {
			return value();
		}
		relax();
		return op.perform(a.get(from, to, op), b.get(from, to, op));
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			Iterator<E> ia = a.iterator(), ib = b.iterator();

			@Override
			public boolean hasNext() {
				return ia.hasNext() || ib.hasNext();
			}

			@Override
			public E next() {
				if (ia.hasNext()) {
					return ia.next();
				}
				if (ib.hasNext()) {
					return ib.next();
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
