import java.lang.Math.PI
import kotlin.math.abs
import kotlin.math.atan2

object Day10 {

    fun part1(map: String): Pair<Pair<Int, Int>, Int> {
        val asteroids = map.lines()
            .withIndex()
            .flatMap { l ->
                l.value
                    .withIndex()
                    .filter { it.value == '#' }
                    .map { Pair(it.index, -l.index) }
            }
        return asteroids.map { stationSelected ->
            val asteroidsInLos = asteroids
                .filter { it != stationSelected }
                .map { angle(stationSelected, it) }
                .distinct()
                .size
            stationSelected to asteroidsInLos
        }
            .toMap()
            .maxByOrNull { it.value }!!
            .toPair()
    }


    fun part2(map: String, station: Pair<Int, Int>): Int {
        val asteroids = map.lines()
            .withIndex()
            .flatMap { l ->
                l.value
                    .withIndex()
                    .filter { it.value == '#' }
                    .map { Pair(it.index, -l.index) }
            }
        val targets = asteroids
            .filter { it != station }
            .map { angle(station, it) to it }
            .sortedByDescending { it.first }
            .groupBy { it.first }
            .mapValues { a ->
                a.value.map { it.second to distance(station, it.second) }.sortedBy { it.second }
            }

        val keys = sequence { while (true) yield(targets.keys.toList()) }.flatten()

        tailrec fun go(
            keys: Iterator<Double>,
            targets: Map<Double, List<Pair<Pair<Int, Int>, Int>>>,
            count: Int
        ): Pair<Pair<Int, Int>, Int> {
            val next = keys.next()
            return if (targets.getValue(next).isEmpty()) {
                go(keys, targets, count)
            } else {
                if (count == 200) {
                    targets.getValue(next)[0]
                } else {
                    val newMap = targets.toMutableMap()
                        .apply { this[next] = this.getValue(next).toMutableList().apply { this.removeAt(0) } }
                        .toMap()
                    go(keys, newMap, count + 1)
                }
            }
        }


        val lastTarget = go(keys.iterator(), targets, 1).first

        // parsed as negative, subtract instead of add
        return lastTarget.first * 100 - lastTarget.second

    }

    private fun angle(station: Pair<Int, Int>, asteroids: Pair<Int, Int>): Double {
        val dx = asteroids.first - station.first
        val dy = asteroids.second - station.second
        val rotatedAngle = atan2(dy.toDouble(), dx.toDouble()) * 180 / PI
        return rotatedAngle - (if (rotatedAngle <= 180 && rotatedAngle > 90) 360 else 0)
    }

    private fun distance(station: Pair<Int, Int>, asteroids: Pair<Int, Int>): Int {
        return abs(asteroids.first - station.first) + abs(asteroids.second - station.second)
    }

}