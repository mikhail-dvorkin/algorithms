import java.util.*;

public class ConvexHull {
	class Point implements Comparable<Point> {
		int x, y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Point o) {
			if (x == o.x)
				return y - o.y;
			return x - o.x;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Point) {
			    Point point = (Point) obj;
			    return (x == point.x) && (y == point.y);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return x * 239 ^ y;
		}
	}
	
	public static int orientation(Point a, Point b, Point c) {
		long area = a.x * (long) b.y - b.x * (long) a.y +
					b.x * (long) c.y - c.x * (long) b.y +
					c.x * (long) a.y - a.x * (long) c.y;
		return Long.signum(area);
	}
	
	public static Point[] convexHull(Collection<Point> points) {
		int n = points.size();
		Point[] p = points.toArray(new Point[n]);
		Arrays.sort(p);
		if (p[0].equals(p[n - 1])) {
			return new Point[]{p[0]};
		}
		ArrayList<Point> x = new ArrayList<Point>();
		for (int i = 0; i < 2 * n; i++) {
			int j;
			if (i < n) {
				j = i;
			} else {
				j = 2 * n - 1 - i;
				int o = orientation(p[0], p[n - 1], p[j]);
				if (j > 0 && o >= 0 || j == 0 && x.size() == 2)
					continue;
			}
			x.add(p[j]);
			while (true) {
				if (x.size() < 3)
					break;
				int o = orientation(x.get(x.size() - 3), x.get(x.size() - 2), x.get(x.size() - 1));
				if (o < 0)
					break;
				x.remove(x.size() - 2);
			}
		}
		if (x.get(0).equals(x.get(x.size() - 1)))
			x.remove(x.size() - 1);
		return x.toArray(new Point[x.size()]);
	}
}
