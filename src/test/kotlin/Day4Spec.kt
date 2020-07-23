import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day4Spec : StringSpec({

    "checkRules" {
        Day4.checkRules("1") shouldBe false
        Day4.checkRules("111111") shouldBe true
        Day4.checkRules("223450") shouldBe false
        Day4.checkRules("123789") shouldBe false
    }

    "newRule" {
        Day4.newRule("112233") shouldBe true
        Day4.newRule("123444") shouldBe false
        Day4.newRule("111122") shouldBe true
    }

    "answer" {
        val answer = Day4.differentPassword(256310, 732736)
        answer shouldBe 979
        val answer2 = Day4.differentPassword2(256310,732736)
        answer2 shouldBe 635
        println("day4")
        println("answer1: $answer")
        println("answer2: $answer2")
    }
})