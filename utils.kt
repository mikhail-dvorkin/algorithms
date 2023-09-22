@file:Suppress("unused")
private fun readInt() = readln().toInt()
private fun readStrings() = readln().split(" ")
private fun readInts() = readStrings().map { it.toInt() }
private fun readIntArray() = readln().parseIntArray()
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
private infix fun Double.isNot(other: Double) = abs(this - other) > 1e-9
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
private val stdStreams = (true to true).apply  { if (!isOnlineJudge) {
	if (!first) System.setIn(java.io.File("input.txt").inputStream())
	if (!second) System.setOut(java.io.PrintStream("output.txt"))
}}
private fun setIn(input: String) = System.setIn(input.byteInputStream())
private val bufferedReader = java.io.BufferedReader(java.io.InputStreamReader(System.`in`))
private fun readLn2() = bufferedReader.readLine()
private fun println(vararg msg: Any, sep: String=" ", end: String="\n", flush: Boolean=false) { print(msg.joinToString(sep, "", end)); if (flush) System.out.flush() }
