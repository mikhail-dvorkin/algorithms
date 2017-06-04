package sandbox.segtree;

import java.util.AbstractCollection;
import java.util.Iterator;

public class SegTree<E> extends AbstractCollection<E> {
	INode<E> root;
	
	public SegTree(E[] e, Operation<E> op) {
		root = makeNode(e, op, 0, e.length, 8);
	}
	
	/* size >= to - from */
	private INode<E> makeNode(E[] e, Operation<E> op, int from, int to, int size) {
		if (size == 1) {
			if (from < to) {
				return new Element<E>(from, e[from]);
			}
			return new Element<E>(from, op.neutral());
		}
//		Node<E> a = makeNode(e, from, from + size / 2, size / 2);
		return null;
	}

	@Override
	public Iterator<E> iterator() {
		return root.iterator();
	}

	@Override
	public int size() {
		return root.right() - root.left();
	}
	
	public static void main(String[] args) {
		SegTree<Integer> segTree = new SegTree<Integer>(new Integer[]{5, 6, 6}, new Operation<Integer>() {
			@Override
			public Integer perform(Integer a, Integer b) {
				return a + b;
			}

			@Override
			public Integer neutral() {
				return 0;
			}
		});
		segTree.add(7);
	}
}
