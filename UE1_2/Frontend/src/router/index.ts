import { createRouter, createWebHistory } from 'vue-router'

import Main from "@/views/Main.vue";
import Form from "@/views/Form.vue";
import ArtistForm from "@/views/ArtistForm.vue";
import Artists from "@/views/Artists.vue";
import SongDetail from "@/views/SongDetail.vue";

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
        {
            path: '/addArtist',
            name: 'addArtist',
            component: ArtistForm,
        },
        {
            path: '/artists',
            name: 'artists',
            component: Artists,
        },
        {
            path: '/song-detail/:id',
            name: 'songDetail',
            component: SongDetail,
            props: true,
        },
    ],
})

export default router;
