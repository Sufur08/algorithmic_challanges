package solutions

import core.Solution

object WarehouseProblem: Solution
{
    override fun entryPoint(vararg args: String)
    {

    }

    class Storage(articles: List<Int> = listOf(), blocked: List<Int> = listOf())
    {
        private val content: ArrayList<Int> = ArrayList(articles)
        private val blocked: MutableSet<Int> = blocked.toMutableSet()


        fun wouldBlock(article: Int): Int = content.map { it + article }.run { this.size - blocked.filter { it in this }.size }

        fun canContain(article: Int): Boolean = article !in blocked && article !in content
        fun tryAdd(article: Int): Boolean
        {
            if (!canContain(article)) return false
            blocked.addAll(content.map { it + article })
            return content.add(article)
        }

        fun copy() = Storage(content, blocked.toList())

        override fun toString(): String = "Storage: [${content.joinToString(", ")}]"
        operator fun get(index: Int): Int = content[index]
        operator fun contains(value: Int): Boolean = content.contains(value)
    }

    fun findStorages(articleRange: IntRange, sets: Int): List<List<Int>>
    {
        val storages: ArrayList<Storage> = arrayListOf(Storage())
        fun ArrayList<Storage>.copy() = this.map { it.copy() }



        for (i in articleRange)
        {

            var j = sets
            val sorted = storages.filter { it.canContain(i) }.sortedBy { it.wouldBlock(i) }
            for (storage in sorted)
            {
                if (storage.tryAdd(i))
                    if ((--j) == 0) break
            }
            storages.addAll(Array(j) { Storage(listOf(i)) })

        }

        println(storages.joinToString("\n"))

        return listOf()

    }


}