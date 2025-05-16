import {defineStore} from "pinia";
import {useRouter} from "vue-router";
import {ref, watch} from "vue";


export const useRouteSettings = defineStore("routeSettings", () => {

    const header = ref("Header");

    const router = useRouter();

    function reset() {
        header.value = "Header"
    }

    watch(
        () => router.currentRoute.value,
        () => {
            reset();
        }
    );

    return {
        header
    }
})




