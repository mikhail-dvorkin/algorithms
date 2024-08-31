data class IncrementalIncreasingSequence(val map: TreeMap<Int, Int>) {
	constructor() : this(TreeMap())

	init {
		map[Int.MIN_VALUE] = Int.MIN_VALUE
		map[Int.MAX_VALUE] = Int.MAX_VALUE
	}

	fun size() = map.size - 2

	fun add(key: Int, value: Int) {
		val floorEntry = map.floorEntry(key)
		if (floorEntry.value >= value) return
		map[key] = value
		while (true) {
			val entry = map.higherEntry(key)
			if (entry.value > value) break
			map.remove(entry.key)
		}
	}
}
