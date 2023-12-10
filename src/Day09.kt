fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            var list =  numbersL(line).toList()
            var result = 0L
            while (list.isNotEmpty() && !list.all { it == 0L }) {
                result += list.last()
                list = list.zip(list.drop(1)).map {(l, r) -> r - l}
            }
            result
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val forResult = mutableListOf<Long>()
            var list =  numbersL(line).toList()
            while (list.isNotEmpty() && !list.all { it == 0L }) {
                forResult.add(list.first())
                list = list.zip(list.drop(1)).map {(l, r) -> r - l}
            }
            var result = 0L
            for (it in forResult.reversed()) {
                result = it - result
            }
            result
        }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day09_test")) == 114L)
    check(part1(readInput("Day09")) == 1834108701L)
    check(part2(readInput("Day09_test")) == 2L)
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
// 2992137963 is too high