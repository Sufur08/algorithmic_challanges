<script setup>

import ProblemPreview from "@/components/ProblemPreview.vue";
</script>

<template>

    <div class="mainHome__container">
        <ProblemPreview img="./previewImgs/coins.webp" title="The Coins" link-to="/coin-problem"/>
        <ProblemPreview img="./previewImgs/warehouse.webp" title="The Boxes" link-to="/warehouse-problem"/>
    </div>

</template>

<style lang="scss">

    .mainHome {
        &__container {
            padding-block-start: 4dvh;
            overflow: visible;
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
        }
    }

</style>

