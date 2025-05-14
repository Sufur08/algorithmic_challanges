<script setup lang="ts">

import KotlinIntegration from "@/components/KotlinIntegration.vue";
import {computed, ref} from "vue";
import ArgInput from "@/components/ArgInput.vue";


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


</script>

<template>

    <div class="coin-problem">

        <div class="coin-problem__explanation">

        </div>


        <ArgInput
            :fields="[formField, {type: 'email', value: ref(2), description: 'bla bla'}]"
            :additional-args="optional"
        />
        <div style="height: 30px"/>

        <kotlin-integration
            :args="args"
        />



    </div>
    

</template>

<style lang="scss">
.coin-problem {

    padding-inline: calc(2dvw + 10%);

    &__explanation {
        height: 26dvh;
    }


}

</style>