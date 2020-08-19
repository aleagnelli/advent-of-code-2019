object Day9 {
    fun part1(program: List<Long>): List<Long> {
        return Intcode.newProgram(program, listOf(1)).computeUntilEnd().getOutput()
    }

    fun part2(program: List<Long>): List<Long> {
        return Intcode.newProgram(program, listOf(2)).computeUntilEnd().getOutput()
    }
}