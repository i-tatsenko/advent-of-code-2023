import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

class PrefixTreeNode<T>() {
    var value: T? = null
    private val children: MutableMap<Char, PrefixTreeNode<T>> = hashMapOf()

    fun add(key: String, v: T) {
        if (key.isEmpty()) this.value = v
        else {
            val child = children.getOrDefault(key[0], PrefixTreeNode())
            child.add(key.substring(1), v)
            children[key[0]] = child
        }
    }

    fun node(ch: Char): PrefixTreeNode<T>? {
        return children[ch]
    }
}

fun numbersL(s: String) = sequence {
    val regex = "-?\\d+".toRegex()
    yieldAll(regex.findAll(s).map { it.value.toLong() })
}

fun numbers(s: String) = sequence {
    yieldAll(numbersL(s).map { it.toInt() })
}

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun invoke(toX: Int, toY: Int) = Point(x + toX, y + toY)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun times(v: Int) = Point(x * v, y * v)

    fun inBounds(xBound: Int, yBound: Int): Boolean = x in 0..<xBound && y in 0..<yBound
    override fun compareTo(other: Point): Int {
        val thisDist = x + y
        val otherDist = other.x + other.y
        if (thisDist - otherDist != 0) return thisDist - otherDist
        return x - other.x
    }
}

data class PointL(val x: Long, val y: Long)
enum class Dir(private val mask: Point) {
    UP(Point(-1, 0)),
    DOWN(Point(1, 0)),
    LEFT(Point(0, -1)),
    RIGHT((Point(0, 1)));

    fun next(p: Point): Point = p + mask

    fun isOpposite(other: Dir) = when(this) {
        UP -> other == DOWN
        DOWN -> other == UP
        LEFT -> other == RIGHT
        RIGHT -> other == LEFT
    }
}