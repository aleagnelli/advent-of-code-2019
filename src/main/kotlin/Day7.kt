import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Day7 {
    fun part1(program: List<Int>): Int {
        return allPhases(0, 4).map { AmplifiersSeries(program).start(it) }.max()!!
    }

    fun part2(program: List<Int>): Int {
        println(allPhases(5, 9).map { AmplifiersSeries(program).startWithLoop(it) })
        return allPhases(5, 9).map { AmplifiersSeries(program).startWithLoop(it) }.max()!!
    }

    private fun allPhases(from: Int, to: Int): List<List<Int>> {
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

class AmplifiersSeries(private val program: List<Int>) {
    fun start(phases: List<Int>): Int {
        return phases.fold(0) { acc, phase -> executeAmplifier(listOf(phase, acc)) }
    }

    fun startWithLoop(phases: List<Int>): Int = runBlocking {
        val a = Intcode.newProgram(program, listOf(phases[0], 0))
        val b = Intcode.withInputChannel(program, a.outputChannel.also { it.send(phases[1]) })
        val c = Intcode.withInputChannel(program, b.outputChannel.also { it.send(phases[2]) })
        val d = Intcode.withInputChannel(program, c.outputChannel.also { it.send(phases[3]) })
        val e = Intcode.withInputChannel(program, d.outputChannel.also { it.send(phases[4]) })

        val copy = Channel<Int>(Channel.CONFLATED)
        val powers = mutableListOf<Int>()

        coroutineScope {
            launch { a.compute() }
            launch { b.compute() }
            launch { c.compute() }
            launch { d.compute() }
            launch { e.compute() }
            for (message in e.outputChannel) {
                a.inputChannel.send(message)
                copy.send(message)
                powers.add(message)
            }
        }

        powers.max()!!

    }

    private fun executeAmplifier(input: List<Int>): Int = runBlocking {
        Intcode.newProgram(program, input).compute().getOutput()[0]
    }
}