<script setup lang="ts">

const props = withDefaults(defineProps<{
    spaces?: number,
    widthPerSpace?: string
}>(), {
    spaces: 4,
    widthPerSpace: ".5em"
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
}


</style>