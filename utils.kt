@file:Suppress("unused")

private fun readLn() = readLine()!!
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }
private fun readLongs() = readStrings().map { it.toLong() }

private operator fun <T> List<T>.component6() = get(5)
private fun <E> MutableList<E>.pop() = removeAt(lastIndex)
private fun <T, R> Iterable<T>.cartesianProduct(other: Iterable<R>) = flatMap { x -> other.map { y -> x to y } }
private fun <T> Iterable<T>.cartesianSquare() = flatMap { x -> map { y -> x to y } }
private fun IntProgression.size() = (last - first) / step + 1
private fun LongProgression.size() = (last - first) / step + 1
private fun Collection<Int>.mex() = (0..this.size).first { it !in this }
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