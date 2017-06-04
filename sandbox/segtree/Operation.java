package sandbox.segtree;

public interface Operation<E> {
	public E perform(E a, E b);
	public E neutral();
}
