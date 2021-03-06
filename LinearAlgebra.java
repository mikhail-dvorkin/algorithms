import java.util.Random;

public class LinearAlgebra {
	final static double EPS = 1e-9;

	/**
	 * Modifies a and b!
	 */
	public static double[] gauss(double[][] a, double[] b) {
		int m = a.length;
		int n = a[0].length;
		int[] pos = new int[m];
		for (int i = 0; i < m; i++) {
			int s = 0;
			for (int j = 1; j < n; j++) {
				if (Math.abs(a[i][j]) > Math.abs(a[i][s])) {
					s = j;
				}
			}
			if (Math.abs(a[i][s]) < EPS) {
				pos[i] = -1;
				continue;
			}
			pos[i] = s;
			for (int k = 0; k < m; k++) {
				if (k == i) {
					continue;
				}
				double c = - a[k][s] / a[i][s];
				for (int j = 0; j < n; j++) {
					a[k][j] += c * a[i][j];
				}
				b[k] += c * b[i];
			}
		}
		double[] ans = new double[n];
		for (int i = 0; i < m; i++) {
			if (pos[i] == -1) {
				continue;
			}
			ans[pos[i]] = b[i] / a[i][pos[i]];
		}
		return ans;
	}

	public static int[][] matrixPower(int[][] a, long power, final int M) {
		if (power == 0) {
			int[][] res = new int[a.length][a.length];
			for (int i = 0; i < a.length; i++) {
				res[i][i] = 1;
			}
			return res;
		}
		if (power == 1) {
			return a;
		}
		if (power % 2 == 0) {
			int[][] b = matrixPower(a, power / 2, M);
			return matrixMultiply(b, b, M);
		}
		int[][] b = matrixPower(a, power - 1, M);
		return matrixMultiply(a, b, M);
	}

	public static int[][] matrixMultiply(int[][] a, int[][] b, final int M) {
		int[][] c = new int[a.length][b[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				for (int k = 0; k < b.length; k++) {
					c[i][j] = (int) ((c[i][j] + (long) a[i][k] * b[k][j]) % M);
				}
			}
		}
		return c;
	}

	static double[][] pca(double[][] x, int number, int iters, Random rnd) {
		int hei = x.length;
		int wid = x[0].length;
		double[][] coord = new double[number][hei];
		for (int c = 0; c < number; c++) {
			for (int j = 0; j < wid; j++) {
				double av = 0;
				for (int i = 0; i < hei; i++) {
					av += x[i][j];
				}
				av /= hei;
				for (int i = 0; i < hei; i++) {
					x[i][j] -= av;
				}
			}
			double[] r = new double[wid];
			for (int j = 0; j < wid; j++) {
				r[j] = rnd.nextGaussian();
			}
			for (int iter = 0; iter < iters; iter++) {
				double[] s = new double[wid];
				for (int i = 0; i < hei; i++) {
					double scal = 0;
					for (int j = 0; j < wid; j++) {
						scal += r[j] * x[i][j];
					}
					for (int j = 0; j < wid; j++) {
						s[j] += scal * x[i][j];
					}
				}
				double norm = 0;
				for (int j = 0; j < wid; j++) {
					norm += s[j] * s[j];
				}
				norm = Math.sqrt(norm);
				for (int j = 0; j < wid; j++) {
					r[j] = s[j] / norm;
				}
			}
			for (int i = 0; i < hei; i++) {
				double scal = 0;
				for (int j = 0; j < wid; j++) {
					scal += r[j] * x[i][j];
				}
				coord[c][i] = scal;
				for (int j = 0; j < wid; j++) {
					x[i][j] -= r[j] * scal;
				}
			}
		}
		return coord;
	}

}
