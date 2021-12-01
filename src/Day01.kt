data class Delta(
        private val previousDepth: Int?,
        private val depth: Int
) {
    val isDeeper: Boolean
        get() = previousDepth?.let { p -> depth > p } ?: false
}

fun List<Int>.getDelta(index: Int) = Delta(
        previousDepth = (index - 1).takeIf { it >= 0 }?.let { this[it] },
        depth = this[index]
)

fun main() {
    fun part1(input: List<String>): Int {
        val depths = input.map { it.toInt() }
        return IntRange(0, depths.lastIndex)
            .map(depths::getDelta)
            .count { it.isDeeper }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day01_input")
    println("Depth increases: ${part1(input)}")
    //println(part2(input))
}
