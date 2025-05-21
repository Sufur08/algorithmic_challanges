<script setup lang="ts">


const props = withDefaults(defineProps<{
    overflowWidth?: string,
}>(), {
    overflowWidth: "6.4dvw"
})

</script>

<template>

    <div class="problem-description">
        <slot/>
    </div>

</template>

<style lang="scss">

.problem-description {


    align-self: center;
    min-width: calc(100% + v-bind(overflowWidth));
    padding-inline: 1.6em;
    padding-block: 1.4em;
    border-radius: 36px;

    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 1.2em;

//    color: #243535;
    background-color: #67aeae66;;

    &__question {
        //font-size: 16px;
        font-weight: bold;
    }

}

</style>