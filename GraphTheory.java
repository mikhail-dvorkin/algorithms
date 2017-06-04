import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphTheory {
	int n, s, t;
	boolean[] mark;
	int[] tIn, fUp;
	int time;
	ArrayList<Integer>[] nei;
	int[][] c, f;
	
	public void dfsBridges(int v, int p) {
		mark[v] = true;
		tIn[v] = time;
		fUp[v] = time;
		time++;
		for (int u : nei[v]) {
			if (u == p) {
				continue;
			}
			if (mark[u]) {
				fUp[v] = Math.min(fUp[v], tIn[u]);
			} else {
				dfsBridges(u, v);
				fUp[v] = Math.min(fUp[v], fUp[u]);
				if (fUp[u] > tIn[v]) {
					
				}
			}
		}
	}
	
	public int[] eulerianCycle() {
		int edges = 0;
		for (ArrayList<Integer> list : nei) {
			edges += list.size();
		}
		edges /= 2;
		int[] stack = new int[edges + 1];
		int stackSize = 1;
		int[] tour = new int[edges + 1];
		int pos = 0;
		while (stackSize > 0) {
			int v = stack[stackSize - 1];
			if (nei[v].isEmpty()) {
				tour[pos++] = v;
				stackSize--;
			} else {
				int u = nei[v].get(nei[v].size() - 1);
				nei[v].remove(nei[v].size() - 1);
				nei[u].remove((Integer) v);
				stack[stackSize++] = u;
			}
		}
		return tour;
	}
	
	public static void floyd(int[][] e) {
		int n = e.length;
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (e[i][j] > e[i][k] + e[k][j]) {
						e[i][j] = e[i][k] + e[k][j];
					}
				}
			}
		}
	}
	
	static class TwoSat {
		static boolean[] solve(int[][] data, int n) {
			boolean[] ans = new boolean[n];
			@SuppressWarnings("unchecked")
			ArrayList<Integer>[] g = new ArrayList[2 * n];
			for (int i = 0; i < 2 * n; i++) {
				g[i] = new ArrayList<Integer>();
			}
			for (int[] clause : data) {
				g[clause[0] ^ 1].add(clause[1]);
				g[clause[1] ^ 1].add(clause[0]);
			}
			boolean[] mark = new boolean[2 * n];
			ArrayList<Integer> order = new ArrayList<Integer>();
			for (int i = 0; i < 2 * n; i++) {
				if (!mark[i]) {
					dfs1(i, mark, order, g);
				}
			}
			int[] comp = new int[2 * n];
			Arrays.fill(comp, -1);
			for (int i = 2 * n - 1, compNum = 0; i >= 0; i--) {
				int v = order.get(i);
				if (comp[v] == -1) {
					dfs2(v, compNum++, comp, g);
				}
			}
			for (int i = 0; i < n; i++) {
				if (comp[2 * i] == comp[2 * i + 1]) {
					return null;
				}
				ans[i] = comp[2 * i + 1] > comp[2 * i];
			}
			return ans;
		}
		
		static void dfs1(int v, boolean[] mark, ArrayList<Integer> order, ArrayList<Integer>[] g) {
			mark[v] = true;
			for (int u : g[v]) {
				if (!mark[u]) {
					dfs1(u, mark, order, g);
				}
			}
			order.add(v);
		}
		
		static void dfs2(int v, int compNum, int[] comp, ArrayList<Integer>[] g) {
			comp[v] = compNum;
			for (int u : g[v ^ 1]) {
				u ^= 1;
				if (comp[u] == -1) {
					dfs2(u, compNum, comp, g);
				}
			}
		}
	}

	
	public int fordFulkersonMatrix() {
		for (int ans = 0;;) {
			Arrays.fill(mark, false);
			if (dfsFordFulkersonMatrix(s)) {
				ans++;
				continue;
			}
			return ans;
		}
	}

	public boolean dfsFordFulkersonMatrix(int v) {
		if (v == t) {
			return true;
		}
		mark[v] = true;
		for (int u = 0; u < n; u++) {
			if (f[v][u] == c[v][u] || mark[u]) {
				continue;
			}
			if (dfsFordFulkersonMatrix(u)) {
				f[v][u]++;
				f[u][v]--;
				return true;
			}
		}
		return false;
	}
	
	static class FordFulkerson {
		Vertex s = new Vertex(), t = new Vertex();
		int curTime;
		
		static class Vertex {
			List<Edge> adj = new ArrayList<Edge>();
			int time;
		}
		
		static class Edge {
			Vertex from, to;
			int c, f;
			Edge rev;

			public Edge(Vertex from, Vertex to, int c) {
				this.from = from;
				this.to = to;
				this.c = c;
			}
		}
		
		static void addEdge(Vertex v, Vertex u, int c1, int c2) {
			Edge vu = new Edge(v, u, c1);
			Edge uv = new Edge(u, v, c2);
			vu.rev = uv;
			uv.rev = vu;
			v.adj.add(vu);
			u.adj.add(uv);
		}

		public int fordFulkerson() {
			for (int ans = 0;;) {
				curTime++;
				if (dfsFordFulkerson(s)) {
					ans++;
					continue;
				}
				return ans;
			}
		}
		
		public boolean dfsFordFulkerson(Vertex v) {
			if (v == t) {
				return true;
			}
			v.time = curTime;
			for (Edge e : v.adj) {
				Vertex u = e.to;
				if (e.f == e.c || u.time == curTime) {
					continue;
				}
				if (dfsFordFulkerson(u)) {
					e.f++;
					e.rev.f--;
					return true;
				}
			}
			return false;
		}
	}
	
	static class EdmonsKarp {
		public static long edmonsKarp(long[][] c, int s, int t) {
			int n = c.length;
			long[][] f = new long[n][n];
			int[] queue = new int[n];
			int[] prev = new int[n];
			long res = 0;
			for (;;) {
				queue[0] = s;
				int low = 0;
				int high = 1;
				Arrays.fill(prev, -1);
				prev[s] = s;
				while (low < high && prev[t] == -1) {
					int v = queue[low];
					low++;
					for (int u = 0; u < n; u++) {
						if (prev[u] != -1 || f[v][u] == c[v][u]) {
							continue;
						}
						prev[u] = v;
						queue[high] = u;
						high++;
					}
				}
				if (prev[t] == -1) {
					break;
				}
				long flow = Long.MAX_VALUE;
				for (int u = t; u != s; u = prev[u]) {
					flow = Math.min(flow, c[prev[u]][u] - f[prev[u]][u]);
				}
				for (int u = t; u != s; u = prev[u]) {
					f[prev[u]][u] += flow;
					f[u][prev[u]] -= flow;
				}
				res += flow;
			}
			return res;
		}
	}
}
