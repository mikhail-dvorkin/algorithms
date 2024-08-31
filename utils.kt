@file:Suppress("unused")
private fun solve() {}

private fun readInt() = readln().toInt()
private fun readStrings() = readln().split(" ")
private fun readInts() = readStrings().map { it.toInt() }
private fun readIntArray() = readln().parseIntArray()
private fun readLongs() = readStrings().map { it.toLong() }

private inline fun <T> T.letIf(condition: Boolean, block: (T) -> T) = if (condition) block(this) else this
private operator fun <T> List<T>.component6() = get(5)
private fun <T> List<T>.getCycled(index: Int) = getOrElse(index) { get(if (index >= 0) index % size else lastIndex - index.inv() % size) }
private fun <T> Iterable<T>.withReversed() = listOf(toList(), reversed())
private fun <T> List<T>.toPair() = get(0) to get(1)
private inline fun <E> MutableList<E?>.computeIfNull(key: Int, defaultValue: () -> E) = get(key) ?: defaultValue().also { set(key, it) }
private inline fun <T, R : Comparable<R>> Iterable<T>.minByAndValue(crossinline selector: (T) -> R) = asSequence().map { it to selector(it) }.minBy { it.second }
private inline fun <T, R : Comparable<R>> Iterable<T>.maxByAndValue(crossinline selector: (T) -> R) = asSequence().map { it to selector(it) }.maxBy { it.second }
private fun <T> List<T>.shifted(shift: Int) = drop(shift) + take(shift)
private fun <T> List<T>.allShifts() = List(size) { shifted(it) }
private fun <T> List<List<T>>.transposed() = List(this[0].size) { i -> map { it[i] } }
private fun List<IntArray>.transposedIntArray() = List(this[0].size) { i -> map { it[i] }.toIntArray() }
private fun List<String>.transposedStrings() = List(this[0].length) { i -> buildString(this@transposedStrings.size) { this@transposedStrings.forEach { append(it[i]) } } }
private fun <T, R> Iterable<T>.cartesianProduct(other: Iterable<R>) = flatMap { x -> other.map { y -> x to y } }
private fun <T> Iterable<T>.cartesianSquare() = flatMap { x -> map { y -> x to y } }
private fun <T> Iterable<T>.cartesianTriangle() = withIndex().flatMap { x -> take(x.index).map { it to x.value } }
private fun IntArray.scalarProduct(that: IntArray) = indices.sumOf { i -> this[i].toLong() * that[i] }
private fun IntProgression.size() = (last - first) / step + 1
private fun Collection<Int>.mex() = (0..this.size).first { it !in this }
private fun Long.coerceInInt() = if (this >= Int.MAX_VALUE) Int.MAX_VALUE else if (this <= Int.MIN_VALUE) Int.MIN_VALUE else toInt()
private fun IntArray.reversedPermutation() = IntArray(size).also { for (i in indices) it[this[i]] = i }
//private operator fun Int.get(index: Int) = ushr(index) and 1
private fun Int.bit(index: Int) = ushr(index) and 1
private fun Long.bit(index: Int) = ushr(index).toInt() and 1
private fun Int.hasBit(index: Int) = bit(index) != 0
private fun Int.setBit(index: Int) = or(1 shl index)
private fun Int.countSignificantBits() = Int.SIZE_BITS - Integer.numberOfLeadingZeros(this)
private fun Int.oneIndices() = (0 until countSignificantBits()).filter { bit(it) != 0 }
private infix fun Int.with(that: Int) = (toLong() shl 32) or that.toUInt().toLong()
private infix fun Int.withShort(that: Int) = shl(16) or that
private fun decode(code: Long) = (code ushr 32).toInt() to code.toInt()
private fun decodeShort(code: Int) = (code ushr 16) to (code and 0xFFFF)
private fun Int.abs() = kotlin.math.abs(this)
private fun Int.sqr() = this * this
private fun Double.abs() = kotlin.math.abs(this)
private fun Double.sqr() = this * this
private fun Double.ceil() = kotlin.math.ceil(this)
private fun Double.floor() = kotlin.math.floor(this)
private infix fun Int.mod(other: Int) = (this % other).let { it + (other and (((it xor other) and (it or -it)) shr 31)) }
private infix fun Int.ceilDiv(other: Int) = (this + other - 1) / other
private infix fun Int.upToDiv(other: Int) = (this ceilDiv other) * other
private tailrec fun gcd(a: Int, b: Int): Int = if (a == 0) b else gcd(b % a, a)
private fun dividedByGcd(a: Int, b: Int) = gcd(a, b).let { a / it to b / it }
private fun minusOnePow(i: Int) = 1 - ((i and 1) shl 1)
private infix fun Int.towards(to: Int) = if (to > this) this..to else this downTo to
private infix fun Double.isNot(other: Double) = kotlin.math.abs(this - other) > 1e-9
private fun Boolean.toInt() = if (this) 1 else 0
private fun <T> Boolean.iif(onTrue: T, onFalse: T) = if (this) onTrue else onFalse
private fun BooleanArray.getOrFalse(index: Int) = getOrNull(index) ?: false
private operator fun <T> Iterable<T>.times(count: Int) = (0 until count).flatMap { this }
private fun CharSequence.toCharArray() = CharArray(this.length) { this[it] }
private fun CharSequence.sorted() = toCharArray().apply { sort() }.concatToString()
private fun String(n: Int, init: (Int) -> Char) = buildString(n) { repeat(n) { append(init(it)) } }
private operator fun (() -> Unit).plus(that: () -> Unit) = { -> this(); that() }
@Suppress("DEPRECATION", "removal")
private fun eval(expression: String) = jdk.nashorn.api.scripting.NashornScriptEngineFactory().scriptEngine.eval(expression).toString()

private fun IntRange.binarySearch(predicate: (Int) -> Boolean): Int {
	var (low, high) = this.first to this.last // must be false ... must be true
	while (low + 1 < high) (low + (high - low) / 2).also { if (predicate(it)) high = it else low = it }
	return high // first true
}

private fun String.parseIntArray(): IntArray {
	val result = IntArray(count { it == ' ' } + 1)
	var i = 0; var value = 0
	for (c in this) {
		if (c != ' ') {
			value = value * 10 + c.code - '0'.code
			continue
		}
		result[i++] = value
		value = 0
	}
	result[i] = value
	return result
}

private val isOnlineJudge = System.getProperty("ONLINE_JUDGE") == "true"
private val stdStreams = (true to true).apply { if (!isOnlineJudge) {
	if (!first) System.setIn(java.io.File("input.txt").inputStream())
	if (!second) System.setOut(java.io.PrintStream("output.txt"))
}}
private fun setIn(input: String) = System.setIn(input.byteInputStream())
private val `in` = System.`in`.bufferedReader()
private val out = System.out.bufferedWriter()
private fun readln() = `in`.readLine()
private fun pyPrint(vararg msg: Any, sep: String=" ", end: String="\n", flush: Boolean=false) { print(msg.joinToString(sep, "", end)); if (flush) System.out.flush() }

fun mainPerTestWithTime() {
	val tests = readInt()
	val startTime = System.currentTimeMillis()
	var prevTime = startTime
	repeat(tests) {
		println("Case #${it + 1}: ${solve()}")
		val curTime = System.currentTimeMillis()
		if (curTime > prevTime + 1000) {
			System.err.println("${((it + 1) * 100.0 / tests).toInt()}%\t${(curTime - startTime) / 1000.0}s")
			prevTime = curTime
		}
	}
	out.close()
}

fun main() = repeat(readInt()) { solve() }.also { out.close() }
	