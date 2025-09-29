<script setup lang="ts">
import axios from 'axios';
import {ref} from "vue";
import Song from "@/Song.vue";

const songs = ref({});

axios.get("http://localhost:8080/api/songs").then(response => {
  songs.value = response.data;
  console.log(response.data);
});

</script>

<template>
  <div v-if="songs" class="songs">

    <div v-for="song in songs" :key="song.id">

        <Song :song="song" />

      </div>
  </div>
</template>

<style scoped>
.songs {
  display: flex;
  flex-wrap: wrap;
}
</style>

