<script setup lang="ts">

import KotlinIntegration from "@/components/KotlinIntegration.vue";



const args = [
    {
        name: "arg1",
        kotlinType: "Int",
        kotlinTypeParser: (it: string) => `${it}.toInt()`,
        value: "100"
    }
]


</script>

<template>

    <div class="coin-problem">

        <div class="coin-problem__explanation">

        </div>

        <kotlin-integration :args="args" />



    </div>
    

</template>

<style lang="scss">
.coin-problem {

    width: 100dvw;

    &__explanation {
        height: 26dvh;
    }



}

</style>