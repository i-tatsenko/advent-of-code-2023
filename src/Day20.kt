import java.util.LinkedList

enum class PType {
    LOW, HIGH
}

sealed class Mod(val name: String, val output: List<String>) {

    abstract fun registerInput(name: String)
    abstract fun process(from: String, type: PType): List<Pulse>
    class FlipFlop(name: String, output: List<String>) : Mod(name, output) {

        private var isOn = false

        private val cache = mutableMapOf<Pair<Boolean, PType>, List<Pulse>>()

        override fun registerInput(name: String) {
            // noop
        }

        override fun process(from: String, type: PType): List<Pulse> {
            if (type == PType.HIGH) return listOf()
            isOn = !isOn
            val cacheKey = isOn to type
            val cached = cache[cacheKey]
            if (cached != null) return cached
            val result = output.map { Pulse(name, if (isOn) PType.HIGH else PType.LOW, it) }
            cache[cacheKey] = result
            return result
        }
    }

    class Conjunction(name: String, output: List<String>) : Mod(name, output) {

        private val inputs = mutableMapOf<String, PType>()

        private val cache = mutableMapOf<Pair<Boolean, PType>, List<Pulse>>()

        override fun registerInput(name: String) {
            inputs[name] = PType.LOW
        }

        override fun process(from: String, type: PType): List<Pulse> {
            inputs[from] = type
            val allHigh = inputs.values.all { it == PType.HIGH }
            val cacheKey = allHigh to type
            val cached = cache[cacheKey]
            if (cached != null) return cached
            val newPulseType = if (allHigh) PType.LOW else PType.HIGH
            val result = output.map { Pulse(name, newPulseType, it) }
            cache[cacheKey] = result
            return result
        }
    }

    class Broadcast(output: List<String>) : Mod("broadcast", output) {
        override fun registerInput(name: String) = throw RuntimeException("Broadcast module doesn't have inputs")

        override fun process(from: String, type: PType) = output.map { Pulse(name, type, it) }
    }
}

data class Pulse(val from: String, val type: PType, val to: String)

fun parseModules(input: List<String>) = input.map {
    val splited = it.split(" -> ")
    val name = splited[0].drop(1)
    val output = splited[1].split(", ")
    when (splited[0][0]) {
        '%' -> Mod.FlipFlop(name, output)
        '&' -> Mod.Conjunction(name, output)
        else -> Mod.Broadcast(output)
    }
}

fun Map<String, Mod>.simulate(listenForEmittingHigh: Set<String> = setOf()): Pair<Pair<Long, Long>, List<String>> {
    var lowPulses = 0L
    var highPulses = 0L
    val queue = LinkedList<List<Pulse>>()
    queue.add(listOf(Pulse("button", PType.LOW, "broadcast")))
    val fired = mutableListOf<String>()
    while (queue.isNotEmpty()) {
        queue.pop().forEach { (from, type, to) ->
            if (type == PType.LOW) lowPulses++ else highPulses++
            if (type == PType.HIGH && from in listenForEmittingHigh) fired.add(from)
            this[to]?.also { queue.add(it.process(from, type)) }
        }
    }
    return (lowPulses to highPulses) to fired
}

fun prepareModules(input: List<String>): Map<String, Mod> {
    val modules = parseModules(input).associateBy { it.name }
    modules.values.forEach { mod ->
        mod.output.forEach {
            modules[it]?.registerInput(mod.name)
        }
    }
    return modules
}

fun main() {
    fun part1(input: List<String>): Long {
        val modules = prepareModules(input)
        var lowPulses = 0L
        var highPulses = 0L
        for (i in 1..1000) {
            val (low, high) = modules.simulate().component1()
            lowPulses += low
            highPulses += high
        }
        return lowPulses * highPulses
    }

    fun part2(input: List<String>): Long {
        val modules = prepareModules(input)
        val directConnectionToRx = modules.values.first { "rx" in it.output }.name
        val waitForHighPulseNames = modules.values.filter { directConnectionToRx in it.output }.map { it.name }.toSet()
        val cycles = mutableMapOf<String, Long>()
        var cnt = 1L
        while (cycles.size != waitForHighPulseNames.size) {
            val (_, fired) = modules.simulate(waitForHighPulseNames)
            fired.forEach { cycles.computeIfAbsent(it) { cnt } }
            cnt++
        }
        return cycles.values.reduce { a, b -> lcm(a, b) }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day20_test")) == 32000000L)
    check(part1(readInput("Day20")) == 788081152L)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}

//793830008203336 is too high
