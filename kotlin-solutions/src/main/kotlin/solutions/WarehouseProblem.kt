package solutions

import core.Solution
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration

object WarehouseProblem: Solution
{
    override fun entryPoint(vararg args: String)
    {
        println(findStorages(
            articleRange = 1..args[0].toInt(),
            sets = args[1].toInt(),
            maxTriesPerContainer = args[2].toInt(),
        ))
    }

    fun <T : Any> aLongList(): ArrayList<T> = object : ArrayList<T>()
    {
        override val size: Int = Int.MAX_VALUE
    }

    class Storage(articles: List<Int> = listOf(), blocked: List<Int> = listOf())
    {
        val content: ArrayList<Int> = ArrayList(articles)
        private val blocked: MutableSet<Int> = blocked.toMutableSet()

        val values: List<Int> get() = listOf(*content.toTypedArray())
        val size: Int get() = content.size

        fun wouldBlock(article: Int): Int =
            content.map { it + article }.run { this.size - blocked.filter { it in this }.size }

        fun canContain(article: Int): Boolean = article !in blocked && article !in content
        fun add(article: Int): Boolean
        {
            //  if (!canContain(article)) return false
            blocked.addAll(content.map { it + article })
            return content.add(article)
        }

        fun copy() = Storage(content, blocked.toList())

        override fun equals(other: Any?): Boolean = if (other is Storage) values == other.values else false
        override fun toString(): String = "Storage: [${content.joinToString(", ")}]"
    }

    fun allCombinations(list: List<Int>, number: Int): List<List<Int>>
    {
        if (number == 1) return list.map { listOf(it) }
        val withoutFirst = list.subList(1, list.size)
        return if (number == withoutFirst.size)
            listOf(withoutFirst, *allCombinations(withoutFirst, number - 1).map { it + list.first() }.toTypedArray())
        else listOf(
            *allCombinations(withoutFirst, number).toTypedArray(),
            *allCombinations(withoutFirst, number - 1).map { it + list.first() }.toTypedArray()
        )
    }


    fun findStorages(articleRange: IntRange, sets: Int, maxTriesPerContainer: Int = 300000): List<List<Int>>
    {
        fun ArrayList<Storage>.copy() = ArrayList(this.map { it.copy() })

        data class RecursiveResult(
            val storages: ArrayList<Storage>? = null,
            val pausedAt: Int? = null,
            val canceled: Boolean = false
        ) {
            val number: Int get() = storages?.size ?: pausedAt ?: Int.MAX_VALUE
            override fun toString(): String = "${super.toString()}: {storages: $storages, pausedAt: $pausedAt, canceled: $canceled}"
        }

        val repsOf: HashMap<Int, Int> = hashMapOf(sets to 0)
        var timeForCombinations = 0.toDuration(DurationUnit.MICROSECONDS)
        var calls = 0

        fun recursive(range: IntRange, storages: ArrayList<Storage>, accessSmallestVar: Boolean? = false): RecursiveResult
        {
            calls++
            repsOf[storages.size] = repsOf[storages.size]!! + 1
            for (i in range)
            {
                val canContain = storages.withIndex()
                    .filter { it.value.canContain(i) }
                if (sets > canContain.size)
                {
                    //if (storages.sumOf { it.size } / storages.size.toDouble() < 2) return println("didnt extend $storages").run { RecursiveResult(canceled = true) }
                    val newSize = storages.size + sets - canContain.size
                    return if (accessSmallestVar != null) RecursiveResult(pausedAt = newSize)
                    else
                    {
                        repsOf[newSize] = 0
                        recursive(
                            articleRange,
                            arrayListOf(*Array(newSize) { Storage() }),
                            null
                        )
                    }
                }
                else if (sets == canContain.size) canContain.forEach { it.value.add(i) }
                else
                {
                    if (repsOf[storages.size]!! > maxTriesPerContainer) return RecursiveResult(canceled = true)
                    var combinations: List<List<Int>> = listOf()
                    timeForCombinations += measureTime {
                        combinations = allCombinations(canContain.map { it.index }, sets)//.shuffled()
                    }
                    val list = combinations.map { a -> storages.copy().apply {  a.forEach { this[it].add(i) } } }
                    //val other = list.sortedBy { it.sumOf { j -> j.wouldBlock(i) } }
                    var result: RecursiveResult = list.fold(RecursiveResult()) { acc, indices ->
                        recursive(i + 1..range.last, indices, true)
                       // recursive(i + 1..range.last, storages.copy().apply { indices.forEach { this[it].add(i) } }, true)
                            .run {
                                if (this.storages != null) return this
                                when (this.number.compareTo(acc.number)) {
                                    -1 -> this
                                    else -> acc
                                }
                            }
                    }
                    while (accessSmallestVar == null && result.storages == null)
                    {
                        repsOf[result.number] = 0
                        result = recursive(articleRange, arrayListOf(*Array(result.number) { Storage() }), null)
                    }
                    return result
                }
            }
            return RecursiveResult(storages)
        }

        val storages: ArrayList<Storage> = arrayListOf(*Array(sets) { Storage() })
        val it = recursive(articleRange, storages, null).storages!!.map { it.values }

        println("total amount of recursive calls: $calls")
        println("number of tries for each number of storages: ${repsOf.entries.joinToString(", ")}")
        repsOf.entries.firstOrNull { it.value >= 200000 }?.let {
            println("perfect solution might require only ${it.key} or more containers")
        }

        return it
    }

}