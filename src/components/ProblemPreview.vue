<script setup lang="ts">

import {computed} from "vue";
import {useWindowSize} from "@vueuse/core";


const props = defineProps({
    img: String,
    title: String,
    linkTo: String,
    imgAltText: String,
})


const windowSize = useWindowSize({ includeScrollbar: false });
const minSize = computed(() => windowSize.width.value * 0.05 + 270)
const width = computed(() => {
    const objectsCount = Math.floor(windowSize.width.value / minSize.value);
    return (windowSize.width.value / Math.max(objectsCount, 1)) + "px";
})
const minSizeWidthDif  = computed(() => `calc(${width.value} - ${minSize.value}px)`)
const minSizePx = computed(() => `${minSize.value}px`)



</script>

<template>

    <div class="problem-preview__container">
        <router-link :to="linkTo" class="problem-preview">

            <div class="problem-preview__inner-shadow"/>

            <h2>{{title}}</h2>

            <img :src="img" :alt="imgAltText"/>

        </router-link>
    </div>


</template>

<style lang="scss">

    @import "../assets/variables.scss";

    .problem-preview {
        &__container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: calc(v-bind(width) * 1.2);
            width: v-bind(width);
        }


        transition:
            scale .2s ease-in-out,
            box-shadow .2s ease-in-out;
        &:hover {
            scale: 1.035;
            box-shadow:
                        0 0 8.4dvw -1dvw rgba(0, 0, 0, 0.55);
        }
        will-change: transform;
        $thisRawWidth: calc(v-bind(minSizePx) + v-bind(minSizeWidthDif) * .42);
        height: min(86%, calc($thisRawWidth * 1.2));
        width: min(86%, $thisRawWidth);
        box-shadow:
                    0 0 8.4dvw -1.4dvw rgba(0, 0, 0, 0.5);
        overflow: hidden;
        box-sizing: border-box;
        display: flex;
        justify-content: center;
        align-items: center;
        position: relative;
        $border-thickness: max(8px, v-bind(width) * .0275);
        border: $border-thickness solid $accent1;

        $border-radius: calc(v-bind(width) * .12);
        border-radius: $border-radius;


        &__inner-shadow {
            border-radius:  calc($border-radius - $border-thickness);
            box-sizing: border-box;
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            box-shadow: inset 0 0 1dvw .2dvw rgba(0, 0, 0, 0.5);
            opacity: .9;
            @media (hover: hover) {
                &:hover::before {
                    opacity: .75;
                }
            }
            &::before {
                content: '';
                display: block;
                width: 100%;
                height: 100%;
                z-index: 1;
                transition: opacity .2s ease-in-out;
                background: linear-gradient(to bottom, rgba(0, 0, 0, 0.1) 0%, rgba(0, 0, 0, 0.5) 100%);
                opacity: 1;
            }
        }

        & > img {
            position: absolute;
            z-index: -10;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
            align-content: center;
            text-align: center;
        }
        & > h2 {
            margin-block-end: 30%;
            position: absolute;
            z-index: 10;
            font-size: calc($thisRawWidth * .12);
            font-weight: 900;
            text-align: center;
            text-shadow: 0 0 1dvw rgba(0, 0, 0, 0.36);
        }



    }



</style>