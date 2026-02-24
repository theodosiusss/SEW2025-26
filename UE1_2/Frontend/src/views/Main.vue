<script setup lang="ts">
import axios from 'axios';
import {onMounted, ref, watch} from "vue";
import Song from "@/components/Song.vue";
import type {SongInterface} from "@/interfaces.ts";
import {useUserStore} from "@/store/userStore.ts";

const songs = ref<Array<SongInterface>>([]);
const search = ref("");
const toggleError = ref(false);

const auth = useUserStore();
const currentPage = ref(0);
const maxPage = ref(67);
const minPage = ref(0);
onMounted(async () => {
  fetchData();
})

let timeoutId = 0;

watch(search, (newValue, odlValue) => {
  clearTimeout(timeoutId)
  if (newValue != odlValue) {
    if (newValue.length != 0) {
      timeoutId = setTimeout(() => {
           fetchSearch(true);
          }
          , 300)
    }else {
      toggleError.value = false;
      currentPage.value = 0;
      timeoutId = setTimeout(fetchData, 500)
    }
  }


})

function fetchSearch(newSearch: boolean){
  if(newSearch) {
    currentPage.value = 0;
  }
  axios.get(`http://localhost:8080/api/songs/search/${search.value}?page=${currentPage.value}&size=5`)
      .then((res) => {
        songs.value = res.data.content
        if(res.data.totalPages !== 0){
          maxPage.value = res.data.totalPages-1;
        }else{
          maxPage.value = res.data.totalPages;
        }
        currentPage.value = res.data.pageable.pageNumber;
        toggleError.value = res.data.length === 0;                })

}
function fetchData() {
  toggleError.value = false;
  axios.get(`http://localhost:8080/api/songs?page=${currentPage.value}&size=5`).then(response => {
    songs.value = response.data.content;
    maxPage.value = response.data.totalPages-1;
    currentPage.value = response.data.pageable.pageNumber;
  });
}

watch(currentPage, () => {
  if (search.value.length === 0) {
    fetchData();
  }else{
    fetchSearch(false);
  }
});


</script>

<template>
  <RouterLink v-if="auth.isLoggedIn" to="/add">Neue Songs Hinzufügen</RouterLink>
  <RouterLink v-if="auth.isLoggedIn" to="/artists">Künstler*innen Übersicht</RouterLink>

  <div>
    <label for="search">Songs Suchen</label>
    <input v-model="search" name="search" placeholder="Buscar"/>
    <Transition name="song-fade" >
    <p class="error-msg" v-if="toggleError">Es konnte kein Song gefunden werden, bitte passen Sie Ihre Suche an</p>
    </Transition>
  </div>

  <div class="goofy-pagination">
    <button
        class="page-btn"
        :disabled="currentPage === minPage"
        @click="currentPage = minPage"
    >
      ⏮
    </button>

    <button
        class="page-btn"
        :disabled="currentPage === minPage"
        @click="currentPage--"
    >
      ◀
    </button>

    <span class="page-indicator">
    {{ currentPage + 1 }} / {{ maxPage + 1 }}
  </span>

    <button
        class="page-btn"
        :disabled="currentPage === maxPage"
        @click="currentPage++"
    >
      ▶
    </button>

    <button
        class="page-btn"
        :disabled="currentPage === maxPage"
        @click="currentPage = maxPage"
    >
      ⏭
    </button>
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
.song-fade-enter-active {
  transition: all 3s ease-in                                              ;
}

/* Ausblenden: schneller (0.4s) */
.song-fade-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
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
/* 🌀 Goofy Pagination Container */
.goofy-pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin: 2rem 0;
  animation: paginationFloat 3s infinite ease-in-out;
}

/* 🔮 Page Buttons */
.page-btn {
  padding: 0.6rem 1rem;
  font-size: 1.3rem;
  font-weight: bold;
  border-radius: 50%;
  border: 3px solid transparent;
  cursor: pointer;

  background:
      linear-gradient(135deg, #111, #222) padding-box,
      linear-gradient(135deg, #00ffff, #ff00ff, #ffff00) border-box;

  color: #fff;
  box-shadow:
      0 0 12px rgba(255, 0, 255, 0.6),
      0 0 20px rgba(0, 255, 255, 0.6);

  transition: all 0.25s ease;
}

/* Hover = Chaos */
.page-btn:hover:not(:disabled) {
  transform: scale(1.25) rotate(-8deg);
  box-shadow:
      0 0 18px rgba(255, 255, 0, 0.9),
      0 0 30px rgba(0, 255, 255, 0.9);
}

/* Click */
.page-btn:active:not(:disabled) {
  transform: scale(0.9) rotate(5deg);
}

/* Disabled = kaputt & traurig */
.page-btn:disabled {
  opacity: 0.35;
  cursor: not-allowed;
  filter: grayscale(1) blur(0.5px);
  box-shadow: none;
}

/* 📟 Page Indicator */
.page-indicator {
  font-size: 1.2rem;
  font-weight: bold;
  padding: 0.5rem 1.2rem;
  border-radius: 1.5rem;
  text-transform: uppercase;

  background: linear-gradient(
      90deg,
      #ff00ff,
      #ffff00,
      #00ffff,
      #ff00ff
  );
  background-size: 300% 300%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;

  animation: pageGlow 3s infinite linear;
  text-shadow:
      0 0 10px rgba(255, 255, 0, 0.8),
      0 0 20px rgba(0, 255, 255, 0.6);
}

/* ✨ Animations */
@keyframes paginationFloat {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-6px);
  }
}

@keyframes pageGlow {
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
/* 🌀 Permanente Rotation */
.page-btn {
  animation: slowSpin 6s linear infinite;
}

/* Unterschiedliche Richtungen für mehr Chaos */
.page-btn:nth-child(1),
.page-btn:nth-child(5) {
  animation-direction: normal;
}

.page-btn:nth-child(2),
.page-btn:nth-child(4) {
  animation-direction: reverse;
}

/* Hover = Turbo-Modus */
.page-btn:hover:not(:disabled) {
  animation: fastSpin 0.8s linear infinite;
}

/* Disabled = eingefroren */
.page-btn:disabled {
  animation-play-state: paused;
}

/* 🌀 Keyframes */
@keyframes slowSpin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes fastSpin {
  from {
    transform: rotate(0deg) scale(1.2);
  }
  to {
    transform: rotate(-360deg) scale(1.2);
  }
}

</style>

