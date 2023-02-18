@file:Suppress("unused")
private fun readLn() = readLine()!!
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }
private fun readLongs() = readStrings().map { it.toLong() }

private operator fun <T> List<T>.component6() = get(5)
private fun <T> List<T>.getCycled(index: Int) = getOrElse(index) { get(if (index >= 0) index % size else lastIndex - index.inv() % size) }
private fun <T> Iterable<T>.withReversed() = listOf(toList(), reversed())
private fun <T> List<T>.toPair() = get(0) to get(1)
private fun <T> List<T>.shifted(shift: Int) = drop(shift) + take(shift)
private fun <T> List<T>.allShifts() = List(size) { shifted(it) }
private fun <T> List<List<T>>.transposed() = List(this[0].size) { i -> map { it[i] } }
private fun List<IntArray>.transposedIntArray() = List(this[0].size) { i -> map { it[i] }.toIntArray() }
private fun List<String>.transposedStrings() = List(this[0].length) { i -> buildString(this@transposedStrings.size) { this@transposedStrings.forEach { append(it[i]) } } }
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
private fun CharSequence.toCharArray() = CharArray(this.length) { this[it] }
private fun CharSequence.sorted() = toCharArray().apply { sort() }.concatToString()
private fun String(n: Int, init: (Int) -> Char) = buildString(n) { repeat(n) { append(init(it)) } }
@Suppress("DEPRECATION", "removal")
private fun eval(expression: String) = jdk.nashorn.api.scripting.NashornScriptEngineFactory().scriptEngine.eval(expression).toString()

private fun IntRange.binarySearch(predicate: (Int) -> Boolean): Int {
	var (low, high) = this.first to this.last // must be false ... must be true
	while (low + 1 < high) (low + (high - low) / 2).also { if (predicate(it)) high = it else low = it }
	return high // first true
}

//typealias Modular = Double; fun Number.toModular() = toDouble(); fun Number.toModularUnsafe() = toDouble(); typealias ModularArray = DoubleArray
@JvmInline
@Suppress("NOTHING_TO_INLINE")
private value class Modular(val x: Int) {
	companion object {
		const val M = 998244353; val MOD_BIG_INTEGER = M.toBigInteger()
	}
	inline operator fun plus(that: Modular) = Modular((x + that.x).let { if (it >= M) it - M else it })
	inline operator fun minus(that: Modular) = Modular((x - that.x).let { if (it < 0) it + M else it })
	inline operator fun times(that: Modular) = Modular((x.toLong() * that.x % M).toInt())
	inline operator fun div(that: Modular) = times(that.inverse())
	inline fun inverse() = Modular(x.toBigInteger().modInverse(MOD_BIG_INTEGER).toInt())
	override fun toString() = x.toString()
}
private fun Int.toModularUnsafe() = Modular(this)
private fun Int.toModular() = Modular(if (this >= 0) { if (this < Modular.M) this else this % Modular.M } else { Modular.M - 1 - inv() % Modular.M })
private fun Long.toModular() = Modular((if (this >= 0) { if (this < Modular.M) this else this % Modular.M } else { Modular.M - 1 - inv() % Modular.M }).toInt())
private fun java.math.BigInteger.toModular() = Modular(mod(Modular.MOD_BIG_INTEGER).toInt())
private fun String.toModular() = Modular(fold(0L) { acc, c -> (c - '0' + 10 * acc) % Modular.M }.toInt())
@JvmInline
private value class ModularArray(val data: IntArray) {
	operator fun get(index: Int) = data[index].toModularUnsafe()
	operator fun set(index: Int, value: Modular) { data[index] = value.x }
}
private inline fun ModularArray(n: Int, init: (Int) -> Modular) = ModularArray(IntArray(n) { init(it).x })

private val isOnlineJudge = System.getProperty("ONLINE_JUDGE") == "true"
private val stdStreams = (true to true).apply  { if (!isOnlineJudge) {
	if (!first) System.setIn(java.io.File("input.txt").inputStream())
	if (!second) System.setOut(java.io.PrintStream("output.txt"))
}}
private fun setIn(input: String) = System.setIn(input.byteInputStream())
private val bufferedReader = java.io.BufferedReader(java.io.InputStreamReader(System.`in`))
private fun readLn2() = bufferedReader.readLine()
private fun println(vararg msg: Any, sep: String=" ", end: String="\n", flush: Boolean=false) { print(msg.joinToString(sep, "", end)); if (flush) System.out.flush() }
