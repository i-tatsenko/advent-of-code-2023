import java.util.LinkedList

enum class Pipe(val sign: Char, val inbound: Point, val outbound: Point) {
    VERT('|', Point(-1, 0), Point(1, 0)),
    HOR('-', Point(0, -1), Point(0, 1)),
    L('L', Point(-1, 0), Point(0, 1)),
    J('J', Point(-1, 0), Point(0, -1)),
    LB('7', Point(0, -1), Point(1, 0)),
    F('F', Point(0, 1), Point(1, 0)),
    ;

    companion object {
        private val map: Map<Char, Pipe> by lazy {
            entries.groupBy { it.sign }.mapValues { it.value[0] }
        }

        fun valueOf(ch: Char) = map[ch]
    }

    fun canEnter(from: Point, current: Point): Boolean {
        val mask = from - current
        return mask == inbound || mask == outbound
    }

    fun nextFrom(from: Point, current: Point): Point {
        val mask = from - current
        return current + if (mask == inbound) outbound else inbound
    }
}

operator fun List<String>.get(p: Point) = this[p.x][p.y]

fun path(input: List<String>) = sequence {
    val maxY = input[0].length
    val start = findStart(input)
    val nextPoint = listOf(start(-1, 0), start(1, 0), start(0, -1), start(0, 1))
        .find {
            val newX = it.x
            val newY = it.y
            if (newX < 0 || newX >= input.size || newY < 0 || newY >= maxY) return@find false
            Pipe.valueOf(input[it])?.canEnter(start, it) ?: false
        }

    var prev = start
    var curr = nextPoint!!
    var moves = 1
    while (curr != start) {
        moves++
        val pipe = Pipe.valueOf(input[curr]) ?: throw RuntimeException("Logic error")
        yield(curr to pipe)
        val next = pipe.nextFrom(prev, curr)
        prev = curr
        curr = next
    }
}

private fun findStart(input: List<String>): Point {
    val startX = input.indexOfFirst { it.contains("S") }
    val startY = input[startX].indexOf("S")
    val start = Point(startX, startY)
    return start
}

operator fun Array<Array<Int>>.set(p: Point, value: Int) {
    this[p.x][p.y] = value
}
operator fun Array<Array<Int>>.get(p: Point) = this[p.x][p.y]

fun Array<Array<Int>>.print()  {
    for (row in this) {
        println(row.joinToString(" ", "[", "]"))
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return (path(input).count() + 1) / 2
    }

    fun part2(input: List<String>): Int {
        val field = Array(input.size * 2) { Array(input[0].length * 2) { 0 } }
        val start = findStart(input)
        field[start * 2] = 1
        path(input).forEach { (point, pipe) ->
            field[point * 2] = 1
            field[(point * 2) + pipe.inbound] = 1
            field[(point * 2) + pipe.outbound] = 1
        }

        val queue = LinkedList<Point>()
        for (x in listOf(0, field.size - 1))
            for (y in field[0].indices)
                if (field[x][y] == 0) {
                    field[x][y] = 2
                    queue.add(Point(x, y))
                }
        for (y in listOf(0, field[0].size - 1))
            for (x in field.indices) {
                if (field[x][y] == 0) {
                    field[x][y] = 2
                    queue.add(Point(x, y))
                }
            }
        while (queue.isNotEmpty()) {
            val curr = queue.pop()
            listOf(Point(-1, 0), Point(1, 0), Point(0, -1), Point(0, 1))
                .map { it + curr }
                .filter { it.x > 0 && it.y > 0 && it.x < field.size && it.y < field[0].size }
                .forEach {
                    if (field[it] == 0) {
                        field[it] = 2
                        queue.add(it)
                    }
                }
        }
        var result = 0
        for (x in field.indices step 2) {
            for (y in field[0].indices step 2) {
                if (field[x][y] == 0) result++
            }
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day10_test")) == 8)
    check(part1(readInput("Day10")) == 6942)

    check(part2(readInput("Day10_test_2")) == 4)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
