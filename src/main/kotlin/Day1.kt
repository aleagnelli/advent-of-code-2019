import kotlin.math.floor
import kotlin.math.max

object Day1 {
    fun getFuel(mass: Int): Int {
        return floor(mass.toDouble() / 3).toInt() - 2
    }

    fun getTotalFuel(fuel: Int): Int {
        return if (fuel <= 0) {
            0
        } else {
            val fuelNeeded = getFuel(fuel)
            fuelNeeded + max(0, getTotalFuel(fuelNeeded))
        }
    }

}