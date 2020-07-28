import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day6Spec : StringSpec({
    "getOrbitCountChecksum" {
        Day6(listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L"))
            .getOrbitCountChecksum() shouldBe 42
    }

    "getOrbitalTransfers" {
        Day6(listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN"))
            .getOrbitalTransfers() shouldBe 4
    }

    "answer" {
        val orbits = Resources.read(6, "\n")

        val day6 = Day6(orbits)
        val answer = day6.getOrbitCountChecksum()
        answer shouldBe 270768
        val answer2 = day6.getOrbitalTransfers()
        answer2 shouldBe 451

        AnswerPrinter.print(6, answer, answer2)
    }
})