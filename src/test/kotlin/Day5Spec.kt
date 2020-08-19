import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day5Spec : StringSpec({

    "new instructions" {
        Intcode.newProgram(listOf(3, 0, 99), listOf(4)).computeUntilEnd().memory shouldBe Intcode.toMemory(listOf(4L, 0, 99))
        Intcode.newProgram(listOf(4, 0, 99)).computeUntilEnd().getOutput() shouldBe listOf(4L)
        Intcode.newProgram(listOf(3, 0, 4, 0, 99), listOf(1)).computeUntilEnd().getOutput() shouldBe listOf(1L)
    }

    "new mode" {
        Intcode.newProgram(listOf(1002L, 4, 3, 4, 33)).computeUntilEnd().memory shouldBe Intcode.toMemory(listOf(1002L, 4, 3, 4, 99))
    }

    "new instruction 2" {
        val equalsToEight = listOf(3L, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8)
        Intcode.newProgram(equalsToEight, listOf(8)).computeUntilEnd().getOutput() shouldBe listOf(1L)
        Intcode.newProgram(equalsToEight, listOf(984)).computeUntilEnd().getOutput() shouldBe listOf(0L)

        val equalsToEightImmediateMode = listOf(3L, 3, 1108, -1, 8, 3, 4, 3, 99)
        Intcode.newProgram(equalsToEightImmediateMode, listOf(8)).computeUntilEnd().getOutput() shouldBe listOf(1L)
        Intcode.newProgram(equalsToEightImmediateMode, listOf(984)).computeUntilEnd().getOutput() shouldBe listOf(0L)

        val lessThanEight = listOf(3L, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8)
        Intcode.newProgram(lessThanEight, listOf(3)).computeUntilEnd().getOutput() shouldBe listOf(1L)
        Intcode.newProgram(lessThanEight, listOf(8)).computeUntilEnd().getOutput() shouldBe listOf(0L)

        val lessThanEightImmediateMode = listOf(3L, 3, 1107, -1, 8, 3, 4, 3, 99)
        Intcode.newProgram(lessThanEightImmediateMode, listOf(3)).computeUntilEnd().getOutput() shouldBe listOf(1L)
        Intcode.newProgram(lessThanEightImmediateMode, listOf(8)).computeUntilEnd().getOutput() shouldBe listOf(0L)

        val zeroIfInputZero = listOf(3L, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9)
        Intcode.newProgram(zeroIfInputZero, listOf(8)).computeUntilEnd().getOutput() shouldBe listOf(1L)
        Intcode.newProgram(zeroIfInputZero, listOf(0)).computeUntilEnd().getOutput() shouldBe listOf(0L)

        val zeroIfInputZeroImmediateMode = listOf(3L, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1)
        Intcode.newProgram(zeroIfInputZeroImmediateMode, listOf(8)).computeUntilEnd().getOutput() shouldBe listOf(1L)
        Intcode.newProgram(zeroIfInputZeroImmediateMode, listOf(0)).computeUntilEnd().getOutput() shouldBe listOf(0L)

        val belowEqualGreaterEight = listOf(
            3L, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
            1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
            999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        )
        Intcode.newProgram(belowEqualGreaterEight, listOf(1)).computeUntilEnd().getOutput() shouldBe listOf(999L)
        Intcode.newProgram(belowEqualGreaterEight, listOf(8)).computeUntilEnd().getOutput() shouldBe listOf(1000L)
        Intcode.newProgram(belowEqualGreaterEight, listOf(10)).computeUntilEnd().getOutput() shouldBe listOf(1001L)
    }

    "answer" {
        val ints = Resources.read(5, ",").map { it.toLong() }

        val answer = Day5.part1Output(ints)
        answer shouldBe listOf(0L, 0, 0, 0, 0, 0, 0, 0, 0, 15386262)
        val answer2 = Day5.part2Output(ints)
        answer2 shouldBe listOf(10376124L)
        AnswerPrinter.print(5, answer, answer2)
    }
})