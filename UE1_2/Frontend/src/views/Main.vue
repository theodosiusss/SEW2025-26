<script setup lang="ts">
import axios from 'axios';
import {onMounted, ref} from "vue";
import Song from "@/components/Song.vue";

const songs = ref({});

onMounted(async () => {
  fetchData();
})


function fetchData(){
  axios.get("http://localhost:8080/api/songs").then(response => {
    songs.value = response.data;
    console.log(response.data);
  });
}

</script>

<template>
  <RouterLink to="/add">Neue Songs Hinzufügen</RouterLink>
  <div v-if="songs" class="songs">

    <div v-for="song in songs" :key="song.id">

      <Song :song="song" @deleted="fetchData" />

    </div>
  </div>
</template>

<style scoped>
.songs {
  display: flex;
  flex-wrap: wrap;
}

a {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 1.5rem;
  padding: 0.8rem 1.5rem;
  border-radius: 1.5rem;
  font-size: 1.2rem;
  font-weight: bold;
  text-decoration: none;
  text-transform: uppercase;
  background: linear-gradient(135deg, #00ffcc, #ff00ff, #ffff00);
  color: #111;
  box-shadow: 0 0 12px rgba(255, 0, 255, 0.6),
  0 0 20px rgba(0, 255, 255, 0.6);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  animation: linkBounce 1.8s infinite ease-in-out;
  cursor: pointer;
}

a:hover {
  transform: scale(1.1) rotate(-2deg);
  box-shadow: 0 0 18px rgba(255, 255, 0, 0.9),
  0 0 30px rgba(0, 255, 255, 0.9);
}

@keyframes linkBounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}
</style>

