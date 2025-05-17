import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'
import {createRouter, createWebHashHistory} from "vue-router";
import MainHome from "@/components/sites/MainHome.vue";
import CoinProblem from "@/components/sites/problems/CoinProblem.vue";
import {createPinia} from "pinia";
import {applyDirectives} from "@/directives";

const routes = [
    { path: "/", component: MainHome },
    { path: "/coin-problem", component: CoinProblem }
]





const router = createRouter({
    history: createWebHashHistory(),
    routes: routes,
})

const app = createApp(App)

app.use(router)
app.use(createPinia())
applyDirectives(app)

app.mount('#app')
