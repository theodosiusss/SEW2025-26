<script setup lang="ts">

import {onMounted, ref} from "vue";
import type {ArtistInterface} from "@/interfaces.ts";
import axios from "axios";

const artists = ref<Array<ArtistInterface>>([]);
const editingArtistId = ref<number | null>(null);
const editedName = ref<string>("");


onMounted(() => {
  fetchArtists();
})

function fetchArtists() {
  axios.get("http://localhost:8080/api/artists").then((res) => {
    artists.value = res.data;
  }).catch((err) => {
    console.log(err);
  })
}

function startEditing(artist: ArtistInterface) {
  editingArtistId.value = artist.id;
  editedName.value = artist.name;
}

function saveEdit(artist: ArtistInterface) {
  axios
      .put(`http://localhost:8080/api/artists/${artist.id}`, {
        id: artist.id,
        name: editedName.value,
      })
      .then((res) => {
        artist.name = res.data.name;
        editingArtistId.value = null;
        editedName.value = "";
      })
      .catch((err) => {
        console.error(err);
      });
}
function cancelEdit() {
  editingArtistId.value = null;
  editedName.value = "";
}

function deleteArtist(artist: ArtistInterface) {
  !confirm("Willst du den Artist frfr Löschen?")
  axios.delete(`http://localhost:8080/api/artists/${artist.id}`).then((res) => {
    fetchArtists();

  })
}

</script>


<template>
  <RouterLink to="/">Zurück</RouterLink>
  <RouterLink to="/addArtist">Neue Künstler*in hinzufügen</RouterLink>

  <div v-if="artists" v-for="artist in artists" :key="artist.id" class="artist-card">
    <div v-if="editingArtistId === artist.id">
      <input v-model="editedName" placeholder="Neuer Name" />
      <button @click="saveEdit(artist)">💾 Speichern</button>
      <button @click="deleteArtist(artist)">🗑️ Löschen</button>
      <button @click="cancelEdit">❌ Abbrechen</button>
    </div>

    <div v-else>
      <h1>{{ artist.name }}</h1>
      <h2>ID: {{ artist.id }}</h2>
      <button @click="startEditing(artist)">✏️ Bearbeiten</button>
    </div>
  </div>
</template>

<style scoped>
/* 🔮 Artist Card Design */
.artist-card {
  background: linear-gradient(135deg, #ff00ff, #00ffff, #ffff00);
  color: #111;
  padding: 1.5rem;
  margin: 1.5rem auto;
  border-radius: 2rem;
  box-shadow: 0 0 20px rgba(255, 0, 255, 0.6),
  0 0 30px rgba(0, 255, 255, 0.6);
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  animation: wiggle 2s infinite ease-in-out;
  max-width: 650px;
  width: 90%;
  overflow: hidden; /* 👈 verhindert Schatten-/Animation-Überlauf */
}

.artist-card:hover {
  transform: rotate(-2deg) scale(1.05);
  box-shadow: 0 0 30px rgba(255, 255, 0, 0.8),
  0 0 50px rgba(0, 255, 255, 0.8);
}

.artist-card h1 {
  font-size: 2rem;
  font-weight: bold;
  color: #ff0077;
  text-shadow: 2px 2px 0 #00ffea, -2px -2px 0 #ffff00;
  animation: bounce 1.5s infinite alternate;
  margin: 0.5rem 0;
}

.artist-card h2 {
  font-size: 1.3rem;
  margin: 0.5rem 0;
  color: #00ffcc;
  text-shadow: 1px 1px 0 #ff00ff;
  animation: spinny 3s linear infinite;
}

/* Verhindert, dass Body kurz scrollbar wird 👇 */
html, body {
  overflow-x: hidden;
  overflow-y: auto;
  height: 100%;
}

/* 🧩 sorgt dafür, dass kein animiertes Element Layout-Verschiebung verursacht */
* {
  backface-visibility: hidden;
  transform-style: preserve-3d;
  will-change: transform;
}

input {
  margin: 0.5rem 0;
  padding: 0.6rem 1rem;
  border-radius: 1rem;
  border: 3px solid transparent;
  background: linear-gradient(135deg, #111, #222) padding-box,
  linear-gradient(135deg, #00ffcc, #ff00ff, #ffff00) border-box;
  color: white;
  text-align: center;
  box-shadow: 0 0 10px rgba(255, 0, 255, 0.4),
  0 0 20px rgba(0, 255, 255, 0.4);
  animation: inputWobble 3s infinite ease-in-out;
  width: 80%;
}

input:focus {
  outline: none;
  transform: scale(1.05);
  box-shadow: 0 0 20px rgba(255, 0, 0, 0.8),
  0 0 30px rgba(0, 255, 255, 0.8);
}

/* 🪩 Buttons */
button {
  margin-top: 1rem;
  padding: 0.7rem 1.2rem;
  border: none;
  border-radius: 1.2rem;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  background: linear-gradient(135deg, #ff0000, #ff8800);
  color: white;
  text-transform: uppercase;
  box-shadow: 0 0 10px rgba(255, 0, 0, 0.6),
  0 0 20px rgba(255, 136, 0, 0.6);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  animation: btnPulse 1.5s infinite;
}

button:hover {
  transform: scale(1.1) rotate(-3deg);
  box-shadow: 0 0 15px rgba(255, 0, 0, 0.9),
  0 0 30px rgba(255, 136, 0, 0.9);
}

button:active {
  transform: scale(0.95);
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

/* 💫 Animationen */
@keyframes bounce {
  from { transform: translateY(0); }
  to { transform: translateY(-10px); }
}

@keyframes spinny {
  0% { transform: rotate(0); }
  100% { transform: rotate(360deg); }
}

@keyframes wiggle {
  0%, 100% { transform: rotate(-1deg); }
  50% { transform: rotate(1deg); }
}

@keyframes btnPulse {
  0%, 100% { transform: scale(1); filter: hue-rotate(0deg); }
  50% { transform: scale(1.1); filter: hue-rotate(45deg); }
}

@keyframes linkBounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}

@keyframes inputWobble {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(1.5deg); }
}
</style>
