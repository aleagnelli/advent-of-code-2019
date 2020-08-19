import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Day7 {
    fun part1(program: List<Long>): Long {
        return allPhases(0, 4).map { AmplifiersSeries(program).start(it) }.maxOrNull()!!
    }

    fun part2(program: List<Long>): Long {
        return allPhases(5, 9).map { AmplifiersSeries(program).startWithLoop(it) }.maxOrNull()!!
    }

    private fun allPhases(from: Long, to: Long): List<List<Long>> {
        return if (from == to) {
            listOf(listOf(from))
        } else {
            allPhases(from, to - 1).flatMap { perm ->
                (0..perm.size).map { ix ->
                    perm.toMutableList().apply { add(ix, to) }
                }
            }
        }
    }

}

class AmplifiersSeries(private val program: List<Long>) {
    fun start(phases: List<Long>): Long {
        return phases.fold(0) { acc, phase -> executeAmplifier(listOf(phase, acc)) }
    }

    fun startWithLoop(phases: List<Long>): Long = runBlocking {
        val a = Intcode.newProgram(program, listOf(phases[0], 0))
        val b = Intcode.withInputChannel(program, a.outputChannel.also { it.send(phases[1]) })
        val c = Intcode.withInputChannel(program, b.outputChannel.also { it.send(phases[2]) })
        val d = Intcode.withInputChannel(program, c.outputChannel.also { it.send(phases[3]) })
        val e = Intcode.withInputChannel(program, d.outputChannel.also { it.send(phases[4]) })

        val powers = mutableListOf<Long>()

        coroutineScope {
            launch { a.computeUntilEnd() }
            launch { b.computeUntilEnd() }
            launch { c.computeUntilEnd() }
            launch { d.computeUntilEnd() }
            launch { e.computeUntilEnd() }
            for (message in e.outputChannel) {
                a.inputChannel.send(message)
                powers.add(message)
            }
        }

        powers.maxOrNull()!!

    }

    private fun executeAmplifier(input: List<Long>): Long = runBlocking {
        Intcode.newProgram(program, input).computeUntilEnd().getOutput()[0]
    }
}