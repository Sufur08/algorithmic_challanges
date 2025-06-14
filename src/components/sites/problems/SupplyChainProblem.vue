<script setup lang="ts">
import ProblemDescription from "@/components/ProblemDescription.vue";
import {computed, onMounted, ref} from "vue";
import {useRouteSettings} from "@/storages";
import List from "@/components/elements/List.vue";
import KotlinIntegration from "@/components/KotlinIntegration.vue";
import ArgInput from "@/components/ArgInput.vue";
import Spacer from "@/components/elements/Spacer.vue";


const pageState = useRouteSettings()

const tableNumber = ref(1)
const table = computed(() => {
    if (tableNumber.value % 2 === 1) {
        return "A,G|D,E|B,C,F,H\nA,B,C,D,F,G||E\nA,B,C,E,G|D|F,H\nA,B,C,E,F,G,H||D|\nA|B,C,D,E,F,H|G\nA,B,C,D,E,G,H||F\nA,B,D,E,F,G||C\n||A,B,C,D,E,F,G,H"
    } else {
        return "table2"
    }}
)
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
    kotlinTypeParser: (it: string) => `try { ${it}.split("\\n").map { row ->
    val split = row.split("|").map { it.uppercase().split(",") }
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
    description: "Max number of brute force tries:",
    type: "number",
    value: ref(300000)
}]


onMounted(() => {
    pageState.header = "The Supply Chain Problem"
})


</script>

<template>


    <div class="supply-chain-problem">
        <ProblemDescription>
            <p>
                A company has multiple distribution centers, warehouses, and regional hubs
                connected through a supply chain network purely with one-directional connections.
                <br>The company wants to develop a logistic network matching a given requirement report.
            </p>
            <p>
                This report analyzes: "What would happen if we had to shut down each facility?"
                and consists of a table showing the consequences of closing each facility, but without stating which facility was closed.
                <br>Each table-entry shows:
                <List>
                    <li>Unaffected facilities</li>
                    <li>Facilities with degraded service (facilities that would lose some but not all supply paths)</li>
                    <li>Facilities that would be forced to close (facilities that are 100% dependent on the closed facility)</li>
                </List>
            </p>
            <p>
                Find one graph (or more if you want) matching the given table.
                <br>All facilities are connected to one graph.
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

        <!-- rest -->
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