object Day5 {
    fun part1Output(ints: List<Long>): List<Long> {
        return intcodeOutput(ints, listOf(1))
    }

    fun part2Output(ints: List<Long>): List<Long> {
        return intcodeOutput(ints, listOf(5))
    }

    private fun intcodeOutput(ints: List<Long>, input: List<Long>): List<Long> {
        return Intcode.newProgram(ints, input).computeUntilEnd().getOutput()
    }

}