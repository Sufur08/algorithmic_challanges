package solutions

import core.Solution

object CoinProblem: Solution
{

    fun <T: Any?> T.toString(lam: T.() -> Any?): String = lam(this).toString()

    class ValueUnreachable(value: Int, message: String = "$value unreachable") : Exception(message)

    override fun entryPoint(vararg args: String)
    {
        val arg: Int = args[0].toInt()
        println(findValues(arg).toString { "denominations: [${first.joinToString(", ")}]\naverage coins needed: $second" })
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

        return listOf(2) to .0
    }


    fun tryOut(values: Collection<Int>): Double
    {
        if (1 !in values) throw ValueUnreachable(1)

        val sorted = values.filter { it in -99..99 && it != 0 }.sorted().reversed()
        val solutions: HashMap<Int, ArrayList<Int>> = hashMapOf(0 to arrayListOf())

        fun findSolution(v: Int, coins: List<Int>): ArrayList<Int>
        {
            if (v in solutions) return ArrayList(solutions[v]!!)

            var solution: ArrayList<Int> = object : ArrayList<Int>() {
                override val size: Int = 100
            }

            for (coin in coins)
            {
                val a: Int = v / coin
                if (a == 0 || a > solution.size) continue
                val b = v % coin
                if (v - b in solutions && solutions[v - b]!!.size < a) continue
                val x = findSolution(b, coins.filter { it != coin })
                if (a + x.size < solution.size) solution = x.apply { addAll(Array(a) { coin }) }
            }

            if (solution.isEmpty) throw ValueUnreachable(v)
            solutions.put(v, solution)
            println(solution.joinToString(", "))
            return solution
        }


        for (i in 1..99)
            findSolution(i, sorted)

        return solutions.values.sumOf { it.size.toDouble() } / 99

    }





}