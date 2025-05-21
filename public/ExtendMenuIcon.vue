<script setup lang="ts">


const props = withDefaults(defineProps<{
    class?: string | any
    width?: number
}>(), {
    class: "",
    width: 100
})

</script>

<template>


    <svg class="extend-icon" :class="props.class" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
        <path :stroke-width="width" stroke="currentColor" fill="currentColor" d="M256 120.768L306.432 64 768 512l-461.568 448L256 903.232 659.072 512z"/>
    </svg>

</template>

<style lang="scss">

.extend-icon {
    color: #5b9b9b;
}

</style>