package sandbox.segtree;

public interface INode<E> extends Iterable<E> {
	int left();
	int right();
	E value();
	void change(Change<E> change);
	void change(int from, int to, Change<E> change);
	/* x must be in correct range, otherwise behavior is undefined */
	E get(int x);
	E get(int from, int to, Operation<E> op);
}
