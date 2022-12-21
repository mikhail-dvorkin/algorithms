import java.util.*;

public class SimulatedAnnealing {
	public static interface Annealable {
		public double energy();
		public Annealable randomInstance(Random random);
	}
	
	public static interface AnnealableWithoutStepBack extends Annealable {
		public AnnealableWithoutStepBack vary(Random random);
	}
	
	public static interface AnnealableWithStepBack extends Annealable {
		public void vary(Random random);
		public void undo();
		public AnnealableWithStepBack cloneAnswer();
	}

	public static class Settings {
		public int globalIterations;
		public int iterations;
		public double probStartWithPrevious;
		public int recessionLimit;
		public double desiredEnergy;
		public double temp0;

		public Settings(int globalIterations, int iterations, double probStartWithPrevious, int recessionLimit, double desiredEnergy, double temp0) {
			this.globalIterations = globalIterations;
			this.iterations = iterations;
			this.probStartWithPrevious = probStartWithPrevious;
			this.recessionLimit = recessionLimit;
			this.desiredEnergy = desiredEnergy;
			this.temp0 = temp0;
		}
		
		public Settings() {
			this(1024, 8192, 1 - 1.0 / 16, Integer.MAX_VALUE, -Double.MAX_VALUE, 1);
		}
	}
	
	public static Annealable simulatedAnnealing(Annealable item, Settings settings, Random r) {
		boolean stepBack = item instanceof AnnealableWithStepBack;
		double energy = item.energy();
		double answerEnergy = Double.MAX_VALUE;
		Annealable answer = null;
		for (int glob = 0; glob != settings.globalIterations; glob++) {
			if (glob > 0 && r.nextDouble() >= settings.probStartWithPrevious) {
				item = item.randomInstance(r);
				energy = item.energy();
			}
			int end = settings.iterations;
			for (int iter = 1, recession = 0;; iter++) {
				if (energy < answerEnergy) {
					answerEnergy = energy;
					if (stepBack) {
						answer = ((AnnealableWithStepBack) item).cloneAnswer();
					} else {
						answer = item;
					}
					if (answerEnergy <= settings.desiredEnergy) {
						return answer;
					}
					end = Math.max(end, iter + settings.iterations);
				}
				if (iter > end) {
					break;
				}
				double nextEnergy;
				AnnealableWithoutStepBack next = null;
				if (stepBack) {
					((AnnealableWithStepBack) item).vary(r);
					nextEnergy = item.energy();
				} else {
					next = ((AnnealableWithoutStepBack) item).vary(r);
					nextEnergy = next.energy();
				}
				double dEnergy = nextEnergy - energy;
				boolean accept;
				if (dEnergy < 0) {
					accept = true;
					recession = 0;
				} else {
					recession++;
					if (recession == settings.recessionLimit) {
						break;
					}
					//TODO better:
					//t = t_start * (t_final / t_start) ** part_of_time_passed
					//p = exp((cur_result - new_result) / t)
					double barrier = Math.exp(-1.0 * dEnergy * iter / settings.temp0);
					accept = r.nextDouble() < barrier;
				}
				if (accept) {
					if (!stepBack) {
						assert(next != null);
						item = next;
					}
					energy = nextEnergy;
				} else {
					if (stepBack) {
						((AnnealableWithStepBack) item).undo();
					}
				}
			}
		}
		return answer;
	}

	static class Queens implements AnnealableWithoutStepBack {
		private static int n;
		final int[] a;
		private final int energy;
		
		public Queens(int n, Random random) {
			Queens.n = n;
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < n; i++) {
				list.add(i);
			}
			Collections.shuffle(list, random);
			a = new int[n];
			int e = 0;
			for (int i = 0; i < n; i++) {
				a[i] = list.get(i);
				for (int j = 0; j < i; j++) {
					if (i - j == Math.abs(a[i] - a[j])) {
						e++;
					}
				}
			}
			energy = e;
		}
		
		private Queens(int[] a, int energy) {
			this.a = a;
			this.energy = energy;
		}
		
		@Override
		public AnnealableWithoutStepBack randomInstance(Random random) {
			return new Queens(n, random);
		}
		
		@Override
		public double energy() {
			return energy;
		}
		
		@Override
		public Queens vary(Random random) {
			int i = random.nextInt(n);
			int j;
			for (;;) {
				j = random.nextInt(n);
				if (i != j) {
					break;
				}
			}
			int[] b = a.clone();
			int t = b[i];
			b[i] = b[j];
			b[j] = t;
			int e = energy;
			for (int k = 0; k < n; k++) {
				if (k == i || k == j) {
					continue;
				}
				if (Math.abs(i - k) == Math.abs(a[i] - a[k])) {
					e--;
				}
				if (Math.abs(j - k) == Math.abs(a[j] - a[k])) {
					e--;
				}
				if (Math.abs(i - k) == Math.abs(b[i] - a[k])) {
					e++;
				}
				if (Math.abs(j - k) == Math.abs(b[j] - a[k])) {
					e++;
				}
			}
			return new Queens(b, e);
		}
		
		@Override
		public String toString() {
			return queensToString(a, n);
		}
	}
	
	static class QueensMutable implements AnnealableWithStepBack {
		int n;
		int[] a;
		int energy;
		int lastSwapA, lastSwapB, prevEnergy;
		
		public QueensMutable(int n, Random random) {
			this.n = n;
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < n; i++) {
				list.add(i);
			}
			Collections.shuffle(list, random);
			a = new int[n];
			for (int i = 0; i < n; i++) {
				a[i] = list.get(i);
				for (int j = 0; j < i; j++) {
					if (i - j == Math.abs(a[i] - a[j])) {
						energy++;
					}
				}
			}
		}
		
		private QueensMutable(int n, int[] a, int energy) {
			this.n = n;
			this.a = a;
			this.energy = energy;
		}

		@Override
		public QueensMutable cloneAnswer() {
			return new QueensMutable(n, a, energy);
		}
		
		@Override
		public double energy() {
			return energy;
		}
		
		@Override
		public void vary(Random random) {
			prevEnergy = energy;
			int i = random.nextInt(n);
			int j;
			for (;;) {
				j = random.nextInt(n);
				if (i != j) {
					break;
				}
			}
			int t = a[i];
			a[i] = a[j];
			a[j] = t;
			for (int k = 0; k < n; k++) {
				if (k == i || k == j) {
					continue;
				}
				if (Math.abs(i - k) == Math.abs(a[j] - a[k])) {
					energy--;
				}
				if (Math.abs(j - k) == Math.abs(a[i] - a[k])) {
					energy--;
				}
				if (Math.abs(i - k) == Math.abs(a[i] - a[k])) {
					energy++;
				}
				if (Math.abs(j - k) == Math.abs(a[j] - a[k])) {
					energy++;
				}
			}
			lastSwapA = i;
			lastSwapB = j;
		}
		
		@Override
		public void undo() {
			energy = prevEnergy;
			int t = a[lastSwapA];
			a[lastSwapA] = a[lastSwapB];
			a[lastSwapB] = t;
		}
		
		@Override
		public AnnealableWithStepBack randomInstance(Random random) {
			return new QueensMutable(n, random);
		}
		
		@Override
		public String toString() {
			return queensToString(a, n);
		}
	}
	
	static String queensToString(int[] a, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				sb.append((a[i] == j) ? "*" : ".");
			}
			sb.append("\n");
		}
		return sb.toString().trim();
	}
	
	public static void main(String[] args) {
		Random r = new Random(566);
		int n = 64;
		Settings settings = new Settings();
		settings.desiredEnergy = 0;
		for (Annealable item : new Annealable[]{new Queens(n, r), new QueensMutable(n, r)}) {
			Annealable answer = simulatedAnnealing(item, settings, r);
			System.out.println(answer);
			System.out.println(answer.energy());
		}
	}
}
