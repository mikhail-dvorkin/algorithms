import java.util.*;

public class NumberTheory {
	static ArrayList<Integer> temp = new ArrayList<Integer>();
	static ArrayList<Long> tempLong = new ArrayList<Long>();
	
	public static boolean[] isPrimeArray(int n) {
		boolean[] isPrime = new boolean[n + 1];
		for (int i = 2; i <= n; i++) {
			isPrime[i] = true;
		}
		for (int i = 2, j; (j = i * i) <= n; i++) {
			if (!isPrime[i]) {
				continue;
			}
			do {
				isPrime[j] = false;
				j += i;
			} while (j <= n);
		}
		return isPrime;
	}
	
	public static int[] primesUpTo(int n) {
		boolean[] isPrime = isPrimeArray(n);
		int m = 0;
		for (int i = 2; i <= n; i++) {
			if (isPrime[i]) {
				m++;
			}
		}
		int[] primes = new int[m];
		m = 0;
		for (int i = 2; i <= n; i++) {
			if (isPrime[i]) {
				primes[m++] = i;
			}
		}
		return primes;
	}
	
	public static int[] firstPrimes(int amount) {
		int m = amount;
		while (true) {
			int[] a = primesUpTo(m);
			if (a.length >= amount) {
				return Arrays.copyOf(a, amount);
			}
			m *= 2;
		}
	}
	
	/**
	 * 24 -> [2, 2, 2, 3]
	 */
	public static int[] primeFactorization(int n) {
		temp.clear();
		for (int i = 2; n > 1 && i * i <= n; i++) {
			while (n % i == 0) {
				temp.add(i);
				n /= i;
			}
		}
		if (n > 1)
			temp.add(n);
		int[] factors = new int[temp.size()];
		for (int i = 0; i < factors.length; i++) {
			factors[i] = temp.get(i);
		}
		return factors;
	}
	
	public static long[] primeFactorization(long n) {
		tempLong.clear();
		for (long i = 2; n > 1 && i * i <= n; i++) {
			while (n % i == 0) {
				tempLong.add(i);
				n /= i;
			}
		}
		if (n > 1)
			tempLong.add(n);
		long[] factors = new long[tempLong.size()];
		for (int i = 0; i < factors.length; i++) {
			factors[i] = tempLong.get(i);
		}
		return factors;
	}
	
	/**
	 * 24 -> [1, 2, 3, 4, 6, 8, 12, 24]
	 * TODO improve
	 */
	public static int[] divisors(int n) {
		int i;
		temp.clear();
		for (i = 1; i * i < n; i++) {
			if (n % i == 0)
				temp.add(i);
		}
		int[] divisors;
		if (i * i == n) {
			divisors = new int[2 * temp.size() + 1]; 
			divisors[temp.size()] = i;
		} else
			divisors = new int[2 * temp.size()];
		for (int j = 0; j < temp.size(); j++) {
			divisors[j] = temp.get(j);
			divisors[divisors.length - 1 - j] = n / temp.get(j);
		}
		return divisors;
	}
	
	public static int gcd(int a, int b) {
		while (a > 0) {
			int t = b % a;
			b = a;
			a = t;
		}
		return b;
	}
	
	public static long gcd(long a, long b) {
		while (a > 0) {
			long t = b % a;
			b = a;
			a = t;
		}
		return b;
	}
	
	public static int gcdExtended(int a, int b, int[] xy) {
		if (a == 0) {
			xy[0] = 0;
			xy[1] = 1;
			return b;
		}
		int d = gcdExtended(b % a, a, xy);
		int t = xy[0];
		xy[0] = xy[1] - (b / a) * xy[0];
		xy[1] = t;
		return d;
	}
	
	public static int modInverse(int x, int p) {
		int[] xy = new int[2];
		int gcd = gcdExtended(x, p, xy);
		if (gcd != 1) {
			throw new IllegalArgumentException(x + ", " + p);
		}
		int result = xy[0] % p;
		if (result < 0) {
			result += p;
		}
		return result;
	}
	
	public static long lcm(long a, long b) {
		return (a / gcd(a, b)) * b;
	}
	
	public static int[] fact, factInv;
	
	public static void precalcFactorials(int n, final int M) {
		fact = new int[n];
		factInv = new int[fact.length];
		fact[0] = factInv[0] = 1;
		for (int i = 1; i < fact.length; i++) {
			fact[i] = (int) (1L * fact[i - 1] * i % M);
			factInv[i] = modInverse(fact[i], M);
		}
	}
	
	public static int a(int n, int k, final int M) {
		return (int) (1L * fact[n] * factInv[n - k] % M);
	}
	
	public static int c(int n, int k, final int M) {
		return (int) (1L * a(n, k, M) * factInv[k] % M);
	}
	
	public static boolean isPrime1(int n) {
		for (int i = 2; i * i <= n; i++) {
			if (n % i == 0)
				return false;
		}
		return n >= 2;
	}
	
	static final int[] toCheck = new int[]{2, 3, 5, 7};
	
	public static boolean isPrime2(int n) {
		if ((n & 1) == 0)
			return n == 2;
		if (n < 8)
			return n > 1;
		int d = n - 1;
		int s = 0;
		while ((d & 1) == 0) {
			d >>= 1;
			s++;
		}
		aloop:
		for (int a : toCheck) {
			if (n % a == 0)
				return false;
			long p = power(a, d, n);
			if (p != 1) {
				for (int r = 0; r < s; r++) {
					if (p == n - 1)
						continue aloop;
					p *= p;
					p %= n;
				}
				return false;
			}
		}
		return true;
	}
	
	public static long power(long base, long p, long modulo) {
		if (p < 0) {
			throw new IllegalArgumentException("" + p);
		}
		if (p == 0) {
			return (modulo == 1) ? 0 : 1;
		}
		long v = power(base, p / 2, modulo);
		v = (v * v) % modulo;
		return (p & 1) == 0 ? v : (v * base) % modulo;
	}
	
	public static final int mix(int x) {
		x ^= x >>> 16;
		x *= 0x85ebca6b;
		x ^= x >>> 13;
		x *= 0xc2b2ae35;
		x ^= x >>> 16;
		return x;
	}

	public static final long mix(long x) {
		x ^= x >>> 33;
		x *= 0xff51afd7ed558ccdL;
		x ^= x >>> 33;
		x *= 0xc4ceb9fe1a85ec53L;
		x ^= x >>> 33;
		return x;
	}

	static final int M = 1_000_000_007;

	static int add(int a, int b) {
		return (a + b) % M;
	}

	static int mul(int a, int b) {
		return (int) ((a * (long) b) % M);
	}

	static int div(int a, int b) {
		return mul(a, modInverse(b, M));
	}

	static int pow(int base, int p) {
		if (p < 0) {
			return pow(modInverse(base, M), -p);
		}
		if (p == 0) {
			return 1;
		}
		int v = pow(base, p / 2);
		v = mul(v, v);
		return (p & 1) == 0 ? v : mul(v, base);
	}

	public static void main(String[] args) {
		int[] xy = new int[2];
		System.out.println(gcdExtended(42, 2017, xy));
		System.out.println(modInverse(42, 2017));
		System.out.println(modInverse(3, 7));
		System.out.println(Arrays.toString(xy));
		long ans = 0;
		for (int i : primesUpTo(2000000))
			ans += i;
		System.out.println(ans);
	}
}
