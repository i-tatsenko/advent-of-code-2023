class SymbolLocation {
    private val map: MutableMap<Int, MutableSet<Int>> = hashMapOf()

    fun putSymbol(x: Int, y: Int) {
        val present = map.getOrDefault(x, hashSetOf())
        present.add(y)
        map[x] = present
    }

    fun hasSymbolAround(x: Int, range: IntRange): Boolean {
        return adjacentSymbols(x, range).any()
    }

    fun adjacentSymbols(x: Int, range: IntRange) = sequence {
        val prevRow = map.getOrDefault(x - 1, emptySet())
        val thisRow = map.getOrDefault(x, emptySet())
        val nextRow = map.getOrDefault(x + 1, emptySet())
        listOf(prevRow, thisRow, nextRow).forEachIndexed { index, row ->
            if (row.contains(range.first - 1)) yield(Point(x - 1 + index, range.first - 1))
            if (row.contains(range.last + 1)) yield(Point(x - 1 + index, range.last + 1))
        }
        range.forEach {
            if (prevRow.contains(it)) yield(Point(x - 1, it))
            if (nextRow.contains(it)) yield(Point(x + 1, it))
        }
    }
}


fun main() {
    fun part1(input: List<String>): Int {
        val symbols = SymbolLocation()
        input.forEachIndexed { x, line ->
            line.forEachIndexed { y, symb ->
                if (!symb.isDigit() && symb != '.') symbols.putSymbol(x, y)
            }
        }
        val numRegex = Regex("\\d+")
        return input.flatMapIndexed { x, line ->
            numRegex.findAll(line).map { found ->
                if (symbols.hasSymbolAround(x, found.range)) {
                    found.value.toInt()
                } else
                    0
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val symbols = SymbolLocation()
        input.forEachIndexed { x, line ->
            line.forEachIndexed { y, symb ->
                if (symb == '*') symbols.putSymbol(x, y)
            }
        }
        val gears: MutableMap<Point, MutableList<Int>> = hashMapOf()
        val numRegex = Regex("\\d+")
        input.flatMapIndexed { x, line ->
            numRegex.findAll(line).map { found ->
                symbols.adjacentSymbols(x, found.range).forEach { symbol ->
                    val parts = gears.getOrDefault(symbol, mutableListOf())
                    parts.add(found.value.toInt())
                    gears[symbol] = parts
                }
            }
        }
        return gears.values.filter { it.size == 2 }.sumOf { it[0] * it[1] }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part1(readInput("Day03")) == 540025)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
