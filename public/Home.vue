<script setup lang="ts">

const props = withDefaults(defineProps<{
    color?: string,
    class?: string
}>(), {
    color: "#494949",
    class: ""
})


</script>

<template>
    <svg :fill="color" :class="props.class" viewBox="-4.5 0 32 32" xmlns="http://www.w3.org/2000/svg">
        <title>home</title>
        <path d="M19.469 12.594l3.625 3.313c0.438 0.406 0.313 0.719-0.281 0.719h-2.719v8.656c0 0.594-0.5 1.125-1.094 1.125h-4.719v-6.063c0-0.594-0.531-1.125-1.125-1.125h-2.969c-0.594 0-1.125 0.531-1.125 1.125v6.063h-4.719c-0.594 0-1.125-0.531-1.125-1.125v-8.656h-2.688c-0.594 0-0.719-0.313-0.281-0.719l10.594-9.625c0.438-0.406 1.188-0.406 1.656 0l2.406 2.156v-1.719c0-0.594 0.531-1.125 1.125-1.125h2.344c0.594 0 1.094 0.531 1.094 1.125v5.875z"></path>
    </svg>
</template>

<style lang="scss">

</style>