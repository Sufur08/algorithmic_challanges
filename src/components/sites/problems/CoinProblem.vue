<script setup lang="ts">



import KotlinIntegration from "@/components/KotlinIntegration.vue";
</script>

<template>

    <div class="coin-problem">

        <div class="coin-problem__explanation">

        </div>

        <kotlin-integration/>



    </div>
    

</template>

<style lang="scss">
.coin-problem {

    background-color: white;
    width: 100dvw;

    &__explanation {
        height: 26dvh;
    }



}

</style>