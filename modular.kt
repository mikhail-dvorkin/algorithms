@file:Suppress("unused")

//typealias Modular = Double; fun Number.toModular() = toDouble(); fun Number.toModularUnsafe() = toDouble()
//typealias ModularArray = DoubleArray; val ModularArray.data; get() = this
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

private val factorials = mutableListOf(1.toModularUnsafe())
private fun factorial(n: Int): Modular {
	while (n >= factorials.size) factorials.add(factorials.last() * factorials.size.toModularUnsafe())
	return factorials[n]
}
private fun cnk(n: Int, k: Int) = factorial(n) / factorial(k) / factorial(n - k)
