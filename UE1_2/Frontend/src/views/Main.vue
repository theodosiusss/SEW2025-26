<script setup lang="ts">
import axios from 'axios';
import {onMounted, ref, watch} from "vue";
import Song from "@/components/Song.vue";

const songs = ref({});
const search = ref("");
const toggleError = ref(false);

onMounted(async () => {
  fetchData();
})

let timeoutId = 0;

watch(search, (newValue, odlValue) => {
  clearTimeout(timeoutId)
  if (newValue != odlValue) {
    if (newValue.length != 0) {
      timeoutId = setTimeout(() => {
            axios.get(`http://localhost:8080/api/songs/search/${search.value}`)
                .then((res) => {
                  songs.value = res.data
                  if (res.data.length === 0) {
                    setTimeout(() => {
                      toggleError.value = true;
                    }, 900); // entspricht deiner CSS-Transitiondauer
                  } else {
                    toggleError.value = false;
                  }                })
          }
          , 300)
    }else {
      timeoutId = setTimeout(fetchData, 500)
    }
  }


})


function fetchData() {
  toggleError.value = false;
  axios.get("http://localhost:8080/api/songs").then(response => {
    songs.value = response.data;
  });
}

</script>

<template>
  <RouterLink to="/add">Neue Songs Hinzufügen</RouterLink>

  <div>
    <label for="search">Songs Suchen</label>
    <input v-model="search" name="search" placeholder="Buscar"/>
    <p class="error-msg" v-if="toggleError">Es konnte kein Song gefunden werden, bitte passen Sie Ihre Suche an</p>

  </div>

  <div v-if="songs" class="songs">
    <TransitionGroup name="song-fade" tag="div" class="songs-group">
      <div v-for="song in songs" :key="song.id" class="song-wrapper">
        <Song :song="song" @deleted="fetchData" />
      </div>
    </TransitionGroup>
  </div>
</template>

<style scoped>
.songs {
  display: flex;
  flex-wrap: wrap;
}
/* Container für TransitionGroup */
.songs-group {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: center;
}

/* Sanfte Neon-Fade-Transition */
.song-fade-enter-active,
.song-fade-leave-active {
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.song-fade-enter-from,
.song-fade-leave-to {
  opacity: 0;
  transform: scale(0.9) rotate(-2deg);
  filter: blur(4px);
}

.song-fade-move {
  transition: transform 0.5s ease;
}

/* Optional: sanfter Glow beim Einblenden */
.song-wrapper {
  animation: songPop 0.8s ease both;
}

@keyframes songPop {
  0% {
    opacity: 0;
    transform: scale(0.8) rotate(-5deg);
    filter: brightness(0.6);
  }
  100% {
    opacity: 1;
    transform: scale(1) rotate(0deg);
    filter: brightness(1);
  }
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
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-6px);
  }
}
/* 🌀 Goofy Input Field */
input[name="search"] {
  display: block;
  margin: 1rem auto 2rem auto;
  padding: 0.8rem 1.2rem;
  width: 60%;
  max-width: 400px;
  font-size: 1rem;
  border: 3px solid transparent;
  border-radius: 1.5rem;
  background: linear-gradient(135deg, #111, #222) padding-box,
  linear-gradient(135deg, #00ffcc, #ff00ff, #ffff00) border-box;
  color: #fff;
  box-shadow: 0 0 10px rgba(255, 0, 255, 0.4),
  0 0 20px rgba(0, 255, 255, 0.4);
  text-align: center;
  transition: all 0.3s ease;
  animation: inputWobble 3s infinite ease-in-out;
}

input[name="search"]::placeholder {
  color: rgba(255, 255, 255, 0.6);
  font-style: italic;
}

input[name="search"]:hover {
  transform: scale(1.05) rotate(1deg);
  box-shadow: 0 0 20px rgba(255, 255, 0, 0.8),
  0 0 30px rgba(0, 255, 255, 0.8);
}

input[name="search"]:focus {
  outline: none;
  transform: scale(1.08);
  box-shadow: 0 0 25px rgba(255, 255, 0, 1),
  0 0 40px rgba(255, 0, 255, 0.9);
}

/* Label passend dazu */
label {
  display: block;
  text-align: center;
  font-weight: bold;
  text-transform: uppercase;
  color: #fff;
  text-shadow: 0 0 8px #00ffff, 0 0 12px #ff00ff;
  margin-top: 1rem;
  letter-spacing: 1px;
  font-size: 1.1rem;
}

/* Animationen */
@keyframes linkBounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-6px);
  }
}

@keyframes inputWobble {
  0%, 100% {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(1.5deg);
  }
}

/* 🌀 Goofy Error Message */
.error-msg {
  text-align: center;
  margin-top: -1rem;
  margin-bottom: 2rem;
  font-size: 1.1rem;
  font-weight: bold;
  text-transform: uppercase;
  background: linear-gradient(90deg, #ff00ff, #ffff00, #00ffff, #ff00ff);
  background-size: 300% 300%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: goofyGlow 3s infinite linear, goofyShake 1.5s infinite ease-in-out;
  text-shadow: 0 0 10px rgba(255, 255, 0, 0.8),
  0 0 20px rgba(0, 255, 255, 0.6);
  letter-spacing: 1px;
  filter: drop-shadow(0 0 4px rgba(255, 0, 255, 0.6));
}

@keyframes goofyGlow {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

@keyframes goofyShake {
  0%, 100% {
    transform: translateX(0) rotate(0deg);
  }
  25% {
    transform: translateX(-3px) rotate(-1deg);
  }
  50% {
    transform: translateX(3px) rotate(1deg);
  }
  75% {
    transform: translateX(-2px) rotate(0.5deg);
  }
}
</style>

