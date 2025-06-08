<script setup lang="ts">


import {useRouteSettings} from "@/storages";
import WithOverlayScrollbar from "@/components/elements/WithOverlayScrollbar.vue";

const state = useRouteSettings()



</script>

<template>

    <with-overlay-scrollbar
        clazz="app__container"
        sizer-class="app__sizer"
    >
        <header>
            <h1>{{ state.header }}</h1>
        </header>

        <main class="">
            <RouterView/>
        </main>

        <footer>

            About
        </footer>
    </with-overlay-scrollbar>

</template>

<style lang="scss">
    @import "./assets/variables.scss";

    .app__sizer {
        width: 100dvw;
        height: 100dvh;
    }

    .app__container {
        display: flex;
        flex-direction: column;


        & > main {
            margin-block-start: 4dvh;
            margin-block-end: 10dvh;
        }


        & > header {
            z-index: 10000;
            position: sticky;
            top: 0;
            width: 100dvw;
            padding-block-start: 4px;
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: center;

            pointer-events: none;
            & > * {
                pointer-events: all;
            }

            & > h1 {
                margin: 0;
                padding-inline: 4%;
                text-shadow:
                    0 0 26px black,
                    0 0 36px black;
                font-size: 2.75em;
                text-align: center;
                @include ifSmall {
                    font-size: 2.25em;
                }
            }

        }

        & > footer {
            background-color: $accent1;
            width: 100dvw;
            margin-block-start: auto;
            border-block-start: $blackAccent solid 2px;
            padding-block: 120px;

            display: flex;
            justify-content: center;


            color: $blackAccent;
//            font-weight: bold;

        }

    }




</style>
