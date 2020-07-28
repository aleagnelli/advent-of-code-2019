import java.util.*

class Day6(private val rawOrbits: List<String>) {
    private val orbitsGraph = parseOrbits()

    fun getOrbitCountChecksum(): Int {
        fun go(orbit: String, path: Int, counter: Int): Int {
            val orbited = orbitsGraph.getOrDefault(orbit, emptySet())
            return if (orbited.isEmpty()) {
                counter + path
            } else {
                orbited.fold(counter + path) { acc, o -> go(o, path + 1, acc) }
            }

        }

        return go("COM", 0, 0)
    }

    private fun parseOrbits(): Map<String, Set<String>> {
        return this.rawOrbits
            .map { it.split(")") }
            .groupBy { it.first() }
            .mapValues { entry ->
                entry.value
                    .flatMap { list -> list.filter { it != entry.key } }
                    .toSet()
            }
    }

    fun getOrbitalTransfers(): Int {
        fun go(currentOrbit: String, dest: String, path: List<String> = emptyList()): Optional<List<String>> {
            val orbited = orbitsGraph.getOrDefault(currentOrbit, emptySet())
            return when {
                orbited.isEmpty() -> Optional.empty()
                orbited.contains(dest) -> Optional.of(path + currentOrbit)
                else -> orbited.stream()
                    .map { o -> go(o, dest, path + currentOrbit) }
                    .filter { it.isPresent }
                    .map { it.get() }
                    .findFirst()
            }
        }

        val you = go("COM", "YOU").get()
        val san = go("COM", "SAN").get()
        val common = you.intersect(san)
        return you.size + san.size - 2 * common.size
    }

}