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
        return Intcode.newProgram(input).compute().memory
    }
}

class Intcode(
    val memory: List<Int>,
    private val currentAddress: Int,
    private val input: List<Int>,
    val output: List<Int>
) {
    companion object {
        fun newProgram(memory: List<Int>, input: List<Int> = emptyList()): Intcode {
            return Intcode(memory, 0, input, emptyList())
        }
    }

    fun compute(): Intcode {
        return if (this.memory[currentAddress] == 99) {
            this
        } else {
            computeStep().compute()
        }
    }

    private fun computeStep(): Intcode {
        return when (memory[currentAddress] % 100) {
            1 -> computeNextSum()
            2 -> computeNextMul()
            3 -> input()
            4 -> output()
            5 -> jumpIfTrue()
            6 -> jumpIfFalse()
            7 -> lessThan()
            8 -> equals()
            else -> throw IllegalStateException("Instruction not valid!")
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
        return Intcode(this.memory.toMutableList().apply { f(n1, n2, resPos) }, currentAddress + 4, input, output)
    }

    private fun input(): Intcode {
        val n1 = this.input[0]
        val pos = getAddress(1)
        val newMemory = this.memory.toMutableList().apply { this[pos] = n1 }
        return Intcode(newMemory, currentAddress + 2, input.drop(1), output)
    }

    private fun output(): Intcode {
        val pos = getAddress(1)
        return Intcode(memory, currentAddress + 2, input, output + this.memory[pos])
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
        return Intcode(memory, jump, input, output)
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
}

fun Boolean.toInt() = if (this) 1 else 0