fun Array<Array<Char>>.println() {
    this.forEach { println(it.joinToString(" ")) }
}

const val ROCK = 'O'

class Board(input: List<String>) {
    private val map = Array(input.size) { x -> Array(input.first().length) { y -> input[x][y] } }
    private val cache = Array(map.size) { 0 }

    fun tiltVertical(north: Boolean = true): Board {
        cache.fill(if (north) 0 else map.size - 1)
        fun tiltAction(value: Int) = if (north) value + 1 else value - 1

        for (rowIndex in if (north) map.indices else map.size - 1 downTo 0) {
            map[rowIndex].forEachIndexed { index, rock ->
                when (rock) {
                    ROCK -> {
                        map[rowIndex][index] = '.'
                        map[cache[index]][index] = ROCK
                        cache[index] = tiltAction(cache[index])
                    }

                    '#' -> {
                        cache[index] = tiltAction(rowIndex)
                        map[rowIndex][index] = '#'
                    }
                }
            }
        }
        return this
    }

    fun println(): Board {
        map.println()
        kotlin.io.println()
        return this
    }

    fun tiltHorizontal(west: Boolean): Board {
        cache.fill(if (west) 0 else map.first().size - 1)
        fun tiltAction(value: Int) = if (west) value + 1 else value - 1
        for (columnIndex in if (west) map.first().indices else map.first().size - 1 downTo 0) {
            for (rowIndex in map.indices) {
                when (map[rowIndex][columnIndex]) {
                    ROCK -> {
                        map[rowIndex][columnIndex] = '.'
                        map[rowIndex][cache[rowIndex]] = ROCK
                        cache[rowIndex] = tiltAction(cache[rowIndex])
                    }

                    '#' -> {
                        cache[rowIndex] = tiltAction(columnIndex)
                        map[rowIndex][columnIndex] = '#'
                    }
                }
            }
        }
        return this
    }

    fun rockPoints() = map.flatMapIndexed { rowIndex, row -> map[rowIndex].mapIndexedNotNull{colIndex, col -> if (map[rowIndex][colIndex] == ROCK) Point(rowIndex, colIndex) else null}  }.toSet()

    fun tiltCycle() {
        tiltVertical(true).tiltHorizontal(true).tiltVertical(false).tiltHorizontal(false)
    }

    fun load() = map.mapIndexed { index, row -> row.sumOf { if (it == ROCK) map.size - index else 0 } }.sum()
}

fun main() {


    fun part1(input: List<String>): Int {
        return Board(input).tiltVertical().load()
    }

    fun part2(input: List<String>): Int {
        val board = Board(input)
        var tiltCyclesLeft = 1000000000
        val history = mutableSetOf<Set<Point>>()
        var cycleDetected = false
        while (tiltCyclesLeft > 0) {
            board.tiltCycle()
            if (!cycleDetected && history.contains(board.rockPoints())) {
                tiltCyclesLeft %= history.size
                tiltCyclesLeft += 1
                cycleDetected = true
            } else {
                history.add(board.rockPoints())
                tiltCyclesLeft--
            }
        }
        return board.load()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day14_test")) == 136)
    check(part1(readInput("Day14")) == 109833)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}

