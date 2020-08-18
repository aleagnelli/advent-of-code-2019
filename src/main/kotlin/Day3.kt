import kotlin.math.abs

object Day3 {
    fun distance(input: List<String>): Int {
        val points0 = getWirePoints(input[0])
        val points1 = getWirePoints(input[1])
        val intersect = points0.intersect(points1)
        return getDistances(intersect).minOrNull()!!
    }

    fun bestIntersection(input: List<String>): Int {
        val points0 = getWirePoints(input[0])
        val points1 = getWirePoints(input[1])
        val intersect = points0.intersect(points1)
        return intersect.map { points0.indexOf(it) + points1.indexOf(it) + 2 }.minOrNull()!!
    }

    private fun getWirePoints(directions: String): List<Point> {
        return directions
            .split(",")
            .fold(emptyList(), { points, move ->
                val previous = points.lastOrNull() ?: Point.origin()
                val newPoints = getPointsOfMove(previous, move.drop(1).toInt()) { point -> point.move(move.first()) }
                points.toMutableList().apply { addAll(newPoints) }
            })
    }

    private fun getPointsOfMove(point: Point, amount: Int, move: (Point) -> Point): List<Point> {
        fun go(list: List<Point>, amount: Int, move: (Point) -> Point): List<Point> {
            return if (amount == 0) {
                list
            } else {
                val new = move(list.last())
                go(list.toMutableList().apply { add(new) }, amount - 1, move)
            }
        }

        return go(listOf(point), amount, move).drop(1)
    }

    private fun getDistances(points: Set<Point>): List<Int> {
        return points.map { it.distance() }
    }


    data class Point(val x: Int, val y: Int) {
        companion object {
            fun origin() = Point(0, 0)
        }

        fun move(move: Char): Point {
            return when (move) {
                'U' -> this.up()
                'D' -> this.down()
                'R' -> this.right()
                'L' -> this.left()
                else -> throw IllegalArgumentException("Invalid direction")
            }
        }

        private fun up(): Point = copy(y = y + 1)

        private fun down(): Point = copy(y = y - 1)

        private fun right(): Point = copy(x = x + 1)

        private fun left(): Point = copy(x = x - 1)

        fun distance(): Int = abs(x) + abs(y)

    }
}