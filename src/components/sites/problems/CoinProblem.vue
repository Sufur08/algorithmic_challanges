<script setup lang="ts">

import KotlinIntegration from "@/components/KotlinIntegration.vue";
import {computed, onMounted, ref} from "vue";
import ArgInput from "@/components/ArgInput.vue";
import {useRouteSettings} from "@/storages";
import ProblemDescription from "@/components/ProblemDescription.vue";
import Solution from "@/components/Solution.vue";


const pageState = useRouteSettings()

const arg = ref(100);

const args = computed(() => {
    return [
        {
            name: "arg1",
            kotlinType: "Int",
            kotlinTypeParser: (it: string) => `${it}.toInt()`,
            value: arg.value.toString()
        }
    ]
})

const formField = {
    description: "Please enter a number:",
    type: "number",
    value: arg,
}

const optional = ref<{ name: string, value: string}[]>([])
console.log(optional.value)


onMounted(() => {
    pageState.header = "The Coin Problem"
})


</script>

<template>

    <div class="coin-problem">

        <ProblemDescription>

            <p>
                We are used to coins with the denominations 1, 2, 5, 10, 20 and 50 (ct or similar).
                These values are intuitive because they match our decimal number system.
                But are these mathe&shy;matically ideal, regarding the amount of coins needed to total a certain value? - Probably not.
            </p>
            <p class="problem-description__question">
                The question goes as follows: What is the most efficient set of denominations
                in order to achieve the lowest average of needed coins for the values of 1..99.
                Your function should be able to create this set in different lengths, given as parameter.
            </p>
            <p>
                The main focus should be on finding the best result, but keep efficiency in mind.
            </p>


        </ProblemDescription>

        <div style="height: 30px"/>

        <ArgInput
            :fields="[formField]"
            :additional-args="optional"
        />
        <div style="height: 30px"/>

        <kotlin-integration
            :args="args"
            :unnamed-args="optional.map(({value}) => value)"
        />

        <div style="height: 30px"/>

        <Solution
            :parameters="[formField]"
            code-file="/kotlin/CoinProblem.kt"
        >
            <p>
                My solution doesn't focus that much on the function that actually searches the best result,
                rather than the one that verifies each result and finds the best arrangement of the given coins to total different values.
            </p>
            <p>
                For example:
                <br/><tab/>coins = [1, 3, 8, 10, 40]; value = 27
                <br/><tab/>The 'intuitive' solution here would be [10, 10, 3, 3, 1]; size = 5.
                <br/><tab/>But theres a better one: [8, 8, 8, 3]; size = 4
            </p>
            <p>

            </p>
        </Solution>



    </div>
    

</template>

<style lang="scss">
.coin-problem {

    padding-inline: 12%;
    padding-block-end: 18dvh;
    overflow: visible;

    display: flex;
    flex-direction: column;
    align-items: stretch;

    &__explanation {


    }


}

</style>