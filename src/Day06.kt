fun main() {
    fun numberOwWays(timeAndDistance: Pair<Long, Long>): Long {
        val (time, distance) = timeAndDistance
        var l = 0L
        var r = time / 2
        while (r - l > 1) {
            val m = (r - l) / 2 + l
            if ((time - m) * m > distance) {
                r = m
            } else {
                l = m
            }
        }
        return (time / 2 - r + 1) * 2 - if (time % 2 == 0L) 1 else 0
    }


    fun part1(input: List<String>): Long {
        return numbersL(input[0]).zip(numbersL(input[1]))
            .map(::numberOwWays)
            .reduce { l, r -> l * r }
    }

    fun part2(input: List<String>): Long {
        return numberOwWays(
            numbersL(input[0]).joinToString("").toLong() to
                    numbersL(input[1]).joinToString("").toLong()
        )
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day06_test")) == 288L)
    check(part2(readInput("Day06_test")) == 71503L)
    check(part1(readInput("Day06")) == 5133600L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
