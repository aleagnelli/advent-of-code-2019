import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day3Spec : StringSpec({
    "distance" {
        Day3.distance(listOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")) shouldBe 159
        Day3.distance(
            listOf(
                "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
                "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
            )
        ) shouldBe 135
    }

    "bestIntersection" {
        Day3.bestIntersection(
            listOf(
                "R75,D30,R83,U83,L12,D49,R71,U7,L72",
                "U62,R66,U55,R34,D71,R55,D58,R83"
            )
        ) shouldBe 610
        Day3.bestIntersection(
            listOf(
                "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
                "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
            )
        ) shouldBe 410
    }

    "answer" {
        val directions = Day3::class.java.getResource("day3.txt")
            .readText()
            .split("\n")

        val answer = Day3.distance(directions)
        answer shouldBe 2427
        val answer2 = Day3.bestIntersection(directions)
        answer2 shouldBe 27890
        println("day3")
        println("answer1: $answer")
        println("answer2: $answer2")
    }
})