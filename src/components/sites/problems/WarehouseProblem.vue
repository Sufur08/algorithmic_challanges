<script setup lang="ts">

import {computed, onMounted, ref} from "vue";
import {useRouteSettings} from "@/storages";
import ProblemDescription from "@/components/ProblemDescription.vue";
import Spacer from "@/components/elements/Spacer.vue";
import ArgInput from "@/components/ArgInput.vue";
import KotlinIntegration from "@/components/KotlinIntegration.vue";
import Solution from "@/components/Solution.vue";
import Indented from "@/components/elements/Indented.vue";
import {vAnimatedIf} from "@/directives";

const pageState = useRouteSettings()


const length = ref(52)
const sets = ref(1)

const kotlinArgs = computed(() => [{
    name: "length",
    kotlinType: "Int",
    kotlinTypeParser: (it: string) => `${it}.toInt()`,
    value: length.value.toString(),
}, {
    name: "sets",
    kotlinType: "Int",
    kotlinTypeParser: (it: string) => `${it}.toInt()`,
    value: sets.value.toString(),
}])

const inputFields = [{
    description: "Number of articles:",
    type: "number",
    value: length
}, {
    description: "Number of sets:",
    type: "number",
    value: sets
}]
const anonymousArgs = ref([])

const solutionArgs = [{
    description: "Number of articles:",
    type: "number",
    value: ref(19)
}, {
    description: "Number of sets:",
    type: "number",
    value: ref(2)
}, {
    description: "Max number of brute force tries:",
    type: "number",
    value: ref(300000)
}]

onMounted(() => {
    pageState.header = "The Warehouse Problem"
})

const showMoreSolutions = ref(false)

</script>

<template>

    <div class="warehouse">

        <ProblemDescription>
            <p>
                We have a set of articles, which needs to be stored with very specific restrictions:
                <br/>
                <indented spaces="2">
                    The articles are numbered (eg. from 1 to 52). Articles can't be stored in the same container when any three of them
                    fit in the pattern x&nbsp;+&nbsp;y&nbsp;=&nbsp;z.
                </indented>
                <br/>They can be imagined to have special chemical or physical properties, so they can't be stored together.
            </p>
            <p>
                Example: <br/>
                <Indented>
                    After [1,&nbsp;2] we need to add a second storage for&nbsp;3
                    <br/>[1, 2, 4,&nbsp;8], [3,&nbsp;5; 6,&nbsp;7] works
                    <br/>[1, 2, 4,&nbsp;8], [3,&nbsp;5, 6, 7,&nbsp;9] doesn't work because 3&nbsp;+&nbsp;6&nbsp;=&nbsp;9 (=&nbsp;1&nbsp;+&nbsp;8), so we need to add a third storage.
                </Indented>
            </p>
            <p class="problem-description__question">
                The goal is to minimize the number of containers required to store all articles.
            </p>
            <p>
                We might also want to store a second or third set of the articles for replacement.
                In that case we want to store the duplicates separate from each other too, in case one container gets lost / damaged.
            </p>
        </ProblemDescription>

        <Spacer height="30px"/>
        <ArgInput
            :fields="inputFields"
            :additional-args="anonymousArgs"
        />
        <Spacer height="30px"/>

        <KotlinIntegration
            :args="kotlinArgs"
            :unnamed-args="anonymousArgs.map(({value}) => value)"
        />
        <Spacer height="30px"/>

        <Solution
            code-file="/kotlin/WarehouseProblem.kt"
            :parameters="solutionArgs"
        >
            <p>
                This code finds the most ideal solutions (I&nbsp;think) but takes quite long to execute on higher inputs.
                <br>For example 19,&nbsp;2 may finish within seconds and 20,&nbsp;2 takes ~1:25 minutes.
                But if the order in which possibil&shy;ities are tested differs even 19, 2 might take millions of tries to finish.
                <br>For one set, the point, where the runtime increases dramati&shy;cally is around&nbsp;60.
                <br>To find a solution within reason&shy;able runtime, you can specify a number ot limit the attempts spend on each amount of containers.
                This probably brings worse solutions than an efficiency focused algorithm would calculate.
                My approach restarts the entire calcu&shy;lation when it realizes that it needs to a new storage.
            </p>
            <div class="warehouse__show-more-solutions">
                <h2 @click="showMoreSolutions = !showMoreSolutions">
                    Show more solutions to compare
                </h2>
                <div
                    data-v-relative-transition=".006"
                    v-animated-if.height="showMoreSolutions"
                >
                    <p>
                        One set of 66 articles fit in 4&nbsp;containers:<br>
                        <Indented>
                            I: [24,&nbsp;26, 27, 28, 29, 30, 31, 32, 33, 36, 37 ,38, 39, 41, 42, 44, 45, 46, 47, 48,&nbsp;49]
                            <br>II: [9,&nbsp;10, 12, 13, 14, 15, 17, 18, 20, 54, 55, 56, 57, 58, 59, 60, 61,&nbsp;62]
                            <br>III: [1,&nbsp;2, 4, 8, 11, 16, 22, 25, 40, 43, 53,&nbsp;66]
                            <br>IV: [3,&nbsp;5, 6, 7, 19, 21 ,23, 34, 35, 50, 51, 52, 63, 64,&nbsp;65]
                        </Indented>
                    </p>
                    <p>
                        Two sets of 65 articles fit in 6&nbsp;containers:<br>
                        <Indented>
                            I: [1, 3, 5, 10, 14, 16, 18, 20, 22, 29, 31, 44, 48, 50, 52,&nbsp;56]
                            <br>II: [2, 8, 12, 13, 16, 27, 32, 33, 38, 42,&nbsp;52]
                            <br>III: [1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 29, 35, 37, 41, 43, 45, 47, 49, 53,&nbsp;55]
                            <br>IV: [24, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51,&nbsp;53]
                            <br>V: [2, 6, 10, 18, 19, 22, 26, 27, 30, 34, 43, 47, 51, 54, 55, 58,&nbsp;59]
                            <br>VI: [4, 7, 12, 14, 15, 17, 20, 25, 28, 36, 46, 54,&nbsp;57]
                        </Indented>
                    </p>
                </div>
            </div>

        </Solution>


    </div>

</template>

<style lang="scss">

.warehouse {
    padding-inline: 12%;
    padding-block-end: 18dvh;

    display: flex;
    flex-direction: column;
    align-items: stretch;


    &__show-more-solutions {
        width: 100%;
        display: flex;
        flex-direction: column;

        & > h2 {
            margin-block: .4em;
            font-size: 1.4em;
        }

        & > * {
            font-size: revert;
        }
    }
}

</style>