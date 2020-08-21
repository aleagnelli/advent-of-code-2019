import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day2Spec : StringSpec({
    "Intcode" {
        Day2.intcode(listOf(1, 0, 0, 0, 99)) shouldBe Intcode.toMemory(listOf(2, 0, 0, 0, 99))
        Day2.intcode(listOf(2, 3, 0, 3, 99)) shouldBe Intcode.toMemory(listOf(2, 3, 0, 6, 99))
        Day2.intcode(listOf(2, 4, 4, 5, 99, 0)) shouldBe Intcode.toMemory(listOf(2, 4, 4, 5, 99, 9801))
        Day2.intcode(listOf(1, 1, 1, 4, 99, 5, 6, 0, 99)) shouldBe Intcode.toMemory(listOf(30, 1, 1, 4, 2, 5, 6, 0, 99))

        val expected = Intcode.toMemory(listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50))
        Day2.intcode(listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)) shouldBe expected
    }

    "answer" {
        val ints = Resources.read(2, ",").map { it.toLong() }

        val answer = Day2.part1(ints).getValue(0)
        answer shouldBe 3_101_878L
        val answer2 = Day2.part2(ints)
        answer2 shouldBe 8444L
        AnswerPrinter.print(this, answer, answer2)
    }
})