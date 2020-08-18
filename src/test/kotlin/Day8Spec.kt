import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day8Spec : StringSpec({
    "answer"  {
        val program = Resources.read(8, "")
            .drop(1).dropLast(1)
            .map { it.toInt() }

        val answer = Day8.part1(program)
        answer shouldBe 1474
        val answer2 = Day8.part2(program)
        answer2 shouldBe """  ##  ##  ###   ##  ###  
                           |   # #  # #  # #  # #  # 
                           |   # #    #  # #    ###  
                           |   # #    ###  #    #  # 
                           |#  # #  # # #  #  # #  # 
                           | ##   ##  #  #  ##  ###  """.trimMargin()

        AnswerPrinter.print(8, answer, "\n" + answer2)
    }
})