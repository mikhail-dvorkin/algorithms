package sandbox.segtree;

public interface Change<E> {
	public E apply(E value, int num);
}
