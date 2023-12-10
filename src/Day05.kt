
class MappingUnit(val source: Long,
                  target: Long,
                  private val length: Long) {
    private val diff: Long = target - source

    fun fallsInto(n: Long) = n - source < length
    fun apply(n: Long) = n + diff
}

class Mapping(private val name: String) {
    private val units: MutableList<MappingUnit> = arrayListOf()
    private var prev: Mapping? = null
    fun add(source: Long, target: Long, length: Long) {
        units.add(MappingUnit(source, target, length))
        units.sortBy { it.source }
    }

    fun mapping(source: Long): Long {
        val lookup = prev?.mapping(source) ?: source
        var result = lookup
        for (unit in units) {
            if (unit.source > lookup)
                break
            if (unit.fallsInto(lookup)) {
                result = unit.apply(lookup)
                break
            }
        }
        return result
    }

    fun combine(name: String): Mapping {
        val result = Mapping(name)
        result.prev = this
        return result
    }

}

val mapRegex = "\\w+-to-(\\w+) map:".toRegex()

fun prepareMapping(input: List<String>): Mapping {
    var mapping = Mapping("initial")
    input.drop(2).forEach { line ->
        val mapHeader = mapRegex.find(line)
        if (mapHeader != null) {
            mapping = mapping.combine(mapHeader.groupValues[0])
        } else if (line.isNotEmpty()) {
            val s = numbersL(line).toList()
            mapping.add(s[1], s[0], s[2])
        }
    }
    return mapping
}

fun main() {
    fun part1(input: List<String>): Long {
        val mapping = prepareMapping(input)
        return numbersL(input.first()).toList().minOf { mapping.mapping(it) }
    }

    fun part2(input: List<String>): Long {
        val mapping = prepareMapping(input)
        return numbersL(input.first()).chunked(2).minOf { (start, end) ->
            LongRange(start, start + end - 1).minOf {
                val r = mapping.mapping(it)
                if (r == 0L) {
                    println("$start -- $end: $it")
                }
                r
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day05_test")) == 35L)
    check(part1(readInput("Day05")) == 340994526L)
    check(part2(readInput("Day05_test")) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
