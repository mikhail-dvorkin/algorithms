@file:Suppress("unused")
private fun readLn() = readLine()!!
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }
private fun readLongs() = readStrings().map { it.toLong() }

private operator fun <T> List<T>.component6() = get(5)
private fun <T> Iterable<T>.withReversed() = listOf(toList(), reversed())
private fun <T> List<T>.toPair() = component1() to component2()
private fun <T> MutableList<T>.pop() = removeAt(lastIndex)
private fun <T, R> Iterable<T>.cartesianProduct(other: Iterable<R>) = flatMap { x -> other.map { y -> x to y } }
private fun <T> Iterable<T>.cartesianSquare() = flatMap { x -> map { y -> x to y } }
private fun <T> Iterable<T>.cartesianTriangle() = withIndex().flatMap { x -> take(x.index).map { it to x.value } }
private fun IntProgression.size() = (last - first) / step + 1
private fun Collection<Int>.mex() = (0..this.size).first { it !in this }
private fun Collection<Int>.sumLong() = fold(0L, Long::plus)
private fun Int.bit(index: Int) = shr(index) and 1
private fun Int.hasBit(index: Int) = bit(index) != 0
private fun Int.countSignificantBits() = Int.SIZE_BITS - Integer.numberOfLeadingZeros(this)
private fun Int.oneIndices() = (0 until countSignificantBits()).filter { bit(it) != 0 }
private fun Int.abs() = kotlin.math.abs(this)
private tailrec fun gcd(a: Int, b: Int): Int = if (a == 0) b else gcd(b % a, a)
private fun dividedByGcd(a: Int, b: Int) = gcd(a, b).let { a / it to b / it }
private fun minusOnePow(i: Int) = 1 - ((i and 1) shl 1)
private infix fun Int.towards(to: Int) = if (to > this) this..to else this downTo to
private fun Boolean.toInt() = if (this) 1 else 0
private fun <T> Boolean.iif(onTrue: T, onFalse: T) = if (this) onTrue else onFalse
private fun BooleanArray.getOrFalse(index: Int) = getOrNull(index) ?: false
private operator fun <T> Iterable<T>.times(count: Int) = (0 until count).flatMap { this }
private fun CharSequence.sorted() = toList().sorted().joinToString("")
@Suppress("DEPRECATION")
private fun eval(expression: String) = jdk.nashorn.api.scripting.NashornScriptEngineFactory().scriptEngine.eval(expression).toString()

private fun IntRange.binarySearch(predicate: (Int) -> Boolean): Int {
	var (low, high) = this.first to this.last // must be false .. must be true
	while (low + 1 < high) (low + (high - low) / 2).also { if (predicate(it)) high = it else low = it }
	return high // first true
}

private val isOnlineJudge = System.getProperty("ONLINE_JUDGE") == "true"
private val stdStreams = (true to true).apply  { if (!isOnlineJudge) {
	if (!first) System.setIn(java.io.File("input.txt").inputStream())
	if (!second) System.setOut(java.io.PrintStream("output.txt"))
}}
private val bufferedReader = java.io.BufferedReader(java.io.InputStreamReader(System.`in`))
private fun readLn2() = bufferedReader.readLine()
