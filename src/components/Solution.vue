<script setup lang="ts">


import ExtendMenuIcon from "../../public/ExtendMenuIcon.vue";
import {computed, onBeforeUnmount, onMounted, Ref, ref, watch} from "vue";
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



let codeContent = ref("[loading code]")


onMounted(() => {

    const regex = /object(?:[^}]*})+/
    const loadCode = async () => {
        const response = await fetch(window.location.pathname.split('/').slice(0, -1).join('/') + props.codeFile)
        codeContent.value = "\n" + (await response.text()).match(regex)[0] + "\n"
        return codeContent.value
    }

    loadCode()

})


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
            v-animated-if.height.thenWidth="extended"
            data-v-base-width="74dvw"
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
                    base-width="74dvw"
                    horizontal-expend="12.5dvw"
                    :hidden-dependency="`package core; interface Solution { fun entryPoint(vararg args: String) }`"
                    :dependencies="['core.Solution']"
                    :unnamed-args="parameters.map(({value}) => value.value?.toString()).filter((it) => it != null)"
                    :editable="false"
                    :hide-stuff="false"
                    :hidden-outside-main="codeContent"
                    :sample-code="`${codeFile.split('/').pop().replace('.kt', '')}.entryPoint(*args)`"
                />
            </div>
        </div>


    </div>



</template>

<style lang="scss">

@import "../assets/variables.scss";


.puzzle-solution {
    //position: relative;

    transition: height 0.33s ease;

    align-self: center;
    padding-inline: 1.5em;
    padding-block: 1.33em;
    border-radius: 36px;

    display: flex;
    flex-direction: column;
    align-items: center;

    //    color: #243535;
    background-color: rgba(84, 142, 142, 0.4);;


    min-width: calc(3em + 74dvw);
    max-width: min(96dvw, calc(3em + 94dvw));
    width: fit-content;
    &__content, &__content__kotlin {
        max-width: calc(min(96dvw, (94dvw + 3em)) - 3em);
        width: fit-content;
    }

    &__extend {
        display: flex;
        align-items: center;
        gap: 10px;
        cursor: pointer;
        transition: color 0.2s ease;
        width: 100%;

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


        &__description {
            display: flex;
            flex-direction: column;
            align-items: stretch;
            gap: .75em;

            & tab {
                display: inline-block;
                width: 2em;
            }

        }

    }


}

</style>