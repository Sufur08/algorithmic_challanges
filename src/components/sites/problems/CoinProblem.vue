<script setup lang="ts">

import KotlinIntegration from "@/components/KotlinIntegration.vue";
import {computed, onMounted, ref} from "vue";
import ArgInput from "@/components/ArgInput.vue";
import {useRouteSettings} from "@/storages";


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

        <div class="coin-problem__explanation">

        </div>


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

    padding-inline: calc(2dvw + 10%);

    &__explanation {
        height: 26dvh;
    }


}

</style>