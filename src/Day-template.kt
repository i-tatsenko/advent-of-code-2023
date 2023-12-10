fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day01_test")) == 1)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
