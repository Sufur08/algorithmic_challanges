<script setup lang="ts">

import {onMounted, ref} from "vue";
import playground from 'kotlin-playground'

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
    args: {
        type: Array<Arg>,
        default: () => []
    },
    sampleCode: {
        type: String,
        default: "    // your code here"
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

let instance = null;

onMounted(() => {
    playground('code', {
        onChange: (code) => {
            codeContent.value = code;
        },
        getInstance: (it) => {
            console.log(it);
            instance = it;
        },
        callback: () => {
            instance.codemirror.on("change", () => {
                changeSize();
            });
            changeSize();
        }
    });


});


function changeSize() {
    //const doc = instance.codemirror.doc;//doc.lineCount();
    const height = instance.codemirror.doc.height;
    //const mul = instance.codemirror.defaultTextHeight();
    //console.log(height);
    instance.codemirror.setSize(null, Math.max(50, Math.min(height + 25, props.maxHeight)))
}



</script>

<template>

    <div @click="changeSize"> ewatthhb dv</div>

    <div class="kotlin-code__container">
        <code
            class="kotlin-code"
            theme="darcula"
            match-brackets="true"
            :args="props.args.map(({value}) => { return value }).join(' ')"
        ><!-- get theme by storage -->
            <!-- don't work:
            data-shorter-height="100"
            data-output-height="100"
            -->
            {{ codeContent }}
        </code>
    </div>





</template>

<style lang="scss">




.kotlin-code {
    &__container {
        visibility: visible;
    }
}

</style>