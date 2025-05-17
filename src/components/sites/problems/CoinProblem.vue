<script setup lang="ts">

import KotlinIntegration from "@/components/KotlinIntegration.vue";
import {computed, onMounted, ref} from "vue";
import ArgInput from "@/components/ArgInput.vue";
import {useRouteSettings} from "@/storages";
import ProblemDescription from "@/components/ProblemDescription.vue";


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


        </ProblemDescription>


        <ArgInput
            :fields="[formField]"
            :additional-args="optional"
        />
        <div style="height: 30px"/>

        <kotlin-integration
            :args="args"
            :unnamed-args="optional.map(({value}) => value)"
        />



    </div>
    

</template>

<style lang="scss">
.coin-problem {

    padding-inline: 12%;
    overflow: visible;

    display: flex;
    flex-direction: column;
    align-items: stretch;

    &__explanation {


    }


}

</style>