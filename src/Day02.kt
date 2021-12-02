data class Position(
    val depth: Int = 0,
    val horizontal: Int = 0
)

data class PositionWithAim(
    val position: Position = Position(),
    val aim: Int = 0
)

val positionPattern: Regex = """^(\w+) (\d+)$""".toRegex()

fun stringToPosition(positionString: String): Position = positionPattern.find(positionString.trim())
    ?.let { match ->
        val (direction, distanceString) = match.destructured
        val distance = distanceString.toInt()
        when(direction) {
            "up" -> Position(depth = -1 * distance)
            "down" -> Position(depth = distance)
            "forward" -> Position(horizontal = distance)
            else -> Position()
        }
    } ?: Position()

fun move(startPosition: Position, delta: Position): Position = Position(
    depth = startPosition.depth + delta.depth,
    horizontal = startPosition.horizontal + delta.horizontal
)

fun moveWithAim(startPosition: PositionWithAim, delta: Position): PositionWithAim {
    val aim = startPosition.aim + delta.depth
    return PositionWithAim(
        aim = aim,
        position = Position(
            horizontal = startPosition.position.horizontal + delta.horizontal,
            depth = startPosition.position.depth + (delta.horizontal * aim)
        )
    )
}

fun List<Position>.aggregatePosition(): Position = this.fold(Position(), ::move)

fun List<Position>.aggregatePositionWithAim(): PositionWithAim = this.fold(PositionWithAim(), ::moveWithAim)

fun part1(movements: List<Position>): Int = movements.aggregatePosition()
    .let { it.depth * it.horizontal }

fun part2(movements: List<Position>): Int = movements.aggregatePositionWithAim()
    .let { it.position.depth * it.position.horizontal}

fun main() {
    val input = readInput("Day02_input")
    val movements = input.map(::stringToPosition)
    println("Part 1: Final positions multiplied: ${part1(movements)}")
    println("Part 2: Final positions using aim, multiplied: ${part2(movements)}")
}