import java.util.LinkedList
import kotlin.math.max

enum class Dir(private val mask: Point) {
    UP(Point(-1, 0)),
    DOWN(Point(1, 0)),
    LEFT(Point(0, -1)),
    RIGHT((Point(0, 1)));

    fun next(p: Point): Point = p + mask
}

data class Vector(val p: Point, val d: Dir)

class MapCell(
    private val content: Char,
    private val coords: Point,
    private val visited: MutableSet<Dir> = mutableSetOf()
) {

    private fun vector(d: Dir): Vector = Vector(d.next(coords), d)
    fun visit(d: Dir): List<Vector> {
        if (!visited.add(d)) {
            return listOf()
        }
        return when (content) {
            '.' -> listOf(vector(d))
            '|' -> when (d) {
                Dir.UP, Dir.DOWN -> listOf(vector(d))
                else -> listOf(vector(Dir.UP), vector(Dir.DOWN))

            }

            '-' -> when (d) {
                Dir.LEFT, Dir.RIGHT -> listOf(vector(d))
                else -> listOf(vector(Dir.LEFT), vector(Dir.RIGHT))

            }

            '\\' -> when (d) {
                Dir.RIGHT -> listOf(vector(Dir.DOWN))
                Dir.DOWN -> listOf(vector(Dir.RIGHT))
                Dir.LEFT -> listOf(vector(Dir.UP))
                Dir.UP -> listOf(vector(Dir.LEFT))

            }
//  /
            else -> when (d) {
                Dir.RIGHT -> listOf(vector(Dir.UP))
                Dir.DOWN -> listOf(vector(Dir.LEFT))
                Dir.LEFT -> listOf(vector(Dir.DOWN))
                Dir.UP -> listOf(vector(Dir.RIGHT))
            }
        }
    }

    fun isEnergized() = visited.isNotEmpty()
}

operator fun <T> Array<Array<T>>.get(p: Point) = this[p.x][p.y]

fun main() {

    fun energized(input: List<String>, start: Vector = Vector(Point(0, 0), Dir.RIGHT)): Int {
        val queue = LinkedList<Vector>()
        val map = Array(input.size) { x -> Array(input.first().length) { y -> MapCell(input[x][y], Point(x, y)) } }
        queue.add(start)
        while (queue.isNotEmpty()) {
            val (p, d) = queue.pop()
            queue.addAll(map[p].visit(d).filter { (p) -> p.inBounds(input.size, input.first().length) })
        }
        return map.sumOf { row -> row.count { it.isEnergized() } }
    }

    fun part1(input: List<String>): Int {
        return energized(input)
    }

    fun part2(input: List<String>): Int {
        val fromVerticalBound = input.indices.flatMap {
            listOf(
                energized(input, Vector(Point(it, 0), Dir.RIGHT)),
                energized(input, Vector(Point(it, input.first().length - 1), Dir.LEFT))
            )
        }.max()
        val fromHorizontalBound = input.first().indices.flatMap {
            listOf(
                energized(input, Vector(Point(0, it), Dir.DOWN)),
                energized(input, Vector(Point(input.size -1, it), Dir.UP))
            )
        }.max()
        return max(fromVerticalBound, fromHorizontalBound)
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day16_test")) == 46)
    check(part1(readInput("Day16")) == 7210)

    check(part2(readInput("Day16_test")) == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
