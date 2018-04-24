public class Useful {
	public static final int[] DX = new int[]{1, 0, -1, 0};
	public static final int[] DY = new int[]{0, 1, 0, -1};
	public static final String DIR = "RULD";
	public static final String DIR_ROSE = "ENWS";
	public static final int[] DX8 = new int[]{1, 0, -1, 0, 1, -1, -1, 1};
	public static final int[] DY8 = new int[]{0, 1, 0, -1, 1, 1, -1, -1};
	public static final String[] DIGITAL = new String[]{"+++ +++", "  +  + ", "+ +++ +", "+ ++ ++", " +++ + ", "++ + ++", "++ ++++", "+ +  + ", "+++++++", "++++ ++"};
	
	static int[][] cnk = new int[777][777];
	static {
		for (int i = 0; i < cnk.length; i++) {
			cnk[i][i] = cnk[i][0] = 1;
			for (int j = 1; j < i; j++) {
				cnk[i][j] = cnk[i - 1][j - 1] + cnk[i - 1][j];
			}
		}
	}
	
	static int dist(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	
	static int dirTo(int x1, int y1, int x2, int y2) {
		if (x1 == x2) {
			return y1 < y2 ? 1 : 3;
		}
		return x1 < x2 ? 0 : 2;
	}
	
	static void traverse(int cx, int cy, int r, int xSize, int ySize) {
		// Filled Rhombus
		for (int x = Math.max(cx - r, 0), xxLast = Math.min(cx + r, xSize - 1); x <= xxLast; x++) {
			for (int y = Math.max(cy - r + Math.abs(x - cx), 0), yyLast = Math.min(cy + r - Math.abs(x - cx), ySize - 1); y <= yyLast; y++) {
				System.out.println(x + " " + y);
			}
		}

		// Border Rhombus
		for (int x = Math.max(cx - r, 0), xxLast = Math.min(cx + r, xSize - 1); x <= xxLast; x++) {
			for (int u = -1; u <= 1; u += 2) {
				int y = cy + u * (r - Math.abs(x - cx));
				if (y == cy && u == 1 || y < 0 || y >= ySize) {
					continue;
				}
				System.out.println(x + " " + y);
			}
		}

		// Filled Circle
		for (int x = Math.max(cx - r, 0), xxLast = Math.min(cx + r, xSize - 1); x <= xxLast; x++) {
			int hy = (int) Math.floor(Math.sqrt(r * r - (x - cx) * (x - cx)));
			for (int y = Math.max(cy - hy, 0), yyLast = Math.min(cy + hy, ySize - 1); y <= yyLast; y++) {
				System.out.println(x + " " + y);
			}
		}

		// Hyperbola
		for (int area = 2; area <= r * r; area++) {
			for (int sidex = 1; sidex * sidex <= area; sidex++) {
				if (area % sidex != 0) {
					continue;
				}
				int sidey = area / sidex;
				int dx = sidex - 1;
				int dy = sidey - 1;
				for (int dir = 0; dir < 8; dir++) {
					int swap = dir & 1;
					int sx = (dir & 2) - 1;
					int sy = ((dir >> 1) & 2) - 1;
					if (dx == 0 && sx != sy) {
						continue;
					}
					if (dx == dy && swap == 1) {
						continue;
					}
					int x = cx + sx * (swap * dx + (1 - swap) * dy);
					int y = cy + sy * (swap * dy + (1 - swap) * dx);
					if (x < 0 || y < 0 || x >= xSize || y >= ySize) {
						continue;
					}
					System.out.println(x + " " + y);
				}
			}
		}
	}
}
