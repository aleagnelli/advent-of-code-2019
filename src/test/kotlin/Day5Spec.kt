import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day5Spec : StringSpec({

    "new instructions" {
        Intcode.newProgram(listOf(3, 0, 99), listOf(4)).compute().memory shouldBe listOf(4, 0, 99)
        Intcode.newProgram(listOf(4, 0, 99)).compute().output shouldBe listOf(4)
        Intcode.newProgram(listOf(3, 0, 4, 0, 99), listOf(1)).compute().output shouldBe listOf(1)
    }

    "new mode" {
        Intcode.newProgram(listOf(1002, 4, 3, 4, 33)).compute().memory shouldBe listOf(1002, 4, 3, 4, 99)
    }

    "new instruction 2" {
        val equalsToEight = listOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8)
        Intcode.newProgram(equalsToEight, listOf(8)).compute().output shouldBe listOf(1)
        Intcode.newProgram(equalsToEight, listOf(984)).compute().output shouldBe listOf(0)

        val equalsToEightImmediateMode = listOf(3, 3, 1108, -1, 8, 3, 4, 3, 99)
        Intcode.newProgram(equalsToEightImmediateMode, listOf(8)).compute().output shouldBe listOf(1)
        Intcode.newProgram(equalsToEightImmediateMode, listOf(984)).compute().output shouldBe listOf(0)

        val lessThanEight = listOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8)
        Intcode.newProgram(lessThanEight, listOf(3)).compute().output shouldBe listOf(1)
        Intcode.newProgram(lessThanEight, listOf(8)).compute().output shouldBe listOf(0)

        val lessThanEightImmediateMode = listOf(3, 3, 1107, -1, 8, 3, 4, 3, 99)
        Intcode.newProgram(lessThanEightImmediateMode, listOf(3)).compute().output shouldBe listOf(1)
        Intcode.newProgram(lessThanEightImmediateMode, listOf(8)).compute().output shouldBe listOf(0)

        val zeroIfInputZero = listOf(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9)
        Intcode.newProgram(zeroIfInputZero, listOf(8)).compute().output shouldBe listOf(1)
        Intcode.newProgram(zeroIfInputZero, listOf(0)).compute().output shouldBe listOf(0)

        val zeroIfInputZeroImmediateMode = listOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1)
        Intcode.newProgram(zeroIfInputZeroImmediateMode, listOf(8)).compute().output shouldBe listOf(1)
        Intcode.newProgram(zeroIfInputZeroImmediateMode, listOf(0)).compute().output shouldBe listOf(0)

        val belowEqualGreaterEight = listOf(
            3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
            1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
            999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        )
        Intcode.newProgram(belowEqualGreaterEight, listOf(1)).compute().output shouldBe listOf(999)
        Intcode.newProgram(belowEqualGreaterEight, listOf(8)).compute().output shouldBe listOf(1000)
        Intcode.newProgram(belowEqualGreaterEight, listOf(10)).compute().output shouldBe listOf(1001)
    }

    "answer" {
        val ints = Resources.read(5, ",").map { it.toInt() }

        val answer = Day5.part1Output(ints)
        answer shouldBe listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 15386262)
        val answer2 = Day5.part2Output(ints)
        answer2 shouldBe listOf(10376124)
        AnswerPrinter.print(5, answer, answer2)
    }
})