<script setup lang="ts">

const props = withDefaults(defineProps<{
    spaces?: number | string, // a number in a string seems to work as well
    widthPerSpace?: string,
    verticalSpacing?: string,
}>(), {
    spaces: 4,
    widthPerSpace: ".5em",
    verticalSpacing: ".1em",
})

</script>

<template>

    <span class="indented">
        <slot/>
    </span>

</template>

<style lang="scss">

.indented {
    display: inline-block;
    padding-inline-start: calc(v-bind(spaces) * v-bind(widthPerSpace));
    margin-block: v-bind(verticalSpacing);

    &:last-child {
        margin-block-end: 0;
    }
}


</style>