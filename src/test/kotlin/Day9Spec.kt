import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day9Spec : StringSpec({
    "relative mode and new instruction" {
        Intcode.newProgram(
            listOf(109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99)
        ).computeUntilEnd()
            .getOutput() shouldBe listOf(109L, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99)
        Intcode.newProgram(
            listOf(1102, 34915192, 34915192, 7, 4, 7, 99, 0)
        ).computeUntilEnd().getOutput() shouldBe listOf(1219070632396864L)
        Intcode.newProgram(listOf(104, 1125899906842624L, 99))
            .computeUntilEnd().getOutput() shouldBe listOf(1125899906842624L)
    }

    "answer" {
        val program = Resources.read(9, ",").map { it.toLong() }

        val answer = Day9.part1(program)[0]
        answer shouldBe 2351176124L
        val answer2 = Day9.part2(program)[0]
        answer2 shouldBe 73110L

        AnswerPrinter.print(9, answer, answer2)
    }
})