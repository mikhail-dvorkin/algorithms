import java.util.*;

public class DataStructures {
	public static class FenwickTree {
		long[] t;
		int n;

		public FenwickTree(int n) {
			this.n = n;
			t = new long[n];
		}

		public void add(int i, long value) {
			for (; i < n; i += (i + 1) & -(i + 1)) {
				t[i] += value;
			}
		}

		public long sum(int i) {
			long res = 0;
			for (; i >= 0; i -= (i + 1) & -(i + 1)) {
				res += t[i];
			}
			return res;
		}
	}
	
	public static class SparseTable {
		int[][] min;
		int[][] max;
		
		public SparseTable(int[] a) {
			int n = a.length;
			int t = 1;
			while ((1 << t) <= n) {
				t++;
			}
			min = new int[t][n];
			max = new int[t][n];
			System.arraycopy(a, 0, min[0], 0, n);
			System.arraycopy(a, 0, max[0], 0, n);
			for (int j = 1; j < t; j++) {
				for (int i = 0; i + (1 << j) <= n; i++) {
					min[j][i] = Math.min(min[j - 1][i], min[j - 1][i + (1 << (j - 1))]);
					max[j][i] = Math.max(max[j - 1][i], max[j - 1][i + (1 << (j - 1))]);
				}
			}
		}
		
		public int min(int from, int to) {
			int j = Integer.SIZE - 1 - Integer.numberOfLeadingZeros(to - from);
			return Math.min(min[j][from], min[j][to - (1 << j)]);
		}
		
		public int max(int from, int to) {
			int j = Integer.SIZE - 1 - Integer.numberOfLeadingZeros(to - from);
			return Math.max(max[j][from], max[j][to - (1 << j)]);
		}
	}

	public static class QueueMin {
		StackMin a, b;

		public QueueMin(int maxSize) {
			a = new StackMin(maxSize);
			b = new StackMin(maxSize);
		}

		public void push(int v) {
			a.push(v);
		}

		public int min() {
			return Math.min(a.min(), b.min());
		}

		public int pop() {
			if (b.size() == 0) {
				while (a.size() > 0) {
					b.push(a.pop());
				}
			}
			return b.pop();
		}
	}

	public static class StackMin {
		int[] a;
		int[] min;
		int size;

		public StackMin(int maxSize) {
			a = new int[maxSize];
			min = new int[maxSize];
			size = 0;
		}

		public void push(int v) {
			a[size] = v;
			if (size == 0) {
				min[size] = v;
			} else {
				min[size] = Math.min(min[size - 1], v);
			}
			size++;
		}

		public int size() {
			return size;
		}

		public int pop() {
			size--;
			return a[size];
		}

		public int min() {
			if (size == 0) {
				return Integer.MAX_VALUE;
			}
			return min[size - 1];
		}
	}

	public static class DisjointSetUnion {
		int[] p;
		Random r = new Random(566);

		public DisjointSetUnion(int n) {
			p = new int[n];
			clear();
		}

		void clear() {
			for (int i = 0; i < p.length; i++) {
				p[i] = i;
			}
		}

		int get(int v) {
			if (p[v] == v) {
				return v;
			}
			p[v] = get(p[v]);
			return p[v];
		}

		void unite(int v, int u) {
			v = get(v);
			u = get(u);
			if (r.nextBoolean()) {
				p[v] = u;
			} else {
				p[u] = v;
			}
		}
	}
}