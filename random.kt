import kotlin.random.Random

fun Random.nextSequence(n: Int) = sequence {
	val threshold = n / 2
	val used = mutableSetOf<Int>()
	while (used.size < threshold) {
		val x = nextInt(n)
		if (used.add(x)) yield(x)
	}
	yieldAll((0 until n).filter { it !in used }.shuffled(this@nextSequence))
}

fun <E> List<E>.asShuffledSequence(random: Random) = random.nextSequence(size).map { this[it] }

fun <E> List<E>.randomOrNull(random: Random, predicate: (E) -> Boolean) =
	asShuffledSequence(random).firstOrNull(predicate)
