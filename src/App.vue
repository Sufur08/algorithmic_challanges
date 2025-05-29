<script setup lang="ts">


import {useRouteSettings} from "@/storages";

const state = useRouteSettings()



</script>

<template>

    <div class="app__container">
        <header>
            <h1>{{ state.header }}</h1>
        </header>

        <main class="">
            <RouterView/>
        </main>
    </div>

</template>

<style lang="scss">
    @import "./assets/variables.scss";

    .app__container {
        display: flex;
        flex-direction: column;
        overflow-x: hidden;
        width: 100dvw;
        height: 100dvh;


        & > main {
            margin-top: 3dvh;

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

    }




</style>
