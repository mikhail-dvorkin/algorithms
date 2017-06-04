import java.util.*;

public class Matching {
	public int n;
	public boolean[][] e;
	
	private boolean[] mark;
	private int[] left;

	public int matching() {
		mark = new boolean[e.length];
		left = new int[e[0].length];
		Arrays.fill(left, -1);
		int ans = 0;
		for (int i = 0; i < e.length; i++) {
			Arrays.fill(mark, false);
			if (dfs(i)) {
				ans++;
			}
		}
		return ans;
	}

	private boolean dfs(int i) {
		if (mark[i]) {
			return false;
		}
		mark[i] = true;
		for (int j = 0; j < e[i].length; j++) {
			if (!e[i][j]) {
				continue;
			}
			if (left[j] == -1 || dfs(left[j])) {
				left[j] = i;
				return true;
			}
		}
		return false;
	}
	
	/*
	 * infty must be greater than all elements of e
	 */
	public static int[] hungarian(double[][] e) {
		int[] ans = new int[e.length];
		Arrays.fill(ans, -1);
		if (e.length == 0 || e[0].length == 0) {
			return ans;
		}
		double infty = 1e50;
		boolean swap = false;
		if (e.length > e[0].length) {
			swap = true;
			double[][] f = new double[e[0].length][e.length];
			for (int i = 0; i < e.length; i++) {
				for (int j = 0; j < e[0].length; j++) {
					f[j][i] = e[i][j];
				}
			}
			e = f;
		}
		int n1 = e.length;
		int n2 = e[0].length;
		double[] u = new double[n1 + 1];
		double[] v = new double[n2 + 1];
		int[] p = new int[n2 + 1];
		int[] way = new int[n2 + 1];
		for (int i = 1; i <= n1; i++) {
			p[0] = i;
			int j0 = 0;
			double[] minv = new double[n2 + 1]; 
			Arrays.fill(minv, infty);
			boolean[] used = new boolean[n2 + 1];
			do {
				used[j0] = true;
				int i0 = p[j0], j1 = 0;
				double delta = infty;
				for (int j = 1; j <= n2; j++) {
					if (!used[j]) {
						double cur = e[i0 - 1][j - 1] - u[i0] - v[j];
						if (cur < minv[j]) {
							minv[j] = cur;
							way[j] = j0;
						}
						if (minv[j] < delta) {
							delta = minv[j];
							j1 = j;
						}
					}
				}
				for (int j = 0; j <= n2; j++) {
					if (used[j]) {
						u[p[j]] += delta;
						v[j] -= delta;
					} else {
						minv[j] -= delta;
					}
				}
				j0 = j1;
			} while (p[j0] != 0);
			do {
				int j1 = way[j0];
				p[j0] = p[j1];
				j0 = j1;
			} while (j0 > 0);
		}
		for (int j = 1; j <= n2; j++) {
			if (p[j] > 0) {
				// if (e[p[j] - 1][j - 1] >= infty) no matching of size n1;
				// sum += e[p[j] - 1][j - 1];
				if (swap) {
					ans[j - 1] = p[j] - 1;
				} else {
					ans[p[j] - 1] = j - 1;
				}
			}
		}
		return ans;
	}
}
