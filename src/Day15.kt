import java.util.LinkedList

data class Lens(val label: String, var value: Int)

fun main() {

    fun hash(s: String): Int {
        var result = 0
        for (ch in s) {
            result += ch.code
            result *= 17
            result %= 256
        }
        return result
    }

    fun part1(input: List<String>): Int {
        return input.first().split(",").sumOf { hash(it) }
    }

    fun part2(input: List<String>): Int {
        val boxes = Array<LinkedList<Lens>>(256) { LinkedList() }
        val labelRegex = "^(\\w+)([=-])(\\d*)$".toRegex()
        input.first().split(",").forEach { cmd ->
            val (_, label, op, number) = labelRegex.find(cmd)!!.groupValues
            val lenses = boxes[hash(label)]
            when (op) {
                "-" -> lenses.removeIf { it.label == label }
                "=" -> {
                    val found = lenses.find { it.label == label }
                    if (found != null) found.value = number.toInt()
                    else lenses.add(Lens(label, number.toInt()))
                }
            }
        }
        return boxes.mapIndexed { boxIndex, lenses ->
            val boxMult = boxIndex + 1
            lenses.mapIndexed { index, lens -> boxMult * (index + 1) * lens.value }.sum()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day15_test")) == 1320)
    check(part1(readInput("Day15")) == 511498)
    check(part2(readInput("Day15_test")) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}

