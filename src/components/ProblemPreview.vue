<script setup lang="ts">

import {computed} from "vue";
import {useWindowSize} from "@vueuse/core";


const windowSize = useWindowSize({ includeScrollbar: false });
const width = computed(() => {
    const minSize = windowSize.width.value * 0.06 + 270;
    const objectsCount = Math.floor(windowSize.width.value / minSize);
    return (windowSize.width.value / Math.max(objectsCount, 1)) + "px";
})



</script>

<template>

    <div class="problem-preview__container">
        <div class="problem-preview">


        </div>
    </div>


</template>

<style lang="scss">

    .problem-preview {
        &__container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: calc(v-bind(width) * 1.2);
            width: v-bind(width);
        }


        height: 86%;
        width: 86%;
        background-color: aquamarine;
        box-shadow: inset 0 0 12dvw .12dvw rgba(0, 0, 0, 0.3),
                    inset 0 0 1dvw .035dvw rgba(0, 0, 0, 0.4),
                    0 0 8.4dvw -1.4dvw rgba(0, 0, 0, 0.5);
        border: 1px solid black;
        border-radius: calc(v-bind(width) * .12);
        box-sizing: border-box;



    }



</style>