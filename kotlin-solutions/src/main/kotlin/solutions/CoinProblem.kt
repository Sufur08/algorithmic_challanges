package solutions

import core.Solution

object CoinProblem: Solution
{

    fun <T: Any?> T.toString(lam: T.() -> Any?): String = lam(this).toString()

    class ValueUnreachable(value: Int, message: String = "$value unreachable") : Exception(message)

    override fun entryPoint(vararg args: String)
    {
        if (args.size > 1) tryOut(args[1].filter { it != ' ' }.split(',').map { it.toInt() }, true)
        else println(findValues(args[0].toInt()).toString { "denominations: [${first.joinToString(", ")}]\naverage coins needed: $second" })
    }

    /**
     * @param valueCount number of different allowed denominations
     * @return pair.first: list of the denominations; pair.second: average required number of coins
     */
    fun findValues(valueCount: Int): Pair<Collection<Int>, Double>
    {
        if (valueCount < 1) throw IllegalArgumentException("valueCount must be greater than 0")
        if (valueCount >= 50)
            return if (valueCount > 99)
                (1..valueCount).toList() to 1.0
            else
                (Array(50) { it * 2 + 1 } + Array(valueCount - 50) { (it + 1) * 2 }).toList()
                    .run { this to tryOut(this) }

        val list: ArrayList<Int> = arrayListOf(1)
        var best: Pair<List<Int>, Double> = listOf<Int>() to 100.0
        fun bruteForce(rep: Int, entry: Int)
        {
            if (rep == valueCount)
            {
                tryOut(list, iKnowWhatIDo = true).apply {
                    if (this < best.second) best = ArrayList(list) to this
                }
                return
            }
            for (i in (entry)..98)
            {
                list.add(i)
                bruteForce(rep + 1, i + 1)
                list.removeAt(rep)
            }
        }
        bruteForce(1, 2)

        return best
    }


    /**
     * @param values the set of values to be tested
     * @param printOut whether the best results should be printed out
     * @param iKnowWhatIDo whether [values] should be filtered (`values.filter { it in -99..99 && it != 0 }`)
     * @return the average number of coins needed to total the values in 1..99
     */
    fun tryOut(values: Collection<Int>, printOut: Boolean = false, iKnowWhatIDo: Boolean = false): Double
    {
        val sorted =
            if (iKnowWhatIDo) values.sorted()
            else values.filter { it in -99..99 && it != 0 }.sorted()

        // less efficient solution which saves the set of coins to each value instead of only the length
        // -> can be printed
        if (printOut)
        {
            if (1 !in values) throw ValueUnreachable(1)

            val solutions: HashMap<Int, ArrayList<Int>> = hashMapOf(0 to arrayListOf(), 1 to arrayListOf(1))

            fun findSolution(v: Int, coins: List<Int>): ArrayList<Int>
            {
                if (v in solutions) return ArrayList(solutions[v]!!)
                var solution: ArrayList<Int> = object : ArrayList<Int>() {
                    override val size: Int = 100
                }
                for (coin in coins)
                {
                    if (coin > v) break
                    val sol = findSolution(v - coin, coins)
                    if (sol.size + 1 < solution.size)
                    {
                        solution = sol.apply { add(coin) }
                    }
                }
                if (solution.isEmpty()) throw ValueUnreachable(v)
                solutions.put(v, solution)
                return solution
            }
            for (i in 1..99)
                findSolution(i, sorted)

            val avg = solutions.values.sumOf { it.size.toDouble() } / 99
            println(solutions.map { "${it.key}: " + it.value.joinToString(", ")}.joinToString("\n") + "\navg: $avg")
            return avg
        }

        val solutions = IntArray(100) { 100 }
        solutions[0] = 0
        for (i in 1..99)
        {
            for (coin in sorted)
            {
                if (coin > i) break
                solutions[i] = minOf(solutions[i], solutions[i - coin] + 1)
            }
            if (solutions[i] == 100) throw ValueUnreachable(i)
        }
        return solutions.slice(1..99).average()
    }

}