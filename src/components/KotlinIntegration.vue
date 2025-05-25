<script setup lang="ts">

import {computed, onBeforeUnmount, onMounted, Ref, ref, watch} from "vue";
import playground from 'kotlin-playground';
import {useWindowSize} from "@vueuse/core";

const theme: Ref<string> = ref("darcula");

type Arg = {
    name: string,
    kotlinType: string,
    kotlinTypeParser: (it: string) => string,
    value: string,
}

const props = withDefaults(defineProps<{
    maxHeight?: number,
    horizontalExpend?: string,
    baseWidth?: string  // resting on knife \ you heavy soul \ don't you drown \ and float away
    args?: Array<Arg>,
    unnamedArgs?: Array<string>,
    sampleCode?: string,
    hiddenInMain?: string,
    hiddenOutsideMain?: string,
    hiddenDependency?: string | undefined,
    dependencies?: Array<string>,
    editable?: boolean,
    editableStrict?: boolean,
    hideStuff?: boolean,
}>(), {
    maxHeight: window.innerHeight * 0.8,
    horizontalExpend: "calc(min(120%, 100dvw) - 100%)",
    args: () => [],
    unnamedArgs: () => [],
    sampleCode: `
    // your code here
    // print the result
    println(args.joinToString())`,
    hiddenInMain: "",
    hiddenOutsideMain: "",
    hiddenDependency: undefined,
    dependencies: () => [],
    baseWidth: "100%",
    editable: true,
    editableStrict: false,
    hideStuff: true,
})




const startingCode = computed(() =>
`${props.dependencies.filter((it: string) => it != "").map((it: string) => `import ${it}`).join("\n")}
${props.hiddenOutsideMain}
fun main(args: Array\<\String\>\) {${props.hiddenInMain ? "\n" + props.hiddenInMain : ""}${props.hideStuff ? "\n//sampleStart" : ""}
    ${props.args.length > 0 ? props.args.map((value, index) => (
        `var ${value.name}: ${value.kotlinType} = ${value.kotlinTypeParser(`args[${index}]`)}`
    )).join("\n\t") + "\n" : ""}${props.sampleCode}${props.hideStuff ? " //sampleEnd" : ""}
}`)
const codeContent = ref(startingCode.value)
const codeRef = ref(null);
const expandedWidth = computed(() => { return `calc(${props.baseWidth} + ${props.horizontalExpend})` })

const allArgs = computed(() =>
    [...props.args.map(({value}) => { return value }), ...props.unnamedArgs]
)
function replaceWhitespaces(list: string[]) {
    return list.map(it => it.replace(/\s/g, '_'))
}

const expanded = ref(false);
const editable = ref(props.editable);

const { width } = useWindowSize();

let instance = null;
let checkID

let compiledMain: Ref<(args: Array<string>) => any> = ref(null);


onMounted(() => {
    function check() {
        if (codeRef.value.offsetParent !== null) {
            playground(codeRef.value, {
                onChange: (code: string) => {
                    codeContent.value = code;
                },
                getJsCode: (code: string) => {

                    if (code) {
                        compiledMain.value = (args: Array<string>) => {
                            // Die ursprüngliche main-Aufrufzeile finden
                            const mainCallRegex = /main\(\[(?:"(?:\\.|[^"\\])*"(?:,\s*)?)*]\);/;

                            // Bereite die Parameter vor - String-Array in Kotlin-Format
                            const paramsStr = JSON.stringify(args);
                            const mainCall = `main(${paramsStr});`;

                            // Ersetze den ursprünglichen main()-Aufruf durch unseren
                            const modifiedCode = code.replace(mainCallRegex, mainCall);

                            // Führe den modifizierten Code aus und erfasse den Rückgabewert
                            try {
                                //const execFunc = new Function(returnValueCode);
                                return eval(modifiedCode);
                            } catch (error) {
                                console.log("Failed running kotlin code as javascript: ", error);
                                return error; // Den Fehler weiterwerfen, damit der Aufrufer ihn behandeln kann
                            }
                        }
                    }


                    //console.log(code);
                    //console.log(eval(code));
                    //compiledMain.value(["30"])
                    //   console.log("return: " + runWithParams(code, ["20"]))
                },
                getInstance: (it) => {
                    console.log(it);
                    instance = it;
                },
                callback: () => {
                    instance.codemirror.on("change", () => {
                        changeSize();
                    });
//            instance.state.jsLibs = ["/kotlin.js"];
                    changeSize();
                    instance.state.args = replaceWhitespaces(allArgs.value).join(' ')
                    const oldOnUpdate = instance.onUpdate;
                    instance.onUpdate = (it)  => {
                        if (it.output && compiledMain.value && it.output != compiledMain.value(allArgs.value))
                            setOutput(compiledMain.value(allArgs.value))

                        if (!editable.value) {
                            instance.codemirror.setOption("readOnly", true);
                            instance.codemirror.setOption("cursorBlinkRate", -1);
                        }

                        oldOnUpdate(it);
                    }
                    instance.update({})

                }
            });
            return;
        }
        checkID = requestAnimationFrame(check)
    }
     checkID = requestAnimationFrame(check)

});

onBeforeUnmount(() => {
    cancelAnimationFrame(checkID);
})


watch(startingCode, (newCode) => {
    codeContent.value = newCode
})
watch(editable, (newEditable) => {
    if (newEditable) {
        instance?.codemirror.setOption("readOnly", false);
        instance?.codemirror.setOption("cursorBlinkRate", 530);
    } else instance?.update({})
})
watch(
    () => allArgs.value,
    (newArgs) => {
        instance.state.args = replaceWhitespaces(newArgs).join(' ')
        if (compiledMain.value) {
            setOutput(compiledMain.value(newArgs))
        } else {
            instance.execute();
        }
    }
)


function changeSize() {
    //const doc = instance.codemirror.doc;//doc.lineCount();
    const height = instance.codemirror.doc.height;
    //const mul = instance.codemirror.defaultTextHeight();
    //console.log(height);
    instance.codemirror.setSize(null, Math.max(50, Math.min(height + 25, props.maxHeight)))
    //instance.codemirror.display.wrapper.style.maxHeight = `${props.maxHeight}px`
}

function setOutput(output: any) {
    if (typeof output === "string")
    {
        instance.state.output = output
        instance.state.exception = null
    } else {
        instance.state.output = ""
        instance.state.exception = output
    }
    instance.update({ openConsole: true })
}

function setCode(code: string) {
    if (instance) {
        if (instance.state.folded)
        {
            const split = code.split(/\/\/sampleStart\n|\/\/sampleEnd\n/g)
            instance.codemirror.setValue(split.length > 1 ? split[1] : split[0])
        }
        else
            instance.codemirror.setValue(code.replace(/\/\/sampleStart\n|\/\/sampleEnd\n/g, ''))
        instance.update(instance.state)
    }
}

function resetCode() {
    setCode(startingCode.value)
}


// future idea: i read importSuggestions in instance

</script>

<template>

    <div class="kotlin-code__positioner">
        <div
            class="kotlin-code__container"
            :class="{'--expanded': (props.horizontalExpend && expanded) || width <= 500}"
            ref="containerRef"
        >
            <div
                class="kotlin-code__options-container"
            >
                <span
                    class="kotlin-code__option kotlin-code__expander"
                    v-if="props.horizontalExpend && width > 500"
                    @click="() => expanded = !expanded"
                >
                    {{ expanded ? "collapse" : "expand" }} horizontally
                </span>
                <span
                    class="kotlin-code__option kotlin-code__switch-editable"
                    v-if="!editableStrict"
                    @click="editable = !editable"
                >
                    make {{ editable ? 'read-only' : 'editable' }}
                </span>
                <span
                    class="kotlin-code__option kotlin-code__resetter"
                    @click="resetCode"
                >
                    reset code
                </span>
            </div>
            <code
                ref="codeRef"
                class="kotlin-code"
                theme="darcula"
                match-brackets="true"
                lines="true"
                data-target-platform="js"
                data-version="1.9.25"
            ><!-- get theme by storage -->
                {{ codeContent }}
                <textarea
                    v-if="hiddenDependency"
                    class="hidden-dependency"
                >
                    {{ hiddenDependency }}
                </textarea>
            </code>
        </div>
    </div>





</template>

<style lang="scss">




.kotlin-code {
    visibility: hidden;

    &__positioner {
        overflow: visible;
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    &__container {
        transition: width 0.2s ease-in-out;
        width: v-bind(baseWidth);
        &.--expanded {
            width: v-bind(expandedWidth);
        }
        & .output {
            overflow-wrap: break-word;
        }
        & .code-output {
            white-space: pre-wrap;
            max-height: 42dvh;
        }

    }

    &__options-container {
        display: flex;
        gap: 3%;
        justify-content: space-between;

        & span {
            &.kotlin-code__option {
                font-size: 10px;
                color: #afb1b3;
                text-decoration: underline;
                cursor: pointer;
            }
            &.kotlin-code__expander {
                margin-inline-end: auto;
            }
            &.kotlin-code__switch-editable {
                color: #cfd2d8;
            }
            &.kotlin-code__resetter {
                color: #f86969;
            }
        }

    }
}

</style>