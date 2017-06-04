public class ProbabilityTheory {
	static double gamblerRuin(int up, int down, double p) {
		if (p == 0.5) {
			return down * 1.0 / (up + down);
		}
		if (p < 0.5) {
			return 1 - gamblerRuin(down, up, 1 - p);
		}
		double qp = (1 - p) / p;
		return (Math.pow(qp, down) - 1) / (Math.pow(qp, up + down) - 1);
	}
}
