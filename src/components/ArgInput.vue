<script setup lang="ts" xmlns="http://www.w3.org/1999/html">

import {computed, onUnmounted, ref, Ref, WritableComputedRef} from "vue";
import CloseX from "@/../public/CloseX.vue";
import {vFocus, vFocusEnd} from "@/directives";


const props = withDefaults(defineProps<{
    fields: {
        description: string,
        type: string,
        value: Ref<any>,
    }[];
    additionalArgs?: Array<{ name: string, value: string }> | undefined;
}>(), {
    additionalArgs: undefined,
})

const optionalArgsExtra: Ref<Array<{ hovered: boolean, nameChanged: boolean, lines: number, mirror?: HTMLElement }>> = ref([])

const textareaRef: (index: number) => WritableComputedRef<string> = (index: number) => computed({
    get: () => props.additionalArgs[index].name,
    set: (value: string) => { props.additionalArgs[index].name = value.replace(/\n/g, '')}
})
const linesOf: (el: HTMLElement) => number = (el: HTMLElement) => {
    if (!el) return 1;
    const temp = document.createElement('span')
    temp.style.position = 'absolute'
    temp.style.visibility = 'hidden'
    temp.innerText = '1'
    el.appendChild(temp)
    const lineHeight = parseFloat(getComputedStyle(temp).height)
    el.removeChild(temp)
    return Math.round(el.scrollHeight / lineHeight);
}
const mirrorObserver: ResizeObserver = new ResizeObserver((entries) => {
    entries.forEach(entry => {
        optionalArgsExtra.value.forEach((it) => {
            if (it.mirror == entry.target as HTMLElement) {
                it.lines = linesOf(it.mirror)
            }
        })
    })
});

function addArg() {
    optionalArgsExtra.value.push({ hovered: false, nameChanged: false, lines: 1 })
    props.additionalArgs.push({
        name: `arg${props.additionalArgs.length + 1 + props.fields.length}`,
        value: ""
    })
}

function removeArg(index: number) {
    props.additionalArgs.splice(index, 1)
    optionalArgsExtra.value.splice(index, 1)
    optionalArgsExtra.value.forEach((value, index) => {
        if (!value.nameChanged)
            for (let i = index; i < props.additionalArgs.length; i++)
                props.additionalArgs[i].name = `arg${i + 1 + props.fields.length}`
    })
}

onUnmounted(() => {
    mirrorObserver.disconnect()
})

</script>

<template>


    <div class="arg-input__container">

        <h3>Configure your Para&shy;meters: </h3>

        <div
            class="arg-input"
            v-for="field in fields"
        >
            <span class="arg-input__description">
                {{field.description}}
            </span>

            <input
                class="arg-input__input"
                :type="field.type"
                :required="field.type != 'string'"
                v-focus-end
                v-model="field.value.value"
            >
        </div>

        <span
            class="arg-input arg-input__custom-arg"
            v-if="additionalArgs"
            v-for="{ arg, index, name } in additionalArgs.map((it, i) => ({ arg: it, index: i, name: textareaRef(i) }))"
        >
            <span
                class="arg-input__arg-name-container"
                >
                <span
                    class="arg-input__description arg-input__arg-name-mirror"
                    :ref="el => {
                        if (!optionalArgsExtra[index].mirror) mirrorObserver.observe(el as HTMLElement)
                        optionalArgsExtra[index].lines = linesOf(el as HTMLElement)
                        optionalArgsExtra[index].mirror = el as HTMLElement
                    }"
                >
                    {{name}}
                </span>
                <textarea
                    type="text"
                    class="arg-input__description arg-input__arg-name-input"
                    v-model="name.value"
                    spellcheck="false"
                    @keydown.enter.prevent
                    :rows="optionalArgsExtra[index].lines"
                />
            </span>
            <span class="arg-input__input-container">
                <CloseX
                    class="arg-input__remove-arg"
                    @click="removeArg(index)"
                    :color="optionalArgsExtra[index].hovered == true ? '#598080' : '#3a6262'"
                    @mouseenter="() => { optionalArgsExtra[index].hovered = true }"
                    @mouseleave="() => { optionalArgsExtra[index].hovered = false }"
                />
                <input
                    class="arg-input__input"
                    type="text"
                    placeholder="string"
                    v-focus-end
                    v-focus.select="true"
                    v-model="arg.value"
                >
            </span>
        </span>
        <div
            class="arg-input__add-arg arg-input"
            v-if="additionalArgs"
            @click="addArg"
        >
            Add Argument
        </div>

    </div>


</template>

<style lang="scss">

@import "../assets/variables";

.arg-input {

    display: flex;
    flex-direction: row;
    align-items: stretch;
    @media (max-width: 500px) {
        gap: 5px;
    }

    padding: 10px;
    border-radius: 5px;

    background-color: #243535;
    transition: background-color .1s ease-in-out;


    @media (max-width: 500px) {
        flex-direction: column;
        align-items: center;
    }

    &__add-arg:hover {
        background-color: darken(#243535, 2%);
    }
    &__remove-arg {
        height: 24px;
        padding: 4px;
        flex-shrink: 0;
        align-self: center;
    }


    &__custom-arg {
        padding: 5px;
        & .arg-input__input {
            margin: 5px;
            @media (min-width: 500.001px) {
                margin-inline-start: 0;
            }
            @media (max-width: 500px) {
                margin-block-start: 0;
            }

            &-container {
                display: flex;
                flex-direction: row;
                align-items: center;
                gap: 3px;
                align-self: stretch;
                padding-inline-start: 3px;
            }
        }
    }
    &__arg-name {
        &-container {
            position: relative;
            width: 100%;
            display: inline-grid;
            align-items: center;

            padding-inline: 5px;
            padding-block: 2.5px;
            border-radius: 5px;

            &::after {
                position: absolute;
                content: "";
                width: 100%;
                height: 100%;
                border-radius: 5px;
                transition: opacity .1s ease-in-out;
                opacity: 0;
                background: linear-gradient(to right, rgba(0, 0, 0, 0.2) 0%, rgba(0, 0, 0, 0) 100%);
            }
            &:hover::after,
            &:focus-within::after {
                opacity: 1;
            }

            & .arg-input__arg-name-input,
            & .arg-input__arg-name-mirror {
                grid-area: 1 / 1 /-1 /-1;
            }
        }
        &-input {
            z-index: 1;
            border: none;
            padding: 0;
            background: transparent;
            resize: none;
            overflow: hidden;
            display: flex;
            align-items: center;
            @media (max-width: 500px) {
                text-align: center;
            }
        }
        &-mirror {
            visibility: hidden;
            overflow-wrap: anywhere;
        }
    }

    &__description {
        display: flex;
        flex-direction: row;
        align-items: center;
        font-size: 1em;
        @include ifSmall {
            text-align: center;
        }
    }

    &__container {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        gap: 6px;

        & h3 {
            margin-block: 10px;
        }

    }

    & input::-webkit-inner-spin-button,
    & input::-webkit-outer-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }
    /* Für Firefox */   // (chatgpt)
    & input[type=number] {
        appearance: textfield;
    }

    &__input {
        @media (max-width: 500px) {
            margin-inline-start: 0;
            width: 100%;
        }

        width: max(150px, 16dvw);
        text-align: end;
        margin-inline-start: auto;

        padding-inline: 5px;
        padding-block-start: 2px;
        border-radius: 3px;

        background-color: darkslategray;
        border-block-start: none;
        border-inline-start: none;

        font-size: (15/16)em;
        @include ifSmall {
            font-size: .94em;
        }
        align-content: center;

        transition:
            background-color 0.1s ease-in-out;

        &:valid {
            border-color: #447844;
        }
        &:invalid {
            border-color: #a83c3c;
            font-style: italic;
        }
        &:focus-visible {
            background-color: lighten(darkslategray, 6%);
        }
        &::-webkit-input-placeholder {
            color: #c3c3c3;
        }
    }
}

</style>