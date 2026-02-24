import { createRouter, createWebHistory } from 'vue-router'

import Main from "@/views/Main.vue";
import Form from "@/views/Form.vue";
import ArtistForm from "@/views/ArtistForm.vue";
import Artists from "@/views/Artists.vue";
import SongDetail from "@/views/SongDetail.vue";
import Login from "@/views/Login.vue";
import {useUserStore} from "@/store/userStore.ts";

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
            meta: { requiresAuth: true }
        },
        {
            path: '/add',
            name: 'add',
            component: Form,
            props: true,
            meta: { requiresAuth: true }
        },
        {
            path: '/addArtist',
            name: 'addArtist',
            component: ArtistForm,
            meta: { requiresAuth: true }
        },
        {
            path: '/artists',
            name: 'artists',
            component: Artists,
            meta: { requiresAuth: true }
        },
        {
            path: '/song-detail/:id',
            name: 'songDetail',
            component: SongDetail,
            props: true,
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
        },
    ],

})

router.beforeEach(async (to) => {
    const auth = useUserStore();

    if (to.meta.requiresAuth) {
        await auth.checkLogin(true);
        if (!auth.isLoggedIn) {
            return { name: "login" };
        }
    }
});

export default router;
