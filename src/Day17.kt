import java.util.PriorityQueue

data class Move(val point: Point, val heatLoss: Int, val dir: Dir, val straightMoves: Int)
class PathCell(private val minStraightMoves: Int, private val maxStraightMoves: Int) {
    private val bestChoice = mutableMapOf<Dir, Array<Int>>()

    fun add(dir: Dir, moves: Int, heatLoss: Int): Boolean {
        val losses = bestChoice.computeIfAbsent(dir) { Array(maxStraightMoves + 1) { Int.MAX_VALUE } }
        if ((minStraightMoves..moves).any { losses[it] <= heatLoss }) return false
        losses[moves] = heatLoss
        return true
    }

    val minHeatLoss: Int
        get() = bestChoice.values.minOf { (minStraightMoves..<it.size).minOf { index -> it[index] } }
}

class Path(input: List<String>, private val minStraightMoves: Int, private val maxStraightMoves: Int) {
    private val x = input.size
    private val y = input.first().length
    private val map = Array(x) { Array(y) { PathCell(minStraightMoves, maxStraightMoves) } }
    private val heatLossMap = Array(x) { currX -> Array(y) { currY -> input[currX][currY].digitToInt() } }

    fun buildPath(): Int {
        val queue = PriorityQueue<Move>(compareBy({ it.heatLoss }, { it.straightMoves }, { it.dir }, { it.point }))
        queue.add(Move(Point(0, 1), heatLossMap[0][1], Dir.RIGHT, 1))
        queue.add(Move(Point(1, 0), heatLossMap[1][0], Dir.DOWN, 1))
        while (queue.isNotEmpty()) {
            val (point, heatLoss, dir, moves) = queue.poll()
            val cell = map[point]
            if (cell.add(dir, moves, heatLoss)) {
                Dir.entries.filter { !dir.isOpposite(it) && (it != dir || moves < maxStraightMoves) }
                    .forEach { newDir ->
                        val nextPoint = newDir.next(point)
                        if (nextPoint.inBounds(x, y) && (moves >= minStraightMoves || newDir == dir)) {
                            val newMove = Move(
                                nextPoint,
                                heatLossMap[nextPoint] + heatLoss,
                                newDir,
                                if (newDir == dir) moves + 1 else 1
                            )
                            queue.add(newMove)
                        }
                    }
            }
        }
        return map[x - 1][y - 1].minHeatLoss
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return Path(input, 1, 3).buildPath()
    }

    fun part2(input: List<String>): Int {
        return Path(input, 4, 10).buildPath()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day17_test")) == 102)
    check(part1(readInput("Day17")) == 767)
    check(part2(readInput("Day17_test")) == 94)


    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
