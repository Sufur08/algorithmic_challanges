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
        min-height: 100dvh;


        & > main {
            margin-top: inRange(12dvh, 34px, 100px);

        }


        & > header {
            position: fixed;
            height: inRange(12dvh, 34px, 100px);
            width: 100dvw;
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: center;

            & > h1 {
                text-shadow:
                    0 0 26px black,
                    0 0 36px black;
            }

        }

    }




</style>
