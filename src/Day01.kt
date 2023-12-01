fun main() {
    val nums = PrefixTreeNode<Int>()
    val numsReversed = PrefixTreeNode<Int>()
    val numsToInts = hashMapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
    )
    numsToInts.forEach{(k, v) ->
        nums.add(k, v)
        numsReversed.add(k.reversed(), v)
    }

    fun firstNum(chars: Iterator<Char>, tree: PrefixTreeNode<Int>): Int {
        var guess: MutableList<PrefixTreeNode<Int>> = arrayListOf()
        for (ch in chars) {
            if (ch.isDigit()) return ch.digitToInt()
            guess = guess.mapNotNull {
                val n = it.node(ch)
                if (n?.value != null) return n.value!!
                n
            }.toMutableList()
            tree.node(ch).also { if (it != null) guess.add(it) }
        }
        return 0
    }

    fun num(s: String): Int {
        val first: Int = firstNum(s.iterator(), nums)
        val second = firstNum(s.reversed().iterator(), numsReversed)
        first.println()
        second.println()
        return first * 10 + second
    }

    fun part1(input: List<String>): Int {
        return input.map(::num).sum()
    }

    fun part2(input: List<String>): Int {
        return input.map(::num).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    check(part1(readInput("Day01_test_2")) == 281)
//    check(part1(readInput("Day01")) == 55712)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
