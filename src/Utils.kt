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

data class Point(val x: Int, val y: Int)
data class PointL(val x: Long, val y: Long)
