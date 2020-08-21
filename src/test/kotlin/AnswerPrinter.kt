import io.kotlintest.TestContext

object AnswerPrinter {
    fun print(context: TestContext, answer: Any, answer2: Any) {
        val day = context.description().parent()?.name?.removePrefix("Day")?.removeSuffix("Spec") ?: "0"
        print(day, answer, answer2)
    }

    fun print(day: String, answer: Any, answer2: Any) {
        println("day$day")
        println("answer1: $answer")
        println("answer2: $answer2")
    }
}