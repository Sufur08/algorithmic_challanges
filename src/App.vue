<script setup lang="ts">






</script>

<template>

    <div class="app__container">
        <header>
            <h1>Header</h1>
        </header>

        <main>
            <RouterView/>
        </main>
    </div>

</template>

<style lang="scss">
    @import "./assets/variables.scss";

    .app__container {
        display: flex;
        flex-direction: column;
        min-height: 100dvh;


        & header {
            position: fixed;
            height: inRange(12dvh, 34px, 100px);
            width: 100%;
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: center;
        }

    }




</style>
