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

	static double erf(double x) {
		double t = 1.0 / (1 + 0.47047 * Math.abs(x));
		double p = t * (0.3480242 + t * (-0.0958798 + t * 0.7478556));
		double ans = 1 - p * Math.exp(-x * x);
		return x >= 0 ? ans : -ans;
	}

	static double Phi(double x) {
		return 0.5 * (1 + erf(x / Math.sqrt(2.0)));
	}
}
