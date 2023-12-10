fun direction(input: String) = sequence {
    var index = 0
    while (true)
        yield(input[index++ % input.length])
}

class Node(val name: String, var left: Node?, var right: Node?) {
    constructor(name: String) : this(name, null, null)

}

fun determineLoop(root: Node, directions: String): List<Long> {
    val nodesByDirectionIndex = mutableMapOf<Int, MutableSet<Node>>()
    val exits = mutableListOf<Long>()
    var moves = 0L
    var node = root
    var directionIndex = 0
    while (true) {
        moves++
        node = if (directions[directionIndex] == 'L') node.left!! else node.right!!
        val visited = nodesByDirectionIndex.computeIfAbsent(directionIndex) { mutableSetOf() }
        if (visited.contains(node))
            return exits
        visited.add(node)
        if (node.name.endsWith('Z'))
            exits.add(moves)
        directionIndex = (directionIndex + 1) % directions.length
    }
}

fun buildMap(input: List<String>): Map<String, Node> {
    val result = mutableMapOf<String, Node>()
    val mapRegex = "(\\w{3}) = \\((\\w{3}), (\\w{3})\\)".toRegex()
    input.forEach {
        val found = mapRegex.find(it) ?: throw RuntimeException("Failed to parse the map")
        val root = result.computeIfAbsent(found.groupValues[1]) { Node(found.groupValues[1]) }
        root.left = result.computeIfAbsent(found.groupValues[2]) { Node(found.groupValues[2]) }
        root.right = result.computeIfAbsent(found.groupValues[3]) { Node(found.groupValues[3]) }
    }
    return result
}

fun main() {
    fun part1(input: List<String>): Int {
        val nodes = buildMap(input.drop(2))
        var node = nodes["AAA"]!!
        var moves = 0
        for (d in direction(input[0])) {
            moves++
            node = if (d == 'L') node.left!! else node.right!!
            if (node.name == "ZZZ") return moves
        }
        return moves
    }

    fun part2(input: List<String>): Long {
        val nodes = buildMap(input.drop(2))
        val loops =
            nodes.filter { (k) -> k.endsWith('A') }
                .map { it.component2() }
                .map { determineLoop(it, input[0]).last() }
        val iterator  = loops.max()
        var moves: Long = iterator
        while (!loops.all { moves % it == 0L }) moves += iterator
        return moves
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day08_test")) == 6)
    check(part1(readInput("Day08")) == 14681)

    check(part2(readInput("Day08_test_2")).toInt() == 6)
    println("tests passed")
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
