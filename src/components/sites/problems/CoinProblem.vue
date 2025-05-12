<script setup lang="ts">

import KotlinIntegration from "@/components/KotlinIntegration.vue";
import {computed, ref} from "vue";


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


</script>

<template>

    <div class="coin-problem">

        <div class="coin-problem__explanation">

        </div>


        <input type="number" v-model="arg">

        <kotlin-integration class="coin-problem__kotlin" :args="args" />



    </div>
    

</template>

<style lang="scss">
.coin-problem {

    padding-inline: 1.4dvw;

    &__explanation {
        height: 26dvh;
    }

    &__kotlin {
    }



}

</style>