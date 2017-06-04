import java.util.Arrays;

public class StringUtils {
	public static String reverse(String s) {
		int len = s.length();
		char[] t = new char[len];
		for (int i = 0; i < len; i++)
			t[i] = s.charAt(len - 1 - i);
		return new String(t);
	}
	
	public static boolean isPalindrome(String s) {
		int len = s.length();
		for (int i = 0; i < len / 2; i++)
			if (s.charAt(i) != s.charAt(len - 1 - i))
				return false;
		return true;
	}
	
	public static int[] kmp(String s) {
		int n = s.length();
		int[] p = new int[n + 1];
		p[0] = -1;
		for (int i = 0; i < n; i++) {
			int k = p[i];
			char c = s.charAt(i);
			while ((k >= 0) && (s.charAt(k) != c)) {
				k = p[k];
			}
			p[i + 1] = k + 1;
		}
		return p;
	}
	
	static class Hashing {
		final int X = 566239;
		final int M = 1000000007;
		
		final int[] h;
		final int[] t;

		public Hashing(String s) {
			int n = s.length();
			h = new int[n + 1];
			t = new int[n + 1];
			t[0] = 1;
			for (int i = 0; i < n; i++) {
				t[i + 1] = (int) ((t[i] * (long) X) % M);
				h[i + 1] = (int) ((h[i] * (long) X + s.charAt(i)) % M);
			}
		}

		public int hash(int from, int to) {
			int res = (int) ((h[to] - h[from] * (long) t[to - from]) % M);
			if (res < 0) {
				res += M;
			}
			return res;
		}
	}
	
	/**
	 * @author andrewzta and svembox
	 * @param s
	 * @return z-function of s
	 */
	public static int[] z(String s) {
		int n = s.length();
		int[] z = new int[n];
		for (int i = 1, l = 0, r = 1; i < n; i++, r = Math.max(i, r)) {
			for (z[i] = Math.min(r - i, z[i - l]); i + z[i] < n && s.charAt(i + z[i]) == s.charAt(z[i]); z[i]++, r = i + z[i], l = i) {
				// enjoy
			}
		}
		return z;
	}
	
	static class Ukkonen {
		int[][] t;
		int[] l;
		int[] r;
		int[] p;
		int[] s;
		
		Ukkonen(String a) {
			int n = a.length();
			t = new int[3 * n + 10][27];
			l = new int[3 * n + 10];
			r = new int[3 * n + 10];
			p = new int[3 * n + 10];
			s = new int[3 * n + 10];
			for (int i = 0; i < t.length; i++) {
				Arrays.fill(t[i], -1);
			}
			Arrays.fill(t[1], 0);
			Arrays.fill(r, n - 1);
			int ts = 2;
			s[0] = 1;
			l[0] = -1;
			r[0] = -1;
			l[1] = -1;
			r[1] = -1;
			int tv = 0;
			int tp = 0;
			for (int i = 0; i < n; i++) {
				int c = a.charAt(i) - 'a';
				for (;;) {
					if (r[tv] < tp) {
						if (t[tv][c] == -1) {
							t[tv][c] = ts;
							l[ts] = i;
							p[ts++] = tv;
							tv = s[tv];
							tp = r[tv] + 1;
							continue;
						}
						tv = t[tv][c];
						tp = l[tv];
					}
					if (tp == -1 || c == a.charAt(tp) - 'a') {
						tp++;
					} else {
						l[ts + 1] = i;
						p[ts + 1] = ts;
						l[ts] = l[tv];
						r[ts] = tp - 1;
						p[ts] = p[tv];
						t[ts][c] = ts + 1;
						t[ts][a.charAt(tp) - 'a'] = tv;
						l[tv] = tp;
						p[tv] = ts;
						t[p[ts]][a.charAt(l[ts]) - 'a'] = ts;
						ts += 2;
						tv = s[p[ts - 2]];
						tp = l[ts - 2];
						while (tp <= r[ts - 2]) {
							tv = t[tv][a.charAt(tp) - 'a'];
							tp += r[tv] - l[tv] + 1;
						}
						if (tp == r[ts - 2] + 1) {
							s[ts - 2] = tv;
						} else {
							s[ts - 2] = ts;
						} 
						tp = r[tv] - tp + r[ts - 2] + 2;
						continue;
					}
					break;
				}
			}
			t = Arrays.copyOf(t, ts);
			l = Arrays.copyOf(l, ts);
			r = Arrays.copyOf(r, ts);
			p = Arrays.copyOf(p, ts);
		}
	}
}
