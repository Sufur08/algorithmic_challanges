<script setup lang="ts">

import {computed, onMounted, ref} from "vue";
import {useRouteSettings} from "@/storages";
import ProblemDescription from "@/components/ProblemDescription.vue";
import Spacer from "@/components/elements/Spacer.vue";
import ArgInput from "@/components/ArgInput.vue";
import KotlinIntegration from "@/components/KotlinIntegration.vue";
import Solution from "@/components/Solution.vue";
import Indented from "@/components/elements/Indented.vue";

const pageState = useRouteSettings()


const length = ref(52)
const duplicates = ref(0)

const kotlinArgs = computed(() => [{
    name: "length",
    kotlinType: "Int",
    kotlinTypeParser: (it: string) => `${it}.toInt()`,
    value: length.value.toString(),
}, {
    name: "duplicates",
    kotlinType: "Int",
    kotlinTypeParser: (it: string) => `${it}.toInt()`,
    value: duplicates.value.toString(),
}])

const inputFields = [{
    description: "Number of articles:",
    type: "number",
    value: length
}, {
    description: "Number of duplicates:",
    type: "number",
    value: duplicates
}]
const anonymousArgs = ref([])


onMounted(() => {
    pageState.header = "The Warehouse Problem"
})

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
                    <br/>[1, 2, 4, 7,&nbsp;8], [3,&nbsp;5,&nbsp;6] works
                    <br/>[1, 2, 4, 7,&nbsp;8], [3,&nbsp;5, 6, 9,&nbsp;10] doesn't work because 6&nbsp;+&nbsp;3&nbsp;=&nbsp;9, so we need to add a third storage.
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

        <Solution code-file="" :parameters="[]">

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
}

</style>