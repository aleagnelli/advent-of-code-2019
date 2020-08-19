import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking
import kotlin.math.pow

object Day2 {

    fun part1(input: List<Long>): Map<Long, Long> {
        return intcode(input, 12, 2)
    }

    fun part2(input: List<Long>): Long {
        (0..99L).forEach { noun ->
            (0..99L).forEach { verb ->
                if (intcode(input, noun, verb)[0] == 19_690_720L) {
                    return 100 * noun + verb
                }
            }
        }
        throw NoSuchElementException("No combination of noun and verb found.")
    }

    fun intcode(input: List<Long>, noun: Long, verb: Long): Map<Long, Long> {
        val realInput = input.toMutableList()
            .apply {
                this[1] = noun
                this[2] = verb
            }
        return intcode(realInput)
    }

    fun intcode(input: List<Long>): Map<Long, Long> {
        return Intcode.newProgram(input).computeUntilEnd().memory
    }
}

class Intcode(
    val memory: Map<Long, Long>,
    private val currentAddress: Long,
    private val relativeBase: Long,
    val inputChannel: Channel<Long>,
    val outputChannel: Channel<Long>
) {
    companion object {
        fun newProgram(program: List<Long>, input: List<Long> = emptyList()): Intcode {
            val inputChannel = Channel<Long>(Channel.UNLIMITED)
            runBlocking {
                input.forEach { inputChannel.send(it) }
            }
            return withInputChannel(program, inputChannel)
        }

        fun withInputChannel(program: List<Long>, inputChannel: Channel<Long>): Intcode {
            val mem: Map<Long, Long> = toMemory(program)
            return Intcode(mem, 0, 0, inputChannel, Channel(Channel.UNLIMITED))
        }

        fun toMemory(program: List<Long>): Map<Long, Long> {
            return program
                .withIndex()
                .associateTo(mutableMapOf()) { it.index.toLong() to it.value }
                .toMap()
        }
    }

    fun computeUntilEnd(): Intcode {
        val me = this
        tailrec fun go(computer: Intcode): Intcode {
            val new = runBlocking { computer.compute() }
            return if (new.outputChannel.isClosedForSend) {
                new
            } else {
                go(new)
            }
        }

        return go(me)
    }

    private suspend fun compute(): Intcode {
        return if (this.memory[currentAddress] == 99L) {
            this.outputChannel.close()
            this
        } else {
            computeStep()
        }
    }

    private suspend fun computeStep(): Intcode {
        return when (val instruction = (memory.getValue(currentAddress) % 100).toInt()) {
            1 -> computeNextSum()
            2 -> computeNextMul()
            3 -> input()
            4 -> output()
            5 -> jumpIfTrue()
            6 -> jumpIfFalse()
            7 -> lessThan()
            8 -> equals()
            9 -> adjustRelativeBase()
            else -> throw IllegalStateException("Instruction $instruction not valid!")
        }
    }

    private fun computeNextSum(): Intcode {
        return computeStep { n1, n2, pos -> this[pos] = n1 + n2 }
    }

    private fun computeNextMul(): Intcode {
        return computeStep { n1, n2, pos -> this[pos] = n1 * n2 }
    }

    private fun computeStep(f: MutableMap<Long, Long>.(Long, Long, Long) -> Unit): Intcode {
        val n1 = readMemory(getAddress(1))
        val n2 = readMemory(getAddress(2))
        val resPos = getAddress(3)
        return Intcode(
            this.memory.toMutableMap().apply { f(n1, n2, resPos) },
            currentAddress + 4,
            relativeBase,
            inputChannel,
            outputChannel
        )
    }

    private suspend fun input(): Intcode {
        val n1 = this.inputChannel.receive()
        val pos = getAddress(1)
        val newMemory = this.memory.toMutableMap().apply { this[pos] = n1 }
        return Intcode(newMemory, currentAddress + 2, relativeBase, inputChannel, outputChannel)
    }

    private suspend fun output(): Intcode {
        val pos = getAddress(1)
        outputChannel.send(this.memory.getValue(pos))
        return Intcode(memory, currentAddress + 2, relativeBase, inputChannel, outputChannel)
    }

    private fun jumpIfTrue(): Intcode {
        return jumpIf { it != 0L }
    }

    private fun jumpIfFalse(): Intcode {
        return jumpIf { it == 0L }
    }

    private fun jumpIf(c: (Long) -> Boolean): Intcode {
        val n1 = readMemory(getAddress(1))
        val jump = if (c(n1)) readMemory(getAddress(2)) else (currentAddress + 3)
        return Intcode(memory, jump, relativeBase, inputChannel, outputChannel)
    }

    private fun lessThan(): Intcode {
        return computeStep { n1, n2, pos -> this[pos] = (n1 < n2).toLong() }
    }

    private fun equals(): Intcode {
        return computeStep { n1, n2, pos -> this[pos] = (n1 == n2).toLong() }
    }

    private fun adjustRelativeBase(): Intcode {
        val offset = readMemory(getAddress(1))
        return Intcode(memory, currentAddress + 2, relativeBase + offset, inputChannel, outputChannel)
    }

    private fun getAddress(param: Long): Long {
        return when (getMode(param - 1)) {
            0 -> readMemory(currentAddress + param)
            1 -> currentAddress + param
            2 -> relativeBase + readMemory(currentAddress + param)
            else -> throw IllegalStateException("Mode not valid")
        }
    }

    private fun getMode(param: Long): Int {
        return (readMemory(currentAddress) / 10.0.pow(2 + param.toDouble())).toInt() % 10
    }

    private fun readMemory(address: Long) = this.memory.getOrDefault(address, 0L)

    fun getOutput(): List<Long> = runBlocking {
        outputChannel.toList()
    }
}

fun Boolean.toLong(): Long = if (this) 1L else 0L