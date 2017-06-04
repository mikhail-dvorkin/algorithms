import java.util.*;

public class Geometry {
	/**
	 * Convex polygon
	 * 
	 * n = x.length - 1
	 * x[n] == x[0]
	 */
	static void sliceWithEachIntegerLine(int[] x, int[] y) {
		int n = x.length - 1;
		int xmin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			xmin = Math.min(xmin, x[i]);
			xmax = Math.max(xmax, x[i]);
		}
		int i = 0;
		while (x[i] != xmin) {
			i++;
		}
		int j = i;
		if (j == 0) j = n;
		double slope1 = (y[i + 1] - y[i]) * 1.0 / (x[i + 1] - x[i]);
		double slope2 = (y[j - 1] - y[j]) * 1.0 / (x[j - 1] - x[j]);
		for (int t = xmin; t <= xmax; t++) {
			if (t < xmax) {
				while (x[i + 1] <= t) {
					i++;
					if (i == n) {
						i = 0;
					}
					slope1 = (y[i + 1] - y[i]) * 1.0 / (x[i + 1] - x[i]);
				}
				while (x[j - 1] <= t) {
					j--;
					if (j == 0) {
						j = n;
					}
					slope2 = (y[j - 1] - y[j]) * 1.0 / (x[j - 1] - x[j]);
				}
			}
			double y1 = y[i] + (t - x[i]) * slope1;
			double y2 = y[j] + (t - x[j]) * slope2;
			if (y1 > y2) {
				double p = y1; y1 = y2; y2 = p;
			}
			double yFrom = Math.ceil(y1);
			double yTo = Math.floor(y2) + 1;
			System.err.println(yFrom + ".." + yTo);
		}
	}
	
	static void sortByPolarAngle(Point[] array, final Point origin) {
		Arrays.sort(array, new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				int qA = quarterOrAxis(a.x - origin.x, a.y - origin.y);
				int qB = quarterOrAxis(b.x - origin.x, b.y - origin.y);
				int res = Integer.compare(qA, qB);
				if (res == 0) {
					res = orientation(origin, b, a);
				}
				if (res != 0) {
					return res;
				}
				return Long.compare(a.x * 1L * a.x + a.y * 1L * a.y, b.x * 1L * b.x + b.y * 1L * b.y);
			}
		});
	}
	
	static void scan180DegreeSpans(Point[] array, Point origin) {
		sortByPolarAngle(array, origin);
		for (int j = 0, k = 0; j < array.length; j++) {
			for (k = Math.max(k, j + 1);; k++) {
				if (k == j + array.length) {
					break;
				}
				if (orientation(origin, array[j], array[k % array.length]) < 0) {
					break;
				}
			}
			System.err.println("Span size: " + (j + array.length - k));
			System.err.println(array[j] + ".." + array[k % array.length]);
		}
	}
	
	static long area(long xA, long yA, long xB, long yB, long xC, long yC) {
		return Math.abs(
				xA * yB - xB * yA +
				xB * yC - xC * yB +
				xC * yA - xA * yC
				);
	}
	
	static int orientation(Point a, Point b, Point c) {
		return orientation(a.x, a.y, b.x, b.y, c.x, c.y);
	}

	static int orientation(long xA, long yA, long xB, long yB, long xC, long yC) {
		return Long.signum(
				xA * yB - xB * yA +
				xB * yC - xC * yB +
				xC * yA - xA * yC
				);
	}

	static int orientation4D(int x0, int y0, int z0, int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3) {
		long a0 = x1 - x0;
		long a1 = x2 - x0;
		long a2 = x3 - x0;
		long b0 = y1 - y0;
		long b1 = y2 - y0;
		long b2 = y3 - y0;
		long c0 = z1 - z0;
		long c1 = z2 - z0;
		long c2 = z3 - z0;
		long det =
				a0 * b1 * c2 - a0 * b2 * c1 +
				a1 * b2 * c0 - a1 * b0 * c2 +
				a2 * b0 * c1 - a2 * b1 * c0;
		return Long.signum(det);
	}

	static int quarterOrAxis(int x, int y) {
		if (y == 0) {
			if (x == 0) {
				return -1;
			}
			return x > 0 ? 0 : 4;
		}
		if (x == 0) {
			return y > 0 ? 2 : 6;
		}
		if (y > 0) {
			return x > 0 ? 1 : 3;
		}
		return x > 0 ? 7 : 5;
	}
	
	static boolean insideTriangleNonStrict(int[] xs, int[] ys, int x, int y) {
		int o0 = orientation(xs[0], ys[0], xs[1], ys[1], x, y);
		int o1 = orientation(xs[1], ys[1], xs[2], ys[2], x, y);
		int o2 = orientation(xs[2], ys[2], xs[0], ys[0], x, y);
		int oMin = Math.min(Math.min(o0, o1), o2);
		int oMax = Math.max(Math.max(o0, o1), o2);
		return oMin * oMax != -1;
	}
	
	static boolean insideTriangleStrict(int[] xs, int[] ys, int x, int y) {
		int o0 = orientation(xs[0], ys[0], xs[1], ys[1], x, y);
		int o1 = orientation(xs[1], ys[1], xs[2], ys[2], x, y);
		int o2 = orientation(xs[2], ys[2], xs[0], ys[0], x, y);
		return Math.abs(o0 + o1 + o2) == 3;
	}
	
	static boolean betWeenStrictly(int x0, int x1, int x2) {
		return (x0 < x2) ? (x0 < x1 && x1 < x2) : (x0 > x1 && x1 > x2);
	}
	
	static boolean pointOnSegmentStrict(int x, int y, int x0, int y0, int x1, int y1) {
		if (orientation(x, y, x0, y0, x1, y1) != 0) {
			return false;
		}
		return betWeenStrictly(x0, x, x1) || betWeenStrictly(y0, y, y1);
	}
	
	static boolean segmentsIntersectStrictlyNonParrallel(
			int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3) {
		return orientation(x0, y0, x1, y1, x2, y2) * orientation(x0, y0, x1, y1, x3, y3) == -1
				&& orientation(x2, y2, x3, y3, x0, y0) * orientation(x2, y2, x3, y3, x1, y1) == -1;
	}
	
	static boolean linearSegmentsIntersectNonStricly(int a, int b, int c, int d) {
		return Math.max(Math.min(a, b), Math.min(c, d)) <= Math.min(Math.max(a, b), Math.max(c, d));
	}
	
	static boolean segmentsIntersectNonStricly(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3) {
		return linearSegmentsIntersectNonStricly(x0, x1, x2, x3) && linearSegmentsIntersectNonStricly(y0, y1, y2, y3) &&
				orientation(x0, y0, x1, y1, x2, y2) * orientation(x0, y0, x1, y1, x3, y3) <= 0 && 
				orientation(x2, y2, x3, y3, x0, y0) * orientation(x2, y2, x3, y3, x1, y1) <= 0;
	}

	class Point {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
