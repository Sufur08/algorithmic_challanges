<script setup>

import ProblemPreview from "@/components/ProblemPreview.vue";
</script>

<template>

    <div class="mainHome__container">
        <ProblemPreview img="./previewImgs/coins.webp" title="The Coins" link-to="/coin-problem"/>
    </div>

</template>

<style lang="scss">

    .mainHome {
        &__container {
            padding-block-start: 200px;
            overflow: visible;
        }
    }

</style>

