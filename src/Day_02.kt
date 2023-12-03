val gameSet = "(\\d+) (red|green|blue)".toRegex()

class Game(
    private val input: String
) {
    private val splitted = input.split(":")
    val id: Int = splitted[0].removePrefix("Game ").toInt()

    fun cubes(): List<List<String>> {
        val find = gameSet.findAll(splitted[1])
        val l = find.toList().map { it.groupValues }
        return l
    }
}

val max = hashMapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14
)

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val game = Game(it)
            val cubes: List<List<String>> = game.cubes()
            val valid: Boolean = cubes.all { group ->
                val num = group[1].toInt()
                val color = group[2]
                num <= max[color]!!
            }
            if (valid) game.id else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val game = Game(it)
            val cubes: List<List<String>> = game.cubes()
            val mins = mutableMapOf(
                "red" to 0,
                "green" to 0,
                "blue" to 0
            )
            cubes.forEach { group ->
                val num = group[1].toInt()
                val color = group[2]
                mins.compute(color) {k, prev -> if (num > prev!!) num else prev}
            }
            mins.values.reduce {acc, prev -> prev * acc}
        }

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
