
fun main() {

    fun maps(input: List<String>) = sequence {
        var batch = arrayListOf<String>()
        for (row in input) {
            if (row.isNotEmpty()) {
                batch.add(row)
            } else {
                yield(batch)
                batch = arrayListOf()
            }
        }
        yield(batch)
    }

    fun rotate(input: List<String>): MutableList<String> {
        val resultList = mutableListOf<String>()
        for (y in input[0].indices) {
            val result = StringBuilder()
            for (x in input.size - 1 downTo 0) {
                result.append(input[x][y])
            }
            resultList.add(result.toString())
        }
        return resultList
    }

    fun diff(f: String, s: String): Int {
        var diff = 0;
        for (i in f.indices) {
            if (f[i] != s[i]) {
                diff++
            }
        }
        return diff
    }

    fun reflectionDiff(input: List<String>, axis: Int): Int {
        var diff = 0
        for (i in axis downTo 0) {
            val pairedIndex = axis + (axis - i) + 1
            if (pairedIndex >= input.size) break
            diff += diff(input[i], input[pairedIndex])
        }
        return diff
    }

    fun reflectionRow(input: List<String>, diffRequired: Int = 0): Int? {
        return (0..<input.size - 1).firstOrNull { axis -> reflectionDiff(input, axis) == diffRequired }
    }

    fun solve1(input: List<String>): Int {
        val commonReflection = reflectionRow(input)
        return if (commonReflection != null) (commonReflection + 1) * 100 else (reflectionRow(rotate(input))
            ?: throw RuntimeException("no reflection")) + 1
    }


    fun solve2(input: List<String>): Int {
        val commonReflection = reflectionRow(input, 1)
        if (commonReflection != null) return (commonReflection + 1) * 100
        return (reflectionRow(rotate(input), 1)  ?: throw RuntimeException("bebebe")) + 1

    }

    fun part1(input: List<String>): Int {
        return maps(input).sumOf { solve1(it) }
    }

    fun part2(input: List<String>): Int {
        return maps(input).sumOf { solve2(it) }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day13_test")) == 709)
    check(part1(readInput("Day13")) == 33735)
    check(part2(readInput("Day13_test")) == 1400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}


