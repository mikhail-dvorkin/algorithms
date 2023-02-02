@file:Suppress("unused")
private fun readLn() = readLine()!!
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }
private fun readLongs() = readStrings().map { it.toLong() }

private operator fun <T> List<T>.component6() = get(5)
private fun <T> Iterable<T>.withReversed() = listOf(toList(), reversed())
private fun <T> List<T>.toPair() = get(0) to get(1)
private fun <T, R> Iterable<T>.cartesianProduct(other: Iterable<R>) = flatMap { x -> other.map { y -> x to y } }
private fun <T> Iterable<T>.cartesianSquare() = flatMap { x -> map { y -> x to y } }
private fun <T> Iterable<T>.cartesianTriangle() = withIndex().flatMap { x -> take(x.index).map { it to x.value } }
private fun IntProgression.size() = (last - first) / step + 1
private fun Collection<Int>.mex() = (0..this.size).first { it !in this }
private fun Long.coerceInInt() = if (this >= Int.MAX_VALUE) Int.MAX_VALUE else if (this <= Int.MIN_VALUE) Int.MIN_VALUE else toInt()
private fun IntArray.reversedPermutation() = IntArray(size).also { for (i in indices) it[this[i]] = i }
private fun Int.bit(index: Int) = shr(index) and 1
private fun Int.hasBit(index: Int) = bit(index) != 0
private fun Int.countSignificantBits() = Int.SIZE_BITS - Integer.numberOfLeadingZeros(this)
private fun Int.oneIndices() = (0 until countSignificantBits()).filter { bit(it) != 0 }
private fun Int.abs() = kotlin.math.abs(this)
private fun Int.sqr() = this * this
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
	var (low, high) = this.first to this.last // must be false ... must be true
	while (low + 1 < high) (low + (high - low) / 2).also { if (predicate(it)) high = it else low = it }
	return high // first true
}

private fun Int.toModular() = Modular(this)//toDouble(); typealias Modular = Double
private class Modular {
	companion object {
		const val M = 998244353
	}
	val x: Int
	@Suppress("ConvertSecondaryConstructorToPrimary")
	constructor(value: Int) { x = (value % M).let { if (it < 0) it + M else it } }
	operator fun plus(that: Modular) = Modular((x + that.x) % M)
	operator fun minus(that: Modular) = Modular((x + M - that.x) % M)
	operator fun times(that: Modular) = (x.toLong() * that.x % M).toInt().toModular()
	private fun modInverse() = Modular(x.toBigInteger().modInverse(M.toBigInteger()).toInt())
	operator fun div(that: Modular) = times(that.modInverse())
	override fun toString() = x.toString()
}
private operator fun Int.plus(that: Modular) = Modular(this) + that
private operator fun Int.minus(that: Modular) = Modular(this) - that
private operator fun Int.times(that: Modular) = Modular(this) * that
private operator fun Int.div(that: Modular) = Modular(this) / that

private val isOnlineJudge = System.getProperty("ONLINE_JUDGE") == "true"
private val stdStreams = (true to true).apply  { if (!isOnlineJudge) {
	if (!first) System.setIn(java.io.File("input.txt").inputStream())
	if (!second) System.setOut(java.io.PrintStream("output.txt"))
}}
private fun setIn(input: String) = System.setIn(input.byteInputStream())
private val bufferedReader = java.io.BufferedReader(java.io.InputStreamReader(System.`in`))
private fun readLn2() = bufferedReader.readLine()
