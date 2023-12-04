import kotlin.math.pow

typealias Card = Pair<Sequence<Int>, Sequence<Int>>
fun main() {
    fun split(input: String): Card {
        val nums = input.split(": ")[1]
        val winningAndMine = nums.split(" | ")
        val numsRegex = Regex("\\d+")
        return Pair(
            numsRegex.findAll(winningAndMine[0]).flatMap { it.groupValues }.map { it.toInt() },
            numsRegex.findAll(winningAndMine[1]).flatMap { it.groupValues }.map { it.toInt() }
        )
    }

    fun winningNum(card: Card): Int {
        val (winning, mine) = card
        val winDict: Set<Int> = winning.toSet()
        return mine.count { winDict.contains(it) }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            if (winningNum(split(it)) == 0) 0 else 2.0.pow((winningNum(split(it)) - 1)).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val cards = (1..input.size).map { 1 }.toIntArray()
        input.forEachIndexed {index, line ->
            val wins = winningNum(split(line))
            (1..wins).forEach {
                cards[index + it] += cards[index]
            }
        }
        return cards.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
