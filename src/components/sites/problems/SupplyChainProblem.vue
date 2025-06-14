<script setup lang="ts">
import ProblemDescription from "@/components/ProblemDescription.vue";
import {computed, onMounted, ref} from "vue";
import {useRouteSettings} from "@/storages";
import List from "@/components/elements/List.vue";
import KotlinIntegration from "@/components/KotlinIntegration.vue";
import ArgInput from "@/components/ArgInput.vue";
import Spacer from "@/components/elements/Spacer.vue";
import Solution from "@/components/Solution.vue";


const pageState = useRouteSettings()

function getTable(tableNumber: number) {
    return tableNumber % 2 == 1
        ? "A,G|D,E|B,C,F,H\nA,B,C,D,F,G||E\nA,B,C,E,G|D|F,H\nA,B,C,E,F,G,H||D|\nA|B,C,D,E,F,H|G\nA,B,C,D,E,G,H||F\nA,B,D,E,F,G||C\n||A,B,C,D,E,F,G,H"
        : "A,B,F,G,I,J,L|C,D,H|E,K\nA,C,D,E,F,G,H,I,J,K,L||B\nB,C,D,E,F,G,H,I,J,K,L||A\nA,B,f,g,i,j,l|c,e,k|d,h\nA,B,D,E,F,g,H,i,j,k,l||c\nA,B,C,D,E,G,H,I,J,K,L||F\nA,B,C,E,F,G,H,I,J,K,L||D\nd,e,h,j,k|c|A,B,F,G,i,l\na,b,f,g,i,j,l|c,d,h,k|E\n||A,B,C,D,E,F,G,H,I,J,K,L\nA,B,C,D,E,H,I,J,K,L||f,g\nA,B,D,E,F,G,H,J,K,L|c|i"
}

const tableNumber = ref(1)
const table = computed(() => getTable(tableNumber.value))
const hiddenOutsideMain = `data class TableRow(
    var unaffected: List\<\String>,
    var affected: List<String>,
    var dependent: List<String>
)

fun Collection<TableRow>.toText(): String =
    map { Triple(it.unaffected.joinToString(","), it.affected.joinToString(","), it.dependent.joinToString(",")) }.run {
        val longest: Triple<Int, Int, Int> = fold(Triple(10, 8, 9)) { acc, row ->
            Triple(max(acc.first, row.first.length), max(acc.second, row.second.length), max(acc.third, row.third.length))
        }
        "unaffected\${" ".repeat(longest.first - 10)} | affected\${" ".repeat(longest.second - 8)} | dependent\${" ".repeat(longest.third - 9)}\\n" +
                joinToString("\\n") { "\${it.first}\${" ".repeat(longest.first - it.first.length)} | \${it.second}\${" ".repeat(longest.second - it.second.length)} | \${it.third}\${" ".repeat(longest.third - it.third.length)}" }
    }`
const codeSample = `
// your code here
// print the result
println(table.toText())`

const kotlinArgs = computed(() => [{
    name: "table",
    kotlinType: `List\<\TableRow>`,
    kotlinTypeParser: (it: string) => `try { args[0].uppercase().split("\\n").map { row ->
            val split = row.split("|").map { it.uppercase().split(",").filter { str -> str.isNotEmpty() } }
            TableRow(split[0], split[1], split[2])
} } catch (e: IndexOutOfBoundsException) { throw Exception("wrong input format", e) }`,
    value: table.value,
}])

const inputFields = [{
    description: "Table one or two:",
    type: "number",
    value: tableNumber
}]
const anonymousArgs = ref([])

const solutionArgs = [{
    description: "Table one or two:",
    type: "number",
    value: ref(1),
    mapper: (input: any) => isNaN(Number(input ?? "noNumber")) ? "got invalid input" : getTable(Number(input))
}]


onMounted(() => {
    pageState.header = "The Supply Chain Problem"
})


</script>

<template>


    <div class="supply-chain-problem">
        <ProblemDescription>
            <p>
                A company has multiple distri&shy;bution centers, warehouses, and regional hubs
                connected through a supply chain network purely with one-directional connections.
                <br>The company wants to develop a logistic network matching a given require&shy;ment report.
            </p>
            <p>
                This report analyzes: "What would happen if we had to shut down each facility?"
                and consists of a table showing the consequences of closing each facility, but without stating which facility was closed.
                <br>Each table-entry shows:
                <List>
                    <li>Unaffected facilities</li>
                    <li>Facilities with degraded service (facilities that would lose some but not all supply paths)</li>
                    <li>Facilities that would be forced to close (facilities that are 100&nbsp;% dependent on the closed facility)</li>
                </List>
            </p>
            <p>
                Find one graph (or more if you want) matching the given table.
                <br>All facilities are connected to one graph.
                <br>The second graph contains a cycle, but still, no facilities have connections directly back to each other.
                <br>Tip: You might want to solve it first manually or look into my recon&shy;structed graph to draw a visuali&shy;zation of one possible solution.
            </p>

        </ProblemDescription>

        <Spacer height="30px"/>

        <ArgInput
            :fields="inputFields"
            :additional-args="anonymousArgs"
        />

        <Spacer height="30px"/>

        <KotlinIntegration
            :hidden-outside-main="hiddenOutsideMain"
            :sample-code="codeSample"
            :dependencies="['kotlin.math.max']"
            :args="kotlinArgs"
            :unnamed-args="anonymousArgs.map(({value}) => value)"
        />

        <Spacer height="30px"/>

        <Solution
            code-file="/kotlin/SupplyChainProblem.kt"
            :parameters="solutionArgs"
        >
            <p>
                My code works on the both presets, but I haven't tested it on more.
                <br>It first finds out which row of the table represents which node in the graph and creates a Graph with nodes that contain the information about which other nodes they affect.
                <br>Then, I chaotically loop through the nodes three times, adding targets to them. (it was just trial and error to get to this arrange&shy;ment)
            </p>
            <p>
                The console output is
                <List>
                    <li>The recon&shy;structed table from the argument string</li>
                    <li>The nodes of the final graph (with their targets)</li>
                    <li>The connection of the final graph</li>
                </List>
                Each seperated by an empty line.
            </p>
            <p>
                There are 18 possible solutions for the first one and 140 for the second one.
                <br>I validated my own solutions when I thought they might be done by drawing the graph and creating the table based on my graph.
                It should equal the original table.
            </p>
        </Solution>
    </div>
</template>

<style lang="scss">

.supply-chain-problem {
    padding-inline: 12%;
    padding-block-end: 18dvh;

    display: flex;
    flex-direction: column;
    align-items: stretch;
    text-align: start;
    align-content: start;

}


</style>