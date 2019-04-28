public class SegmentsTreeSimple {
	int n;
	int[] min;
	int[] max;
	int size;

	public SegmentsTreeSimple(int n) {
		this.n = n;
		size = 1;
		while (size <= n) {
			size *= 2;
		}
		min = new int[2 * size];
		max = new int[2 * size];
	}

	void set(int index, int value) {
		int i = size + index;
		min[i] = max[i] = value;
		while (i > 1) {
			i /= 2;
			min[i] = Math.min(min[2 * i], min[2 * i + 1]);
			max[i] = Math.max(max[2 * i], max[2 * i + 1]);
		}
	}

	int get(int index) {
		return min[size + index];
	}

	int getMax(int from, int to) {
		from += size;
		to += size;
		int res = Integer.MIN_VALUE;
		while (from < to) {
			if (from % 2 == 1) {
				res = Math.max(res, max[from]);
				from++;
			}
			if (to % 2 == 1) {
				to--;
				res = Math.max(res, max[to]);
			}
			from /= 2;
			to /= 2;
		}
		return res;
	}

	int getMin(int from, int to) {
		from += size;
		to += size;
		int res = Integer.MAX_VALUE;
		while (from < to) {
			if (from % 2 == 1) {
				res = Math.min(res, min[from]);
				from++;
			}
			if (to % 2 == 1) {
				to--;
				res = Math.min(res, min[to]);
			}
			from /= 2;
			to /= 2;
		}
		return res;
	}
}
