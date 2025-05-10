<script setup lang="ts">

import {onMounted, ref} from "vue";

const props = defineProps({

})


const defaultStr = ref(`
fun main(args: Array\<\String\>\) {
//sampleStart
    println("Hello, World!")
    println(args.joinToString(", "))
//sampleEnd
}
`)
let playground = null;
let kotlinContainer = ref(null);



const mainArgs = ref(["arg1", "arg2", "arg3"]);

onMounted(() => {
    KotlinPlayground('.kotlin-container-for-ext', {
        onChange: (code) => { defaultStr.value = code }
    })
});


function onLoadExtScript() {

}


</script>

<template>

    <div @click=""> ewatthhb dv</div>

    <div class="own-kotlin__container">

        <div ref="kotlinContainer"
             class="kotlin-container-for-ext"
             theme="darcula"
             :args="mainArgs"
        >
            <code
                class="kotlin-code"
            >
                {{ defaultStr }}
            </code>
        </div>
    </div>







</template>

<style lang="scss">

.own-kotlin {
    &__container {
        height: 40px;
        width: 100%;

        & code {
            height: 60px;
            width: 100%;
        }
    }
}

</style>