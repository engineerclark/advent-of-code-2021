import kotlin.math.log2

fun Int.getBinaryDigit(place: Int) = (this shr place).and(1)
fun List<Int>.binaryDigitsToInt(): Int = this.mapIndexed { i, d -> d shl i }.fold(0) { t, d -> t.or(d) }
fun Int.countBinaryDigits(): Int = log2(this.toDouble()).toInt() + 1
fun Int.toBinaryDigits(): List<Int> = IntRange(0, this.countBinaryDigits() - 1).map(this::getBinaryDigit)
fun List<Int>.zipEither(other: List<Int>) = IntRange(0, maxOf(this.size, other.size) - 1)
    .map { i -> Pair(this.getOrNull(i) ?: 0, other.getOrNull(i) ?: 0) }
fun List<Int>.toGammaEpsilon(): GammaEpsilon = this.fold(GammaEpsilon(digits = List(12) { 0 })) { ge, reading ->
    ge.aggregate(reading)
}
fun Int.matchesAtBinaryDigitPlace(other: Int, place: Int): Boolean = this.getBinaryDigit(place) == other.getBinaryDigit(place)
//fun Int.significantDigitsMatch(other: Int, digitsToFilter: Int): Boolean = IntRange(11, 11 - digitsToFilter + 1)
//    .map { place -> this.matchesAtBinaryDigitPlace(other, place) }
//    .all { it -> it }
fun List<Int>.filterToSingleResult(filter: Int): Int = generateSequence(FilterResultSet(this, filter, -1)) { old -> FilterResultSet(
        readings = old.readings.filter { reading -> reading.matchesAtBinaryDigitPlace(old.filter, 11 - (old.digitsMatched + 1)) },
        filter = old.filter,
        digitsMatched = old.digitsMatched + 1
    )}
    .takeWhile { it.readings.isNotEmpty() && it.digitsMatched <= 11 }
    .last().let { println("${it.readings.size} readings"); it.readings.lastOrNull() ?: 0 }
val List<Int>.oxygenRating: Int get() = this.filterToSingleResult(this.toGammaEpsilon().oxygenFilter)
val List<Int>.co2Rating: Int get() = this.filterToSingleResult(this.toGammaEpsilon().co2Filter)

data class GammaEpsilon(
    val digits: List<Int>,
    val aggregateCount: Int = 0
) {
    val gamma: Int get() = digits.map { if ((it - (aggregateCount / 2)) > 0) 1 else 0 }.binaryDigitsToInt()
    val oxygenFilter: Int get() = digits.map { if ((it - (aggregateCount / 2)) >= 0) 1 else 0 }.binaryDigitsToInt()
    val epsilon: Int get() = digits.map { if ((it - (aggregateCount / 2)) < 0) 1 else 0 }.binaryDigitsToInt()
    val co2Filter: Int get() = epsilon
    fun aggregate(reading: Int): GammaEpsilon = GammaEpsilon(
        digits = this.digits.zipEither(reading.toBinaryDigits()).map { (a, b) -> a + b },
        aggregateCount = this.aggregateCount + 1
    )
}
data class FilterResultSet(
    val readings: List<Int>,
    val filter: Int,
    val digitsMatched: Int
)

fun part1(inputInts: List<Int>): Int = inputInts.toGammaEpsilon().let { ge -> ge.gamma * ge.epsilon }
fun part2(inputInts: List<Int>): Int = inputInts.oxygenRating * inputInts.co2Rating

fun main() {
    val input = readInput("day03")
    val inputInts =
        input.map { binaryString -> binaryString.map { c -> c.digitToInt() }.asReversed().binaryDigitsToInt() }
    println("Part 1: Gamma * Epsilon: ${part1(inputInts)}")
    println("Part 2: Life support rating (oxygen rating * co2 rating): ${part2(inputInts)}")
}
