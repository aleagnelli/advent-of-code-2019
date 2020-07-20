import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec


class Day1Spec : StringSpec({
    "getFuel" {
        Day1.getFuel(12) shouldBe 2
        Day1.getFuel(14) shouldBe 2
        Day1.getFuel(1969) shouldBe 654
        Day1.getFuel(100_756) shouldBe 33_583
    }

    "answer" {
        val modules = Day1::class.java.getResource("day1.txt")
            .readText()
            .split("\n")
            .map { it.toInt() }
        val answer = modules.map { Day1.getFuel(it) }.sum()
        answer shouldBe 3_305_041
        val answer2 = modules.map { Day1.getTotalFuel(it) }.sum()
        answer2 shouldBe 4_954_710
        println("day1")
        println("answer1: $answer")
        println("answer2: $answer2")
    }

    "getTotalFuel" {
        Day1.getTotalFuel(14) shouldBe 2
        Day1.getTotalFuel(1969) shouldBe 966
        Day1.getTotalFuel(100_756) shouldBe 50_346
    }
})