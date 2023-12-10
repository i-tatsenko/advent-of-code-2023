import kotlin.math.min


class HandPart2(input: String) : Hand(input) {
    override val rank: Int by lazy {
        val cardsNoJ = this.cards.filter { it != 'J' }
        val jCount = this.cards.length - cardsNoJ.length
        val superGroups = super.groups(cardsNoJ).toMutableList()

        if (superGroups.isEmpty()) computeRank(listOf(min(jCount + 1, 5))) else {
            superGroups[0] += jCount
            computeRank(superGroups)
        }
    }

    override fun cardValue(it: Char): Int {
        return if (it == 'J') 1
        else super.cardValue(it)
    }
}

const val FIVE_OF_KIND = 7
const val FOUR_OF_KIND = 6
const val FULL_HOUSE = 5
const val THREE_OF_KIND = 4
const val TWO_PAIRS = 3
const val PAIR = 2

open class Hand(input: String) : Comparable<Hand> {
    val bid: Int
    protected val cards: String

    init {
        val splitted = input.split(" ")
        this.cards = splitted[0]
        this.bid = splitted[1].toInt()
    }

    open fun groups(cardsOnHand: String) =
        cardsOnHand.toCharArray().groupBy { it }.values.map { it.size }.filter { it > 1 }.sortedDescending()

    open val rank: Int by lazy {
        val groups = groups(this.cards)
        computeRank(groups)
    }

    protected fun computeRank(groups: List<Int>) = when {
        groups.isEmpty() -> 0
        groups[0] == FULL_HOUSE -> FIVE_OF_KIND
        groups[0] == THREE_OF_KIND -> FOUR_OF_KIND
        groups.size == PAIR && groups[0] == TWO_PAIRS && groups[1] == PAIR -> FULL_HOUSE
        groups[0] == TWO_PAIRS -> THREE_OF_KIND
        groups.size == PAIR && groups[0] == PAIR && groups[1] == PAIR -> TWO_PAIRS
        groups[0] == PAIR -> PAIR
        else -> 0
    }

    override fun compareTo(other: Hand): Int {
        if (this.rank != other.rank) return this.rank - other.rank
        for ((l, r) in this.cardValues().zip(other.cardValues())) {
            if (l != r) return l - r
        }
        return 0
    }

    private fun cardValues() = sequence {
        yieldAll(cards.toCharArray().map {
            cardValue(it)
        })
    }

    open fun cardValue(it: Char) = when (it) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 11
        'T' -> 10
        else -> it.digitToInt()
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { Hand(it) }.sorted().withIndex().sumOf {
            (it.index + 1) * it.value.bid
        }
    }

    fun part2(input: List<String>): Int {
        return input.map { HandPart2(it) }.sorted().withIndex().sumOf {
            (it.index + 1) * it.value.bid
        }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day07_test")) == 6440)
    check(part1(readInput("Day07")) == 252656917)
    check(part2(readInput("Day07_test")) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
// 253214796 is too low