import kotlin.math.abs

fun range(l: Int, r: Int) = if (l < r) l..r else r..l


class UniverseMap(map: List<String>, private val spaceMagnification: Int = 2) {
    val galaxies: List<Point>
    val rowsWithoutGalaxies: Set<Int>
    val colsWithoutGalaxies: Set<Int>

    init {
        galaxies = map.flatMapIndexed { row, rowLine ->
            rowLine.mapIndexedNotNull { col, colChar ->
                if (colChar == '#') {
                    Point(row, col)
                } else null
            }
        }
        val rowsWithGalaxies = galaxies.map { it.x }.toSet()
        val colsWithGalaxies = galaxies.map { it.y }.toSet()
        rowsWithoutGalaxies = map.indices.filter { !rowsWithGalaxies.contains(it) }.toSet()
        colsWithoutGalaxies = map[0].indices.filter { !colsWithGalaxies.contains(it) }.toSet()
    }

    fun distances(): ULong {
        val toCount = galaxies.toMutableList()
        var result: ULong = 0u
        while (toCount.isNotEmpty()) {
            val curr = toCount.removeLast()
            result += toCount.sumOf {
                var dist = abs(it.x - curr.x) + abs(it.y - curr.y)
                for (x in range(it.x, curr.x)) {
                    if (x in rowsWithoutGalaxies) dist += spaceMagnification - 1
                }
                for (y in range(it.y, curr.y)) {
                    if (y in colsWithoutGalaxies) dist += spaceMagnification - 1
                }
                dist.toULong()
            }
        }

        return result
    }
}

fun main() {
    fun part1(input: List<String>): ULong {
        return UniverseMap(input).distances()
    }

    fun part2(input: List<String>, magn: Int = 1000000): ULong {
        return UniverseMap(input, magn).distances()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day11_test")) == 374UL)
    check(part1(readInput("Day11")) == 10231178UL)

    check(part2(readInput("Day11_test"), 10) == 1030UL)
    check(part2(readInput("Day11_test"), 100) == 8410UL)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

