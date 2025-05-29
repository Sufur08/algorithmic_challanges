<script setup lang="ts">

const props = withDefaults(defineProps<{
    height?: string,
    width?: string,
}>(), {
    height: "0",
    width: "0",
})

</script>

<template>

    <div class="spacer">
        <slot/>
    </div>

</template>

<style lang="scss">

.spacer {
    height: v-bind(height);
    width: v-bind(width);
}

</style>