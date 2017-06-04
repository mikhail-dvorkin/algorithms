import java.util.*;

public class SegmentsTreeUpdates{
	static int empty = -1;
	static int initial = 0;
	
	static int H = 18;
	static int M = 1 << (H - 1);
	static int[] s = new int[2 * M];
	static {
		Arrays.fill(s, empty);
		s[1] = initial;
	}
	
	static int get(int x) {
		for (int h = H - 1; h >= 0; h--) {
			int y = x >> h;
			if (s[y] != empty) {
				return s[y];
			}
		}
		throw new RuntimeException();
	}
	
	static void update(int v, int from, int to, int value, int h) {
		int v1 = v << h;
		int v2 = ((v + 1) << h) - 1;
		if (from > v2 || to < v1) {
			return;
		}
		from = Math.max(from, v1);
		to = Math.min(to, v2);
		if (from == v1 && to == v2) {
			s[v] = value;
			return;
		}
		if (s[v] != empty) {
			s[2 * v] = s[v];
			s[2 * v + 1] = s[v];
		}
		s[v] = empty;
		update(2 * v, from, to, value, h - 1);
		update(2 * v + 1, from, to, value, h - 1);
	}
}
