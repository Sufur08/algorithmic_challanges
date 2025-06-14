package solutions

import core.Solution
import kotlin.math.max

data class TableRow(
    var unaffected: List<String>,
    var affected: List<String>,
    var dependent: List<String>
)

fun Collection<TableRow>.toText(): String =
    map { Triple(it.unaffected.joinToString(","), it.affected.joinToString(","), it.dependent.joinToString(",")) }.run {
        val longest: Triple<Int, Int, Int> = fold(Triple(10, 8, 9)) { acc, row ->
            Triple(max(acc.first, row.first.length), max(acc.second, row.second.length), max(acc.third, row.third.length))
        }
        "unaffected${" ".repeat(longest.first - 10)} | affected${" ".repeat(longest.second - 8)} | dependent${" ".repeat(longest.third - 9)}\n" +
                joinToString("\n") { "${it.first}${" ".repeat(longest.first - it.first.length)} | ${it.second}${" ".repeat(longest.second - it.second.length)} | ${it.third}${" ".repeat(longest.third - it.third.length)}" }
    }

inline fun Boolean.ifAlso(lam: () -> Any?): Boolean = this.also { if (this) lam() }
inline fun Boolean.elseAlso(lam: () -> Any?): Boolean = this.also { if (!this) lam() }

object SupplyChainProblem: Solution
{
    override fun entryPoint(vararg args: String)
    {
        val table = try { args[0].uppercase().split("\n").map { row ->
            val split = row.split("|").map { it.uppercase().split(",").filter { str -> str.isNotEmpty() } }
            TableRow(split[0], split[1], split[2])
        } } catch (e: IndexOutOfBoundsException) { throw Exception("wrong input format", e) }

        println(table.toText() + "\n")
        findGraph(table).let {
            println(it)
            println()
            println(it.allConnections().joinToString(", "))
        }
    }

    class Node(
        val name: String,
        var targets: MutableSet<Node> = mutableSetOf(),
        var affects: MutableList<Node> = arrayListOf(),
        var dependent: MutableList<Node> = arrayListOf(),
    )
    {
        val finished = false
        val dependAndAffected get() = dependent + affects
        fun unaffected(of: List<Node>): List<Node> = ArrayList(of).apply { removeAll(affects); removeAll(dependent); remove(this@Node) }
        fun measureDependAffect(): ArrayList<Node> = ArrayList<Node>().apply {
            fun ArrayList<Node>.recursive(start: Node)
            {
                for (i in start.targets)
                {
                    if (i in this) continue
                    add(i)
                    recursive(i)
                }
            }
            recursive(this@Node)
        }
        override fun toString(): String = "Node $name: targets: ${targets.names()}, affects: ${affects.names()}, dependent: ${dependent.names()}"
    }
    class Graph(
        val nodes: ArrayList<Node> = arrayListOf(),
    )
    {
        fun addNode(name: String): Node =
            nodes.find { it.name == name } ?: Node(name).apply { nodes.add(this) }
        fun addNode(node: Node) =
            nodes.find { it.name == node.name } ?: nodes.add(node)
        fun allConnections(): List<String> = nodes.fold(listOf()) { acc, node -> acc + node.targets.names().map { node.name + it } }
        operator fun get(name: String): Node? = nodes.find { it.name == name }
        override fun toString(): String = "${super.toString()}: nodes: \n${nodes.joinToString("\n")}"
    }
    fun Collection<Node>.names() = map { it.name }

    fun findGraph(table: List<TableRow>): Graph
    {
        val all: HashMap<String, Node> = hashMapOf(*table.first().run { unaffected + affected + dependent }.map { it to Node(it) }.toTypedArray())
        fun doForNew(node: Node, row: TableRow, graph: Graph)
        {
            node.affects.addAll(row.affected.map { all[it]!! })
            node.dependent.addAll(row.dependent.filter { it != node.name }.map { all[it]!! })
            graph.addNode(node)
        }
        return Graph().apply {
            var ref = mutableMapOf(*table.mapIndexed { index, row -> index to row.dependent }.toTypedArray())
            for (reps in 1..table.size)
            {
                ref = ref.filter {
                    (it.value.size != 1).elseAlso {
                        doForNew(all[it.value.first()]!!, table[it.key], this)
                    }
                }.toMutableMap()
                ref.forEach { (index, value) ->
                    ref[index] = value.filter { it !in nodes.names() }
                }
                if (ref.filter { it.value.size == 1 }.isEmpty()) break
            }
            if (ref.isNotEmpty()) throw Exception("this table is none of my presets")

            val closed = mutableListOf<Node>()
            nodes.forEach { node ->
                node.targets.addAll(node.dependent.filter { affected ->
                    node.dependent.none { affected in it.dependAndAffected }
                })
                closed.addAll(node.targets)
                node.targets.addAll(node.affects.filter { affected ->
                    node.dependAndAffected.none { affected in it.dependAndAffected }
                })
            }
            nodes.forEach { node ->
                node.affects.forEach { affected ->
                    if (affected !in node.measureDependAffect() && affected !in closed && node !in affected.targets)
                        node.targets.add(affected)
                }
            }
            nodes.forEach { node ->
                node.affects.forEach { affected ->
                    if (affected in closed) return@forEach
                    if (node.affects.none { affected in it.dependAndAffected && node !in it.dependAndAffected })
                        node.unaffected(all.values.toList()).filter { affected in it.dependAndAffected && it !in affected.measureDependAffect() }.let { ref ->
                            val reduced = ref.filter { refEl ->
                                refEl.dependAndAffected.none { it in (ref - refEl) }
                            }
                            reduced.forEach { it.targets.add(affected) }
                        }
                }
            }
        }
    }
}