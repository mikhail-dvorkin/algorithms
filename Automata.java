import java.io.*;
import java.util.*;

public class Automata implements Runnable {
	static String alphabetString;

	static class RegualarExpression {
		final static int OP_LETTER = 1;
		final static int OP_EMPTY_SET = 2;
		final static int OP_EPS = 3;
		final static int OP_KLEENE_STAR = 4;
		final static int OP_CONCAT = 5;
		final static int OP_UNION = 6;
		
		int type;
		RegualarExpression x, y;
		int value;
		
		RegualarExpression(int value) {
			this.type = OP_LETTER;
			this.x = null;
			this.y = null;
			this.value = value;
		}

		RegualarExpression(int type, RegualarExpression x, RegualarExpression y) {
			this.type = type;
			this.x = x;
			this.y = y;
		}
		
		public static RegualarExpression stupidParse(String s) {
			int len = s.length();
			RegualarExpression[][] re = new RegualarExpression[len][len + 1];
			for (int ij = 1; ij <= len; ij++) {
				loop:
				for (int i = 0;; i++) {
					int j = i + ij;
					if (j > len)
						break;
					char c = s.charAt(i);
					char d = s.charAt(j - 1);
					if (ij == 1) {
						if (alphabetString.indexOf(c) >= 0) {
							re[i][j] = new RegualarExpression(alphabetString.indexOf(c));
						} else 	if (c == '@') {
							re[i][j] = new RegualarExpression(RegualarExpression.OP_EMPTY_SET, null, null);
						} else 	if (c == 'e' || c == '$') {
							re[i][j] = new RegualarExpression(RegualarExpression.OP_EPS, null, null);
						}
						continue loop;
					}
					if (c == '(' && d == ')' && re[i + 1][j - 1] != null) {
						re[i][j] = re[i + 1][j - 1];
						continue loop;
					}
					for (int k = i + 1; k + 1 < j; k++) {
						if (s.charAt(k) == '|' && re[i][k] != null && re[k + 1][j] != null) {
							re[i][j] = new RegualarExpression(RegualarExpression.OP_UNION, re[i][k], re[k + 1][j]);
							continue loop;
						}
					}
					for (int k = i + 1; k < j; k++) {
						if (re[i][k] != null && re[k][j] != null) {
							re[i][j] = new RegualarExpression(RegualarExpression.OP_CONCAT, re[i][k], re[k][j]);
							continue loop;
						}
					}
					if (d == '*' && re[i][j - 1] != null) {
						re[i][j] = new RegualarExpression(RegualarExpression.OP_KLEENE_STAR, re[i][j - 1], null);
						continue loop;
					}
					re[i][j] = null;
				}
			}
			return re[0][len];
		}

		EpsNFA toEpsNFA() {
			EpsNFA a = new EpsNFA();
			a.alphabet = alphabetString.length();
			a.init = 0;
			if (type == OP_LETTER) {
				a.n = 2;
				a.sigma = new int[a.n][a.alphabet + 1][0];
				a.sigma[0][value] = new int[]{1};
			}
			if (type == OP_EMPTY_SET) {
				a.n = 2;
				a.sigma = new int[a.n][a.alphabet + 1][0];
			}
			if (type == OP_EPS) {
				a.n = 2;
				a.sigma = new int[a.n][a.alphabet + 1][0];
				a.sigma[0][a.alphabet] = new int[]{1};
			}
			if (type == OP_KLEENE_STAR) {
				EpsNFA b = x.toEpsNFA();
				a.n = b.n + 2;
				a.sigma = new int[a.n][a.alphabet + 1][];
				for (int i = 0; i < b.n; i++) {
					for (int c = 0; c <= a.alphabet; c++) {
						a.sigma[i + 1][c] = b.sigma[i][c].clone();
						for (int k = 0; k < b.sigma[i][c].length; k++) {
							a.sigma[i + 1][c][k]++;
						}
					}
				}
				a.sigma[0] = new int[a.alphabet + 1][0];
				a.sigma[a.n - 1] = new int[a.alphabet + 1][0];
				a.sigma[0][a.alphabet] = new int[]{1};
				a.sigma[b.n][a.alphabet] = new int[]{1};
				a.sigma[1][a.alphabet] = Arrays.copyOf(a.sigma[1][a.alphabet], a.sigma[1][a.alphabet].length + 1);
				a.sigma[1][a.alphabet][a.sigma[1][a.alphabet].length - 1] = a.n - 1;
			}
			if (type == OP_CONCAT) {
				EpsNFA left = x.toEpsNFA();
				EpsNFA right = y.toEpsNFA();
				a.n = left.n + right.n - 1;
				a.sigma = new int[a.n][a.alphabet + 1][];
				for (int i = 0; i < left.n; i++) {
					for (int c = 0; c <= a.alphabet; c++) {
						a.sigma[i][c] = left.sigma[i][c].clone();
					}
				}
				for (int i = 0; i < right.n; i++) {
					for (int c = 0; c <= a.alphabet; c++) {
						a.sigma[i + left.n - 1][c] = right.sigma[i][c].clone();
						for (int k = 0; k < right.sigma[i][c].length; k++) {
							a.sigma[i + left.n - 1][c][k] += left.n - 1;
						}
					}
				}
			}
			if (type == OP_UNION) {
				EpsNFA first = x.toEpsNFA();
				EpsNFA second = y.toEpsNFA();
				a.n = first.n + second.n - 2;
				a.sigma = new int[a.n][a.alphabet + 1][];
				for (int i = 0; i < first.n - 1; i++) {
					for (int c = 0; c <= a.alphabet; c++) {
						a.sigma[i][c] = first.sigma[i][c].clone();
						for (int k = 0; k < a.sigma[i][c].length; k++) {
							if (a.sigma[i][c][k] == first.n - 1) {
								a.sigma[i][c][k] = a.n - 1;
							}
						}
					}
				}
				for (int i = 1; i < second.n - 1; i++) {
					for (int c = 0; c <= a.alphabet; c++) {
						a.sigma[i + first.n - 2][c] = second.sigma[i][c].clone();
						for (int k = 0; k < second.sigma[i][c].length; k++) {
							a.sigma[i + first.n - 2][c][k] += first.n - 2;
						}
					}
				}
				for (int c = 0; c <= a.alphabet; c++) {
					int len = a.sigma[0][c].length;
					a.sigma[0][c] = Arrays.copyOf(a.sigma[0][c], len + second.sigma[0][c].length);
					for (int i = 0; i < second.sigma[0][c].length; i++) {
						a.sigma[0][c][len + i] = second.sigma[0][c][i] + first.n - 2;
					}
				}
				a.sigma[a.n - 1] = new int[a.alphabet + 1][0];
			}
			a.terminal = new boolean[a.n];
			a.terminal[a.n - 1] = true;
			return a;
		}
	}
	
	static class EpsNFA {
		int alphabet;
		int n;
		boolean[] terminal;
		int init;
		int[][][] sigma;
		
		int[][] eps;
		boolean[] mark;
		
		public EpsNFA() {
		}

		public boolean accepts(String s) {
			mark = new boolean[n];
			dfsEps(init);
			boolean[] cur = mark.clone();
			for (char c : s.toCharArray()) {
				boolean[] newCur = new boolean[n];
				for (int i = 0; i < n; i++) {
					if (!cur[i]) {
						continue;
					}
					for (int j : sigma[i][alphabetString.indexOf(c)]) {
						newCur[j] = true;
					}
				}
				cur = newCur;
				for (int i = 0; i < n; i++) {
					if (!cur[i]) {
						continue;
					}
					mark = new boolean[n];
					dfsEps(i);
					for (int j = 0; j < n; j++) {
						cur[j] |= mark[j];
					}
				}
			}
			for (int i = 0; i < n; i++) {
				if (cur[i] && terminal[i])
					return true;
			}
			return false;
		}
		
		private void epsClosure() {
			eps = new int[n][];
			for (int i = 0; i < n; i++) {
				mark = new boolean[n];
				dfsEps(i);
				int reach = 0;
				for (int j = 0; j < n; j++) {
					if (mark[j]) {
						reach++;
					}
				}
				eps[i] = new int[reach];
				int k = 0;
				for (int j = 0; j < n; j++) {
					if (mark[j]) {
						eps[i][k++] = j;
					}
				}
			}
		}

		private void dfsEps(int v) {
			if (mark[v])
				return;
			mark[v] = true;
			for (int u : sigma[v][alphabet]) {
				dfsEps(u);
			}
		}
		
		private void dfs(int v) {
			if (mark[v])
				return;
			mark[v] = true;
			for (int c = 0; c <= alphabet; c++) {
				for (int u : sigma[v][c]) {
					dfs(u);
				}
			}
		}
		
		public EpsNFA removeUnreachable() {
			mark = new boolean[n];
			dfs(init);
			int[] newNumber = new int[n];
			EpsNFA a = new EpsNFA();
			a.terminal = new boolean[n];
			for (int i = 0; i < n; i++) {
				if (!mark[i]) {
					continue;
				}
				newNumber[i] = a.n;
				if (terminal[i]) {
					a.terminal[a.n] = true; 
				}
				a.n++;
			}
			a.alphabet = alphabet;
			a.init = newNumber[init];
			a.terminal = Arrays.copyOf(a.terminal, a.n);
			a.sigma = new int[a.n][alphabet + 1][];
			for (int i = 0; i < n; i++) {
				if (!mark[i]) {
					continue;
				}
				int ni = newNumber[i];
				for (int c = 0; c <= alphabet; c++) {
					a.sigma[ni][c] = sigma[i][c].clone();
					for (int k = 0; k < a.sigma[ni][c].length; k++) {
						a.sigma[ni][c][k] = newNumber[a.sigma[ni][c][k]];
					}
				}
			}
			return a;
		}
		
		public EpsNFA prohibitEmpty() {
			EpsNFA a = new EpsNFA();
			a.alphabet = alphabet;
			a.n = n + 1;
			a.init = n;
			a.terminal = Arrays.copyOf(terminal, a.n);
			a.sigma = new int[a.n][alphabet + 1][];
			for (int i = 0; i < n; i++) {
				for (int c = 0; c <= alphabet; c++) {
					a.sigma[i][c] = sigma[i][c].clone();
				}
			}
			a.sigma[n] = new int[alphabet + 1][0];
			mark = new boolean[n];
			dfsEps(init);
			@SuppressWarnings("unchecked")
			Set<Integer>[] s = new Set[alphabet];
			for (int c = 0; c < alphabet; c++) {
				s[c] = new TreeSet<Integer>();
			}
			for (int i = 0; i < n; i++) {
				if (!mark[i]) {
					continue;
				}
				for (int c = 0; c < alphabet; c++) {
					for (int p : sigma[i][c]) {
						s[c].add(p);
					}
				}
			}
			for (int c = 0; c < alphabet; c++) {
				a.sigma[n][c] = new int[s[c].size()];
				int pos = 0;
				for (int p : s[c]) {
					a.sigma[n][c][pos] = p;
					pos++;
				}
			}
			return a;
		}
		
		public DFA determinize() {
			epsClosure();
			DFA a = new DFA();
			
			BitSet m0 = new BitSet();
			for (int x : eps[init])
				m0.set(x);
			Map<BitSet, Integer> masks = new HashMap<BitSet, Integer>();
			List<Integer> terms = new ArrayList<Integer>();
			List<BitSet> queue = new ArrayList<BitSet>();
			List<int[]> sig = new ArrayList<int[]>();
			masks.put(m0, 0);
			queue.add(m0);
			boolean[] set = new boolean[n];
			boolean[] next = new boolean[n];
			for (int k = 0; k < queue.size(); k++) {
				BitSet mask = queue.get(k);
				sig.add(new int[alphabet]);
				for (int c = 0; c < alphabet; c++) {
					for (int i = 0; i < n; i++) {
						set[i] = mask.get(i);
						if (set[i] && terminal[i])
							terms.add(k);
					}
					Arrays.fill(next, false);
					for (int i = 0; i < n; i++) {
						if (!set[i])
							continue;
						for (int j : sigma[i][c]) {
							for (int kk : eps[j])
								next[kk] = true;
						}
					}
					BitSet nxt = new BitSet();
					for (int i = 0; i < n; i++) {
						if (next[i]) {
							nxt.set(i);
						}
					}
					if (!masks.containsKey(nxt)) {
						masks.put(nxt, masks.size());
						queue.add(nxt);
					}
					sig.get(k)[c] = masks.get(nxt);
				}
			}
			
			a.alphabet = alphabet;
			a.n = masks.size();
			a.init = 0;
			a.terminal = new boolean[a.n];
			for (int k : terms)
				a.terminal[k] = true;
			a.sigma = new int[a.n][];
			for (int i = 0; i < a.n; i++) {
				a.sigma[i] = sig.get(i);
			}
			return a.minimize();
		}
		
		/**
		 * Accepts word that both of this and that accept.
		 */
		EpsNFA multiply(EpsNFA that) {
			if (alphabet != that.alphabet) {
				throw new IllegalArgumentException();
			}
			EpsNFA result = new EpsNFA();
			result.alphabet = alphabet;
			result.n = n * that.n;
			result.init = init + n * that.init;
			result.terminal = new boolean[result.n];
			result.sigma = new int[result.n][result.alphabet + 1][0];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < that.n; j++) {
					int k = i + n * j;
					result.terminal[k] = terminal[i] && that.terminal[j];
					for (int c = 0; c < alphabet; c++) {
						int[] byLetter = new int[sigma[i][c].length * that.sigma[j][c].length];
						int pos = 0;
						for (int p : sigma[i][c]) {
							for (int q : that.sigma[j][c]) {
								byLetter[pos] = p + n * q;
								pos++;
							}
						}
						result.sigma[k][c] = byLetter;
					}
					int[] byEps = new int[sigma[i][alphabet].length + that.sigma[j][alphabet].length];
					int pos = 0;
					for (int p : sigma[i][alphabet]) {
						byEps[pos] = p + n * j;
						pos++;
					}
					for (int p : that.sigma[j][alphabet]) {
						byEps[pos] = i + n * p;
						pos++;
					}
					result.sigma[k][alphabet] = byEps;
				}
			}
			return result; //.removeUnreachable();
		}
		
		int[] shortestAccepted() {
			int[] fromLetterCount = new int[n];
			int[] fromEpsCount = new int[n];
			for (int i = 0; i < n; i++) {
				for (int c = 0; c <= alphabet; c++) {
					for (int p : sigma[i][c]) {
						if (c < alphabet) {
							fromLetterCount[p]++;
						} else {
							fromEpsCount[p]++;
						}
					}
				}
			}
			int[][] fromLetter = new int[n][];
			int[][] fromEps = new int[n][];
			for (int i = 0; i < n; i++) {
				fromEps[i] = new int[fromEpsCount[i]];
				fromLetter[i] = new int[fromLetterCount[i]];
			}
			for (int i = 0; i < n; i++) {
				for (int c = 0; c <= alphabet; c++) {
					for (int p : sigma[i][c]) {
						if (c < alphabet) {
							fromLetterCount[p]--;
							fromLetter[p][fromLetterCount[p]] = i;
						} else {
							fromEpsCount[p]--;
							fromEps[p][fromEpsCount[p]] = i;
						}
					}
				}
			}
			int[] dist = new int[n];
			int[] howVertex = new int[n];
			Arrays.fill(dist, Integer.MAX_VALUE);
			int[] queue = new int[2 * n + 1];
			int low = n;
			int high = n;
			for (int i = 0; i < n; i++) {
				if (terminal[i]) {
					queue[high] = i;
					dist[i] = 0;
					howVertex[i] = -1;
					high++;
				}
			}
			while (low < high) {
				int v = queue[low];
				if (v == init) {
					break;
				}
				low++;
				int d = dist[v];
				for (int u : fromLetter[v]) {
					if (d + 1 < dist[u]) {
						dist[u] = d + 1;
						howVertex[u] = v;
						queue[high] = u;
						high++;
					}
				}
				for (int u : fromEps[v]) {
					if (d < dist[u]) {
						dist[u] = d;
						howVertex[u] = v;
						low--;
						queue[low] = u;
					}
				}
			}
			if (dist[init] == Integer.MAX_VALUE) {
				return null;
			}
			int[] ans = new int[dist[init]];
			int pos = 0;
			int v = init;
			for (;;) {
				int u = howVertex[v];
				if (u == -1) {
					break;
				}
				if (dist[u] != dist[v]) {
					int letter = -1;
					for (int c = 0; c < alphabet; c++) {
						for (int w : sigma[v][c]) {
							if (w == u) {
								letter = c;
							}
						}
					}
					ans[pos] = letter;
					pos++;
				}
				v = u;
			}
			return ans;
		}
	}
	
	static class DFA {
		int alphabet;
		int n;
		boolean[] terminal;
		int init;
		int[][] sigma;
		
		public DFA() {
		}
		
		public DFA(Scanner in) {
			alphabet = in.next().length();
			n = in.nextInt();
			init = in.nextInt() - 1;
			terminal = new boolean[n];
			for (int i = in.nextInt(); i > 0; i--) {
				terminal[in.nextInt() - 1] = true;
			}
			sigma = new int[n][alphabet];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < alphabet; j++) {
					sigma[i][j] = in.nextInt() - 1;
				}
			}
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(n);
			sb.append("\n");
			sb.append(init + 1);
			sb.append(" ");
			int terms = 0;
			for (int i = 0; i < n; i++) {
				if (terminal[i])
					terms++;
			}
			sb.append(terms);
			for (int i = 0; i < n; i++) {
				if (terminal[i]) {
					sb.append(" ");
					sb.append(i + 1);
				}
			}
			sb.append("\n");
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < alphabet; j++) {
					if (j > 0)
						sb.append(" ");
					sb.append(sigma[i][j] + 1);
				}
				sb.append("\n");
			}
			return sb.toString();
		}

//		@SuppressWarnings("unchecked")
//		EpsNFA asEpsNFAWithFrame() {
//			EpsNFA a = new EpsNFA();
//			a.alphabet = alphabet;
//			a.n = n + 2;
//			a.init = 0;
//			a.terminal = new boolean[a.n];
//			a.terminal[a.n - 1] = true;
//			a.sigma = new ArrayList[a.n][a.alphabet + 1];
//			for (int i = 0; i <= a.alphabet; i++) {
//				for (int j = 0; j < a.n; j++) {
//					a.sigma[j][i] = new ArrayList<Integer>();
//				}
//			}
//			for (int i = 0; i < n; i++) {
//				for (int j = 0; j < alphabet; j++) {
//					a.sigma[i + 1][j].add(sigma[i][j] + 1);
//				}
//				if (terminal[i]) {
//					a.sigma[i + 1][a.alphabet].add(a.n - 1);
//				}
//			}
//			a.sigma[0][a.alphabet].add(init + 1);
//			return a;
//		}

		EpsNFA asEpsNFA() {
			EpsNFA a = new EpsNFA();
			a.alphabet = alphabet;
			a.n = n;
			a.init = init;
			a.terminal = terminal.clone();
			a.sigma = new int[n][alphabet + 1][1];
			for (int i = 0; i < n; i++) {
				for (int c = 0; c < alphabet; c++) {
					a.sigma[i][c][0] = sigma[i][c];
				}
				a.sigma[i][alphabet] = new int[0];
			}
			return a;
		}
		
		private ArrayList<Integer> queueA;
		private ArrayList<Integer> queueB;
		private boolean[][] diff;
		private boolean[] needed;

		public DFA minimize() {
			DFA a = new DFA();
			a.alphabet = alphabet;
			@SuppressWarnings("unchecked")
			ArrayList<Integer>[][] from = new ArrayList[n][alphabet];
			queueA = new ArrayList<Integer>();
			queueB = new ArrayList<Integer>();
			diff = new boolean[n][n];
			needed = new boolean[n];
			dfs(init);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < alphabet; j++) {
					from[i][j] = new ArrayList<Integer>();
				}
			}
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < alphabet; j++) {
					from[sigma[i][j]][j].add(i);
				}
			}
			for (int i = 0; i < n; i++) {
				if (!needed[i])
					continue;
				for (int j = 0; j < n; j++) {
					if (!needed[j])
						continue;
					if (terminal[i] != terminal[j]) {
						queueA.add(i);
						queueB.add(j);
						diff[i][j] = true;
					}
				}
			}
			for (int cur = 0; cur < queueA.size(); cur++) {
				int x = queueA.get(cur);
				int y = queueB.get(cur);
				for (int c = 0; c < alphabet; c++) {
					for (int xx : from[x][c]) {
						if (!needed[xx])
							continue;
						for (int yy : from[y][c]) {
							if (!needed[yy])
								continue;
							if (!diff[xx][yy]) {
								queueA.add(xx);
								queueB.add(yy);
								diff[xx][yy] = true;
							}
						}
					}
				}
			}
			int[] newname = new int[n];
			boolean[] main = new boolean[n];
			for (int i = 0; i < n; i++) {
				if (!needed[i])
					continue;
				boolean good = true;
				for (int j = 0; j < i; j++) {
					if (!needed[j])
						continue;
					if (!diff[i][j]) {
						good = false;
						newname[i] = newname[j];
						break;
					}
				}
				if (good) {
					newname[i] = a.n++;
					main[i] = true;
				}
			}
			a.terminal = new boolean[a.n];
			for (int i = 0; i < n; i++) {
				if (!needed[i])
					continue;
				if (terminal[i])
					a.terminal[newname[i]] = true;
			}
			a.init = newname[init];
			a.sigma = new int[a.n][a.alphabet];
			for (int i = 0; i < n; i++) {
				if (!needed[i])
					continue;
				 for (int c = 0; c < a.alphabet; c++) {
					 a.sigma[newname[i]][c] = newname[sigma[i][c]];
				 }
			}
			return a;
		}
		
		public DFA negate() {
			DFA neg = clone();
			for (int i = 0; i < terminal.length; i++) {
				neg.terminal[i] ^= true;
			}
			return neg;
		}
		
		@Override
		public DFA clone() {
			DFA clone = new DFA();
			clone.init = init;
			clone.n = n;
			clone.alphabet = alphabet;
			clone.terminal = terminal.clone();
			clone.sigma = new int[sigma.length][];
			for (int i = 0; i < sigma.length; i++) {
				clone.sigma[i] = sigma[i].clone();
			}
			return clone;
		}
		
		public long[][] perStateStringCount(int maxLength) {
			long[][] count = new long[maxLength + 1][n];
			count[0][init] = 1;
			for (int len = 0; len < maxLength; len++) {
				for (int v = 0; v < n; v++) {
					for (int c = 0; c < alphabet; c++) {
						count[len + 1][sigma[v][c]] += count[len][v];
					}
				}
			}
			return count;
		}
		
		public long[] acceptedStringCount(int maxLength) {
			long[][] count = perStateStringCount(maxLength);
			long[] res = new long[maxLength + 1];
			for (int len = 0; len <= maxLength; len++) {
				for (int v = 0; v < n; v++) {
					if (terminal[v]) {
						res[len] += count[len][v];
					}
				}
			}
			return res;
		}
		
		public int[] shortestAccepted() {
			return asEpsNFA().shortestAccepted();
		}

		private void dfs(int v) {
			if (needed[v])
				return;
			needed[v] = true;
			for (int c = 0; c < alphabet; c++) {
				dfs(sigma[v][c]);
			}
		}

		public EpsNFA reverse() {
			EpsNFA a = new EpsNFA();
			a.alphabet = alphabet;
			a.n = n + 1;
			a.init = n;
			a.terminal = new boolean[n + 1];
			a.terminal[init] = true;
			@SuppressWarnings("unchecked")
			ArrayList<Integer>[][] s = new ArrayList[n + 1][alphabet + 1];
			for (int i = 0; i < n + 1; i++) {
				for (int c = 0; c <= alphabet; c++) {
					s[i][c] = new ArrayList<Integer>();
				}
			}
			for (int i = 0; i < n; i++) {
				for (int c = 0; c < alphabet; c++) {
					s[sigma[i][c]][c].add(i);
				}
				if (terminal[i]) {
					s[n][alphabet].add(i);
				}
			}
			for (int i = 0; i < a.n; i++) {
				for (int c = 0; c <= alphabet; c++) {
					a.sigma[i][c] = new int[s[i][c].size()];
					for (int k = 0; k < a.sigma[i][c].length; k++) {
						a.sigma[i][c][k] = s[i][c].get(k);
					}
				}
			}
			return a;
		}
	}

	private static Scanner in;
	private static PrintWriter out;
	
	@Override
	public void run() {
		alphabetString = "";
		for (char c = 'a'; c <= 'z'; c++) {
			alphabetString += c;
		}
//		String stringA = "a***************************************************************************************************";
//		String stringB = "a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*a*";
		String stringA = in.nextLine();
		String stringB = in.nextLine();
		RegualarExpression regA = RegualarExpression.stupidParse(stringA);
		RegualarExpression regB = RegualarExpression.stupidParse(stringB);
		EpsNFA autoA = regA.toEpsNFA();
		EpsNFA autoB = regB.toEpsNFA();
		EpsNFA autoAandB = autoA.multiply(autoB);
		EpsNFA autoAandBnonempty = autoAandB.prohibitEmpty();
		int[] shortest = autoAandBnonempty.shortestAccepted();
		if (shortest == null) {
			out.println("Correct");
			return;
		}
		out.println("Wrong");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < shortest.length; i++) {
			sb.append(alphabetString.charAt(shortest[i]));
		}
		out.println(sb.toString());
	}

	public static void main(String[] args) {
		in = new Scanner(System.in);
		out = new PrintWriter(System.out);
		new Automata().run();
		in.close();
		out.close();
	}
}
