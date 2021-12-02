data class Delta(
        private val previousDepth: Int?,
        private val depth: Int
) {
    val isDeeper: Boolean
        get() = previousDepth?.let { p -> depth > p } ?: false
}

data class Window(
        private val secondPrevious: Int?,
        private val firstPrevious: Int?,
        private val current: Int?
) {
    val sum: Int
        get() = (current ?: 0) + (firstPrevious ?: 0) + (secondPrevious ?: 0)
}

fun List<Int>.getDelta(index: Int) = Delta(
        previousDepth = this.getOrNull(index - 1),
        depth = this[index]
)

fun List<Int>.getWindow(index: Int) = Window(
        secondPrevious = this.getOrNull(index - 2),
        firstPrevious = this.getOrNull(index - 1),
        current = this.getOrNull(index)
)

fun List<Int>.getWindowDelta(index: Int) = Delta(
        previousDepth = this.getWindow(index - 1).sum,
        depth = this.getWindow(index).sum
)

fun main() {
    fun part1(input: List<Int>): Int {
        return IntRange(0, input.lastIndex)
            .map(input::getDelta)
            .count { it.isDeeper }
    }

    fun part2(input: List<Int>): Int {
        return IntRange(3, input.lastIndex)
            .map(input::getWindowDelta)
            .count { it.isDeeper }
    }

    val input = readInput("Day01_input")
    val depths = input.map { it.toInt() }
    println("Part 1: Depth increases: ${part1(depths)}")
    println("Part 2: Windowed depth increases: ${part2(depths)}")
}
