private fun longestIncreasingSubsequenceLength(sequence: List<Int>): Int {
	val bestEnd = mutableListOf<Int>()
	for (x in sequence) {
		val pos = bestEnd.binarySearch(x).let { if (it >= 0) it else it.inv() }
		if (pos == bestEnd.size) bestEnd.add(x) else bestEnd[pos] = x
	}
	return bestEnd.size
}
