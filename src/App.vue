<script setup lang="ts">






</script>

<template>

    <div class="app__container my-scroll-container">
        <header>
            <h1>Header</h1>
        </header>

        <main class="">
            <RouterView/>
        </main>
    </div>

</template>

<style lang="scss">
    @import "./assets/variables.scss";

    .my-scroll-container::-webkit-scrollbar {
        width: 8px;
    }
    .my-scroll-container::-webkit-scrollbar-track {
        background: transparent;
    }
    .my-scroll-container::-webkit-scrollbar-thumb {
        background-color: rgba(0,0,0,0.3);
        border-radius: 4px;
    }

    .app__container {
        display: flex;
        flex-direction: column;
        overflow: auto;
        width: 100dvw;


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
