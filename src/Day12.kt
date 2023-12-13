fun String.haveFailedLeft(from: Int): Boolean {
    for (i in from..<this.length)
        if (this[i] == '#') return true
    return false
}

class Task(private val s: String, private val pattern: List<Int>) {
    fun fits(start: Int, length: Int): Pair<Boolean, Int> {
        val terminatorIndex = start + length
        if (start > 0 && s[start - 1] == '#') return false to -1
        return (start + length <= s.length && (start..<terminatorIndex).all { s[it] == '#' || s[it] == '?' }
                && (terminatorIndex == s.length || s[terminatorIndex] == '.' || s[terminatorIndex] == '?')) to terminatorIndex + 1
    }

    val cache = mutableMapOf<Point, Long>()

    fun find(index: Int = 0, patternIndex: Int = 0): Long {
        val cacheKey = Point(index, patternIndex)
        if (cache.contains(cacheKey)) return cache[cacheKey]!!
        if (patternIndex == pattern.size) return if (s.haveFailedLeft(index)) 0 else 1
        if (index >= s.length) return if (patternIndex >= pattern.size) 1 else 0

        var lookupIndex = index
        while (lookupIndex < s.length && s[lookupIndex] != '#' && s[lookupIndex] != '?') lookupIndex++
        if (lookupIndex == s.length) return 0

        val (fits, nextIndex) = fits(lookupIndex, pattern[patternIndex])
        var result = 0L
        if (fits) result = find(nextIndex, patternIndex + 1)
        if (s[lookupIndex] != '#')
            result += find(lookupIndex + 1, patternIndex)

        cache[cacheKey] = result
        return result
    }
}

fun main() {

    fun part1(input: List<String>): Long {
        return input.map { it.split(" ") }.sumOf {
            Task(it.component1(),  numbers(it.component2()).toList()).find()
        }
    }

    fun part2(input: List<String>): Long {
        return input.map { it.split(" ") }.sumOf { (pattern, numbers) ->
            val newInput = (1..5).joinToString("?") { pattern }
            val newNumbers = numbers((1..5).joinToString(",") { numbers }).toList()
            Task(newInput, newNumbers).find()
        }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day12_test")) == 21L)
    check(part1(readInput("Day12")) == 8419L)
    check(part2(readInput("Day12_test")) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

