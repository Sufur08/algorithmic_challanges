<script setup lang="ts">


import ExtendMenuIcon from "../../public/ExtendMenuIcon.vue";
import {Ref, ref} from "vue";
import KotlinIntegration from "@/components/KotlinIntegration.vue";
import ArgInput from "@/components/ArgInput.vue";
import {vAnimatedIf} from "@/directives";


const props = withDefaults(defineProps<{
    codeFile: string,
    parameters: {
        description: string,
        type: string,
        value: Ref<any>,
    }[],
    overflowWidth?: string,
}>(), {
    overflowWidth: "6.4dvw"
})


const extended = ref(false)
const hovered = ref(false)



</script>

<template>


    <div class="puzzle-solution">

        <span
            class="puzzle-solution__extend"
            @click="extended = !extended"
        >
            <ExtendMenuIcon
                class="puzzle-solution__extend-icon"
                :class="{
                    'puzzle-solution__extend-icon--extended': extended,
                    'puzzle-solution__extend-icon--hovered': hovered
                }"
            />
            <h2>
                Show <b>a</b> solution
            </h2>
        </span>

        <div
            class="puzzle-solution__content"
            v-animated-if.height="extended"
        >
            <div
                class="puzzle-solution__content__description"
            >
                <slot/>
            </div>
            <ArgInput
                :fields="parameters"
            />
            <div
                class="puzzle-solution__content__kotlin"
            >
                <KotlinIntegration
                    base-width="94%"
                    horizontal-expend="20%"
                    :hidden-dependency="`package core; interface Solution { fun entryPoint(vararg args: String) }`"
                    :dependencies="['core.Solution']"
                />
            </div>
        </div>


    </div>




</template>

<style lang="scss">

@import "../assets/variables.scss";


.puzzle-solution {
//    width: 100%;
    max-width: 110%;
    width: 80%;
    min-width: calc(100% + v-bind(overflowWidth));


    transition: height 0.33s ease;

    align-self: center;
    padding-inline: 1.5em;
    padding-block: 1.33em;
    border-radius: 36px;

    display: flex;
    flex-direction: column;
    align-content: center;



    //    color: #243535;
    background-color: rgba(84, 142, 142, 0.4);;


    &__extend {
        display: flex;
        align-items: center;
        gap: 10px;
        cursor: pointer;
        transition: color 0.2s ease;

        & > h2 {
            transition: color 0.1s ease;
            margin: 0;
        }

        &:hover > h2  {
            color: #c6dada;
        }
        &:hover &-icon {
            color: #6da1a1;
        }

        &-icon {
            height: 28px;

            transition:
                transform 0.1s ease,
                color 0.2s ease;
            transform-origin: center;
            transform: rotate(0deg);

            &--extended {
                transform: rotate(90deg);
            }
            //&--hovered {
              //  color: #97bdbd;
            //}
        }
    }

    &__content {
        display: flex;
        flex-direction: column;
        align-items: stretch;
        gap: 1em;

        margin-block-start: 1.4em;

    }


}

</style>