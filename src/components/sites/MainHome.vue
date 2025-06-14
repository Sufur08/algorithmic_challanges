<script setup>

import ProblemPreview from "@/components/ProblemPreview.vue";
</script>

<template>

    <div class="mainHome__container">
        <ProblemPreview img="./previewImgs/coins.webp" title="The Coins" link-to="/coin-problem" img-alt-text="img of coins with different values"/>
        <ProblemPreview img="./previewImgs/warehouse.webp" title="The Boxes" link-to="/warehouse-problem"/>
        <ProblemPreview img="./previewImgs/supply-chain.webp" title="Supply Chain" link-to="/supply-chain-problem"/>
    </div>

</template>

<style lang="scss">

    .mainHome {
        &__container {
            overflow: visible;
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
        }
    }

</style>

