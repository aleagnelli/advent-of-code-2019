object Day5 {
    fun part1Output(ints: List<Int>): List<Int> {
        return intcodeOutput(ints, listOf(1))
    }

    fun part2Output(ints: List<Int>): List<Int> {
        return intcodeOutput(ints, listOf(5))
    }

    private fun intcodeOutput(ints: List<Int>, input: List<Int>): List<Int> {
        return Intcode.newProgram(ints, input).compute().output
    }

}