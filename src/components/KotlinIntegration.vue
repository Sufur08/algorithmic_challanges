<script setup lang="ts">

import {computed, onMounted, Ref, ref, watch} from "vue";
import playground from 'kotlin-playground';
import {useWindowSize} from "@vueuse/core";

const theme: Ref<string> = ref("darcula");

interface Arg {
    name: string,
    kotlinType: string,
    kotlinTypeParser: (it: string) => string,
    value: string,
}

const props = defineProps({
    maxHeight: {
        type: Number,
        default: window.innerHeight * 0.8
    },
    horizontalExpend: {
        type: String,
        default: "calc(min(120%, 100dvw) - 100%)"
    },
    args: {
        type: Array<Arg>,
        default: () => []
    },
    sampleCode: {
        type: String,
        default: `
    // your code here
    // print the result
    println(args.joinToString())`
    },
    prescriptedCode: String
})




const startingCode = `
fun main(args: Array\<\String\>\) {${props.prescriptedCode ? "\n" + props.prescriptedCode : ""}
//sampleStart
    ${props.args.map((value, index) => (
        `var ${value.name}: ${value.kotlinType} = ${value.kotlinTypeParser(`args[${index}]`)}`
    ))}
    ${props.sampleCode}
//sampleEnd
}
`
const codeContent = ref(startingCode)
const containerRef = ref(null);
const expandedWidth = computed(() => { return `calc(100% + ${props.horizontalExpend})` })

const expanded = ref(false);
const { width } = useWindowSize();

let instance = null;

let compiledMain: Ref<(args: Array<string>) => any> = ref(null);

onMounted(() => {
    playground('code', {
        onChange: (code) => {
            codeContent.value = code;
        },
        getJsCode: (code: string) => {

            if (code) {
                compiledMain.value = (args: Array<string>) => {
                    // Die ursprüngliche main-Aufrufzeile finden
                    const mainCallRegex = /main\(\["[^"]*"\]\);/;

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
        }
    });

});

watch(
    () => props.args.map(({value}) => { return value }),
    (newArgs) => {
        instance.state.args = newArgs.join(' ')
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


// future idea: compile to js and run faster with different args

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
                class="kotlin-code__expander__container"
                v-if="props.horizontalExpend && width > 500"
            >
            <span
                class="kotlin-code__expander__button"
                @click="() => expanded = !expanded"
            >{{ expanded ? "collapse" : "expand" }} horizontally</span>
            </div>
            <code

                class="kotlin-code"
                theme="darcula"
                match-brackets="true"
                data-target-platform="js"
                data-version="1.9.25"
                :args="props.args.map(({value}) => { return value }).join(' ')"
            ><!-- get theme by storage -->
                <!-- reactivity of args is not is redundant here -->
                <!-- don't work:
                data-shorter-height="100"
                data-output-height="100"
                -->
                {{ codeContent }}
            </code>
        </div>
    </div>





</template>

<style lang="scss">




.kotlin-code {
    &__positioner {
        overflow: visible;
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    &__container {
        transition: width 0.2s ease-in-out;
        width: 100%;
        &.--expanded {
            width: v-bind(expandedWidth);
        }
    }

    &__expander {
        &__container {
            display: flex;
        }

        &__button {
            font-size: 10px;
            color: #afb1b3;
            text-decoration: underline;
            cursor: pointer;
        }
    }
}

</style>