private var mergeSortTemp = intArrayOf()

private fun IntArray.mergeSort(from: Int, to: Int) {
	fun sort(from: Int, to: Int) {
		val partSize = to - from
		if (partSize <= 1) return
		val mid = (from + to) / 2
		sort(from, mid); sort(mid, to)
		var i = from; var j = mid
		for (k in 0 until partSize) mergeSortTemp[k] = this[
			if (i < mid && (j == to || this[i] <= this[j])) i++ else j++
		]
		System.arraycopy(mergeSortTemp, 0, this, from, partSize)
	}

	if (mergeSortTemp.size < size) mergeSortTemp = IntArray(size)
	sort(from, to)
}
