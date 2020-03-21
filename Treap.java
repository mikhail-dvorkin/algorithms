import java.util.*;

class Treap {
	static Random r = new Random(566);
	final int key;
	final int h;
	Treap left, right;
	final int value;

	int sum, minCumulativeSum;

	void calcMeta() {
		minCumulativeSum = 0;
		sum = 0;
		if (left != null) {
			sum = left.sum;
			minCumulativeSum = left.minCumulativeSum;
		}
		sum += value;
		minCumulativeSum = Math.min(minCumulativeSum, sum);
		if (right != null) {
			minCumulativeSum = Math.min(minCumulativeSum, sum + right.minCumulativeSum);
			sum += right.sum;
		}
	}

	Treap(int key, int value) {
		this.key = key;
		this.value = value;
		this.h = r.nextInt();
		calcMeta();
	}

	static Treap[] split(Treap treap, int keySplit) {
		if (treap == null) return new Treap[2];
		Treap[] split;
		if (treap.key < keySplit) {
			split = split(treap.right, keySplit);
			treap.right = split[0];
			split[0] = treap;
		} else {
			split = split(treap.left, keySplit);
			treap.left = split[1];
			split[1] = treap;
		}
		treap.calcMeta();
		return split;
	}

	static Treap merge(Treap left, Treap right) {
		if (left == null) return right;
		if (right == null) return left;
		if (left.h > right.h) {
			left.right = merge(left.right, right);
			left.calcMeta();
			return left;
		}
		right.left = merge(left, right.left);
		right.calcMeta();
		return right;
	}

	static Treap add(Treap treap, int key, int value) {
		Treap[] split = split(treap, key);
		return merge(split[0], merge(new Treap(key, value), split[1]));
	}
}

class PersistentTreap {
	static Random r = new Random(566);
	final int key;
	final int h;
	final PersistentTreap left, right;
	final int value;

	final int sum, minCumulativeSum;

	private PersistentTreap(int key, int value, int h, PersistentTreap left, PersistentTreap right) {
		this.key = key;
		this.value = value;
		this.h = h;
		this.left = left;
		this.right = right;

		// calcMeta
		int min = 0;
		int sum = 0;
		if (left != null) {
			sum = left.sum;
			min = left.minCumulativeSum;
		}
		sum += value;
		min = Math.min(min, sum);
		if (right != null) {
			min = Math.min(min, sum + right.minCumulativeSum);
			sum += right.sum;
		}
		this.sum = sum;
		this.minCumulativeSum = min;
	}

	PersistentTreap(int key, int value, PersistentTreap left, PersistentTreap right) {
		this(key, value, r.nextInt(), left, right);
	}

	static PersistentTreap[] split(PersistentTreap treap, int keySplit) {
		if (treap == null) return new PersistentTreap[2];
		PersistentTreap[] split;
		if (treap.key < keySplit) {
			split = split(treap.right, keySplit);
			split[0] = new PersistentTreap(treap.key, treap.value, treap.h, treap.left, split[0]);
		} else {
			split = split(treap.left, keySplit);
			split[1] = new PersistentTreap(treap.key, treap.value, treap.h, split[1], treap.right);
		}
		return split;
	}

	static PersistentTreap merge(PersistentTreap left, PersistentTreap right) {
		if (left == null) return right;
		if (right == null) return left;
		if (left.h > right.h) {
			return new PersistentTreap(left.key, left.value, left.h, left.left, merge(left.right, right));
		}
		return new PersistentTreap(right.key, right.value, right.h, merge(left, right.left), right.right);
	}

	static PersistentTreap add(PersistentTreap treap, int key, int value) {
		PersistentTreap[] split = split(treap, key);
		return merge(split[0], merge(new PersistentTreap(key, value, null, null), split[1]));
	}
}
