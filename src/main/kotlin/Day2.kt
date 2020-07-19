object Day2 {

    fun part1(input: List<Int>): List<Int> {
        return intcode(input, 12, 2)
   }

    fun part2(input: List<Int>): Int {
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                if(intcode(input, noun, verb)[0] == 19_690_720){
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
        return Intcode(input, 0).compute().memory
    }
}

class Intcode(val memory: List<Int>, private val currentAddress: Int) {
    fun compute(): Intcode {
        return if (this.memory[currentAddress] == 99) {
            this
        } else {
            Intcode(computeStep(), currentAddress + 4).compute()
        }
    }

    private fun computeStep(): List<Int> {
        return when (memory[currentAddress]) {
            1 -> computeNextSum()
            2 -> computeNextMul()

            else -> this.memory
        }
    }

    private fun computeNextSum(): List<Int> {
        return computeStep { n1, n2, pos -> this[pos] = n1 + n2 }
    }

    private fun computeNextMul(): List<Int> {
        return computeStep { n1, n2, pos -> this[pos] = n1 * n2 }
    }

    private fun computeStep(f: MutableList<Int>.(Int, Int, Int) -> Unit): List<Int> {
        val n1 = memory[memory[currentAddress + 1]]
        val n2 = memory[memory[currentAddress + 2]]
        val resPos = memory[currentAddress + 3]
        return this.memory.toMutableList().apply {
            f(n1, n2, resPos)
        }
    }
}