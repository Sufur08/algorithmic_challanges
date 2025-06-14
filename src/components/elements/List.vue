<script setup lang="ts">


const props = withDefaults(defineProps<{
    numerationType?: string | undefined,
    inheritCustomNumerationType?: boolean,
    verticalSpaceAround?: string,
    verticalSpaceBetween?: string,
}>(), {
    numerationType: "1",
    inheritCustomNumerationType: false,
    verticalSpaceAround: ".1em",
    verticalSpaceBetween: "0",
})


</script>

<template>


    <ol class="list" :class="inheritCustomNumerationType ? 'list--inherit-num-type' : ''">
        <slot/>
    </ol>

</template>

<style lang="scss">

.list {
    display: flex;
    flex-direction: column;
    gap: v-bind(verticalSpaceBetween);

    margin-block: v-bind(verticalSpaceAround);
//    padding-inline-start: 38px;

    &--inherit-num-type {
        list-style-type: inherit;
    }
}


</style>