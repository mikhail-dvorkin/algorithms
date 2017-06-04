import java.util.*;

public class Combinatorics {
	public static Iterable<int[]> combinations(int n, int k) {
		if (k > n || n < 0 || k < 0) {
			throw new IllegalArgumentException("n = " + n + ", k = " + k);
		}
		if (k == 0) {
			return Collections.singleton(new int[0]);
		}
		return new CombinationsIterable(n, k);
	}

	static class CombinationsIterable implements Iterable<int[]> {
		int n, k;
		
		public CombinationsIterable(int n, int k) {
			this.n = n;
			this.k = k;
		}
		
		@Override
		public Iterator<int[]> iterator() {
			return new CombintationsIterator();
		}

		class CombintationsIterator implements Iterator<int[]> {
			int[] a;

			public CombintationsIterator() {
				this.a = new int[k];
				for (int i = 0; i < k; i++) {
					a[i] = i;
				}
				a[k - 1]--;
			}

			@Override
			public boolean hasNext() {
				for (int i = k - 1; i >= 0; i--) {
					if (a[i] != n - k + i) {
						return true;
					}
				}
				return false;
			}

			@Override
			public int[] next() {
				int i = k - 1;
				while (i >= 0 && a[i] == n - k + i) {
					i--;
				}
				if (i < 0) {
					throw new NoSuchElementException();
				}
				a[i]++;
				for (i++; i < k; i++) {
					a[i] = a[i - 1] + 1;
				}
				return a.clone();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();				
			}
		}
	}

	public static void main(String[] args) {
		for (int[] a : combinations(5, 0)) {
			System.out.println(Arrays.toString(a));
		}
	}
}
