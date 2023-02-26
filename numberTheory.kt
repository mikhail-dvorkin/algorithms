private fun largestPrimeDivisors(n: Int): IntArray {
	val largestPrimeDivisors = IntArray(n + 1) { it }
	for (i in 2..n) {
		if (largestPrimeDivisors[i] < i) continue
		var j = i * i
		if (j > n) break
		do {
			largestPrimeDivisors[j] = i
			j += i
		} while (j <= n)
	}
	return largestPrimeDivisors
}

private fun divisorsOf(n: Int, largestPrimeDivisors: IntArray): IntArray {
	if (n == 1) return intArrayOf(1)
	val p = largestPrimeDivisors[n]
	if (p == n) return intArrayOf(1, n)
	var m = n / p
	var counter = 2
	while (m % p == 0) {
		m /= p
		counter++
	}
	val divisorsOfM = divisorsOf(m, largestPrimeDivisors)
	val result = IntArray(divisorsOfM.size * counter)
	for (i in divisorsOfM.indices) {
		var d = divisorsOfM[i]
		for (j in 0 until counter) {
			result[i * counter + j] = d
			d *= p
		}
	}
	return result
}
