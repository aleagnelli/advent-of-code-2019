import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day2Spec : StringSpec({
    "Intcode" {
        Day2.intcode(listOf(1, 0, 0, 0, 99)) shouldBe listOf(2, 0, 0, 0, 99)
        Day2.intcode(listOf(2, 3, 0, 3, 99)) shouldBe listOf(2, 3, 0, 6, 99)
        Day2.intcode(listOf(2, 4, 4, 5, 99, 0)) shouldBe listOf(2, 4, 4, 5, 99, 9801)
        Day2.intcode(listOf(1, 1, 1, 4, 99, 5, 6, 0, 99)) shouldBe listOf(30, 1, 1, 4, 2, 5, 6, 0, 99)

        val expected = listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)
        Day2.intcode(listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)) shouldBe expected
    }

    "answer" {
        val ints = Day2::class.java.getResource("day2.txt")
            .readText()
            .split(",")
            .map { it.toInt() }

        val answer = Day2.part1(ints)[0]
        answer shouldBe 3_101_878
        val answer2 = Day2.part2(ints)
        answer2 shouldBe 8444
        println("day2")
        println("answer1: $answer")
        println("answer2: $answer2")
    }
})