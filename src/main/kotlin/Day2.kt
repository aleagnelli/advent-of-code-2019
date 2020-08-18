import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking
import kotlin.math.pow

object Day2 {

    fun part1(input: List<Int>): List<Int> {
        return intcode(input, 12, 2)
    }

    fun part2(input: List<Int>): Int {
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                if (intcode(input, noun, verb)[0] == 19_690_720) {
                    return 100 * noun + verb
                }
            }
        }
        throw NoSuchElementException("No combination of noun and verb found.")
    }

    fun intcode(input: List<Int>, noun: Int, verb: Int): List<Int> {
        val realInput = input.toMutableList()
            .apply {
                this[1] = noun
                this[2] = verb
            }
        return intcode(realInput)
    }

    fun intcode(input: List<Int>): List<Int> {
        return runBlocking { Intcode.newProgram(input).compute().memory }
    }
}

class Intcode(
    val memory: List<Int>,
    private val currentAddress: Int,
    val inputChannel: Channel<Int>,
    val outputChannel: Channel<Int>
) {
    companion object {
        fun newProgram(memory: List<Int>, input: List<Int> = emptyList()): Intcode {
            val inputChannel = Channel<Int>(Channel.UNLIMITED)
            runBlocking {
                input.forEach { inputChannel.send(it) }
            }
            return withInputChannel(memory, inputChannel)
        }

        fun withInputChannel(memory: List<Int>, inputChannel: Channel<Int>): Intcode {
            return Intcode(memory, 0, inputChannel, Channel(Channel.UNLIMITED))
        }
    }

    suspend fun compute(): Intcode {
        return if (this.memory[currentAddress] == 99) {
            this.outputChannel.close()
            this
        } else {
            computeStep().compute()
        }
    }

    private suspend fun computeStep(): Intcode {
        return when (val instruction = memory[currentAddress] % 100) {
            1 -> computeNextSum()
            2 -> computeNextMul()
            3 -> input()
            4 -> output()
            5 -> jumpIfTrue()
            6 -> jumpIfFalse()
            7 -> lessThan()
            8 -> equals()
            else -> throw IllegalStateException("Instruction $instruction not valid!")
        }
    }

    private fun computeNextSum(): Intcode {
        return computeStep { n1, n2, pos -> this[pos] = n1 + n2 }
    }

    private fun computeNextMul(): Intcode {
        return computeStep { n1, n2, pos -> this[pos] = n1 * n2 }
    }

    private fun computeStep(f: MutableList<Int>.(Int, Int, Int) -> Unit): Intcode {
        val n1 = memory[getAddress(1)]
        val n2 = memory[getAddress(2)]
        val resPos = getAddress(3)
        return Intcode(
            this.memory.toMutableList().apply { f(n1, n2, resPos) },
            currentAddress + 4,
            inputChannel,
            outputChannel
        )
    }

    private suspend fun input(): Intcode {
        val n1 = this.inputChannel.receive()
        val pos = getAddress(1)
        val newMemory = this.memory.toMutableList().apply { this[pos] = n1 }
        return Intcode(newMemory, currentAddress + 2, inputChannel, outputChannel)
    }

    private suspend fun output(): Intcode {
        val pos = getAddress(1)
        outputChannel.send(this.memory[pos])
        return Intcode(memory, currentAddress + 2, inputChannel, outputChannel)
    }

    private fun jumpIfTrue(): Intcode {
        return jumpIf { it != 0 }
    }

    private fun jumpIfFalse(): Intcode {
        return jumpIf { it == 0 }
    }

    private fun jumpIf(c: (Int) -> Boolean): Intcode {
        val n1 = memory[getAddress(1)]
        val jump = if (c(n1)) memory[getAddress(2)] else currentAddress + 3
        return Intcode(memory, jump, inputChannel, outputChannel)
    }

    private fun lessThan(): Intcode {
        return computeStep { n1, n2, pos -> this[pos] = (n1 < n2).toInt() }
    }

    private fun equals(): Intcode {
        return computeStep { n1, n2, pos -> this[pos] = (n1 == n2).toInt() }
    }

    private fun getAddress(param: Int): Int {
        return when (getMode(param - 1)) {
            0 -> this.memory[currentAddress + param]
            1 -> currentAddress + param
            else -> throw IllegalStateException("Mode not valid")
        }
    }

    private fun getMode(param: Int): Int {
        return (this.memory[currentAddress] / 10.0.pow(2 + param)).toInt() % 10
    }

    fun getOutput(): List<Int> = runBlocking {
        outputChannel.toList()
    }
}

fun Boolean.toInt() = if (this) 1 else 0