import kotlin.math.log2

fun Int.getBinaryDigit(place: Int) = (this shr place).and(1)
fun List<Int>.binaryDigitsToInt(): Int = this.mapIndexed { i, d -> d shl i }.fold(0) { t, d -> t.or(d) }
fun Int.countBinaryDigits(): Int = log2(this.toDouble()).toInt() + 1
fun Int.toBinaryDigits(): List<Int> = IntRange(0, this.countBinaryDigits() - 1).map(this::getBinaryDigit)
fun List<Int>.zipEither(other: List<Int>) = IntRange(0, maxOf(this.size, other.size) - 1)
    .map { i -> Pair(this.getOrNull(i) ?: 0, other.getOrNull(i) ?: 0) }

data class GammaEpsilon (
    val digits: List<Int>,
    val aggregateCount: Int = 0
) {
    val gamma: Int get() = digits.map { if ((it - (aggregateCount/2)) > 0) 1 else 0 }.binaryDigitsToInt()
    val epsilon: Int get() = digits.map { if ((it - (aggregateCount/2)) < 0) 1 else 0 }.binaryDigitsToInt()
    fun aggregate(reading: Int): GammaEpsilon = GammaEpsilon(
        digits = this.digits.zipEither(reading.toBinaryDigits()).map { (a, b) -> a + b },
        aggregateCount = this.aggregateCount + 1
    )
}

fun part1(inputInts: List<Int>): Int = inputInts.fold(GammaEpsilon(digits = List(12){ 0 })) { ge, reading ->
    ge.aggregate(reading)
}.let { ge -> ge.gamma * ge.epsilon }

fun main() {
    val input = readInput("day03")
    val inputInts = input.map { binaryString -> binaryString.map { c -> c.digitToInt() }.asReversed().binaryDigitsToInt() }
    println("Part 1: Gamma * Epsilon: ${part1(inputInts)}")
}