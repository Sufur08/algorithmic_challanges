<script setup lang="ts">

</script>

<template>

    <div class="footer">
        <p>
            This page uses google fonts to load JetBrains&nbsp;Mono, so if google collects data that way (which I think has happened in the past), I probably have to tell you. I did now.
        </p>
        <p>
            Created by Rufus Sommer
        </p>
        <p>
            Click to see the <router-link to="/about">About</router-link>-Page
        </p>
    </div>

</template>

<style lang="scss">

.footer {
    @import "../assets/variables.scss";

    padding-block-start: 7dvh;
    padding-block-end: 10dvh;
    padding-inline: 6dvw;

    display: flex;
    flex-direction: column;
    justify-content: start;
    gap: 1.2em;

    color: $blackAccent;

    @include ifSmall {
        font-size: .96em;
    }

    & p * {
        color: $blackAccent;
    }

}


</style>