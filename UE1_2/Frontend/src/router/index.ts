import { createRouter, createWebHistory } from 'vue-router'

import Main from "@/views/Main.vue";
import Form from "@/views/Form.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: Main,
        },
        {
            path: '/add/:id',
            name: 'addId',
            component: Form,
            props: true,
        },
        {
            path: '/add',
            name: 'add',
            component: Form,
            props: true,
        },
    ],
})

export default router;
