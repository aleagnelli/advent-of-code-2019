object Day8 {

    private const val WIDTH = 25
    private const val HEIGHT = 6
    private const val LAYER_LENGTH = WIDTH * HEIGHT

    fun part1(input: List<Int>): Int {
        return toLayers(input)
            .map { l -> Pair(l, l.count { it == 0 }) }
            .minByOrNull { it.second }!!
            .let { (l, _) ->
                val counter = l.groupingBy { it }.eachCount()
                counter.getOrDefault(1, 0) * counter.getOrDefault(2, 0)
            }
    }

    private fun toLayers(input: List<Int>): List<List<Int>> {
        return input.chunked(LAYER_LENGTH)
    }

    fun part2(input: List<Int>): String {
        val layers = toLayers(input)

        return (0 until LAYER_LENGTH)
            .map { p ->
                layers
                    .map { it[p] }
                    .firstOrNull { it != 2 } ?: 2
            }
            .joinToString("") { if (it == 1) "#" else " " }
            .chunked(WIDTH)
            .joinToString("\n")

    }

}