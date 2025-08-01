<script setup lang="ts">


import {useRouteSettings} from "@/storages";
import WithOverlayScrollbar from "@/components/elements/WithOverlayScrollbar.vue";
import MyFooter from "@/components/MyFooter.vue";
import Home from "../public/Home.vue";

const state = useRouteSettings()



</script>

<template>

    <with-overlay-scrollbar
        clazz="app__container"
        sizer-class="app__sizer"
    >
        <header>
            <router-link to="/" class="app__header__home-icon-wrapper">
                <Home :color="'#477575'" class="app__header__home-icon"/>
            </router-link>
            <h1>{{ state.header }}</h1>
        </header>

        <main class="">
            <RouterView/>
        </main>

        <footer>
            <MyFooter/>
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
                margin-inline: calc(4em / 2.75);
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

        & .app__header__home-icon {
            height: 3.8em;
            filter: drop-shadow(0px 0px 10px rgba(0, 0, 0, .75));
            &-wrapper {
                height: 3.3em;
                width: 3.3em;
                padding: .4em;
                overflow: visible;
                display: flex;
                align-items: center;
                justify-content: center;
                position: absolute;
                left: 1dvw;
                align-self: start;
            }
        }

        & > footer {
            background-color: $accent1;
            width: 100dvw;
            margin-block-start: auto;
            border-block-start: $blackAccent solid 2px;

        }

    }




</style>
