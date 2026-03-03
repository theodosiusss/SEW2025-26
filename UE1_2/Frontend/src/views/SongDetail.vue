<script setup lang="ts">
import type { SongInterface } from "@/interfaces.ts";
import { onMounted, ref } from "vue";
import axios from "axios";
import router from "@/router";

const props = defineProps<{ id: string }>();
const song = ref<SongInterface | null>(null);

onMounted(async () => {
  const response = await axios.get(
      "http://localhost:8080/api/songs/" + props.id
  );
  song.value = response.data;
});
</script>

<template>
  <div v-if="song" class="detail-page">
    <!-- Titel -->
    <h1 class="song-title">{{ song.title }}</h1>

    <!-- Artist -->
    <h2 class="artist-name">🎤 {{ song.artist?.name }}</h2>
    <h3 class="artist-name">Owner:  {{ song.user?.username }}</h3>

    <!-- Meta Infos -->
    <div class="meta-chaos">
      <span>⏱ {{ song.length }}</span>
      <span>🎸 {{ song.genre }}</span>
    </div>

    <!-- Audio Player -->
    <div class="audio-chaos">
      <audio
          controls
          :src="`http://localhost:8080/api/songs/${song.id}/audio`"
      ></audio>
    </div>
    <div class="equalizer">
      <span></span><span></span><span></span>
      <span></span><span></span><span></span>
    </div>


    <!-- Zurück Button -->
    <button class="back-btn" @click="router.back()">
      ⬅ BACK TO THE VOID
    </button>
  </div>
</template>

<style scoped>
/* 🌈 PAGE */
.detail-page {

  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;

  animation: bgSpin 12s linear infinite;
}

/* 🎶 TITLE */
.song-title {
  font-size: 3rem;
  font-weight: 900;
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

  animation: titleGlow 3s infinite linear, titleBounce 1.5s infinite;
}

/* 🎤 ARTIST */
.artist-name {
  font-size: 2rem;
  color: #00ffcc;
  text-shadow: 0 0 15px #ff00ff;
  animation: artistSpin 6s linear infinite;
}

/* 🧨 META INFO */
.meta-chaos {
  display: flex;
  gap: 1.5rem;
}

.meta-chaos span {
  padding: 0.6rem 1.4rem;
  border-radius: 999px;
  font-weight: bold;
  background: #ff00ff;
  color: #fff;
  transform: rotate(-2deg);
  animation: metaPulse 1.2s infinite;
}

/* 🎧 AUDIO */
.audio-chaos {
  position: relative;
  padding: 2rem 2.5rem;
  border-radius: 3rem;

  background:
      linear-gradient(135deg, #111, #222) padding-box,
      conic-gradient(
          from 0deg,
          #ff00ff,
          #00ffff,
          #ffff00,
          #ff00ff
      ) border-box;

  border: 6px solid transparent;

  box-shadow:
      0 0 25px rgba(255, 0, 255, 0.8),
      0 0 40px rgba(0, 255, 255, 0.8),
      inset 0 0 15px rgba(255, 255, 255, 0.1);

  animation:
      audioFloat 2.5s ease-in-out infinite,
      raveSpin 6s linear infinite;
}
@keyframes raveSpin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }

}
.audio-chaos:hover {
  position: relative;
  padding: 2rem 2.5rem;
  border-radius: 3rem;

  background:
      linear-gradient(135deg, #111, #222) padding-box,
      conic-gradient(
          from 0deg,
          #ff00ff,
          #00ffff,
          #ffff00,
          #ff00ff
      ) border-box;

  border: 6px solid transparent;

  box-shadow:
      0 0 25px rgba(255, 0, 255, 0.8),
      0 0 40px rgba(0, 255, 255, 0.8),
      inset 0 0 15px rgba(255, 255, 255, 0.1);

  animation:
      audioFloat 2.5s ease-in-out infinite,
      raveSpin 20s linear infinite;
}

audio {
  width: 320px;
  filter:
      hue-rotate(180deg)
      contrast(1.4)
      saturate(2);

  border-radius: 1.5rem;
  animation: audioWobble 1.5s infinite ease-in-out;
}
@keyframes audioWobble {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(1.5deg); }
}


/* ⬅ BACK BUTTON */
.back-btn {
  margin-top: 2rem;
  padding: 1rem 2.5rem;
  font-size: 1.4rem;
  font-weight: 900;
  border-radius: 2rem;
  border: none;
  cursor: pointer;

  background: linear-gradient(
      135deg,
      #ff0000,
      #ffff00,
      #00ffff
  );

  color: #111;
  text-transform: uppercase;
  box-shadow:
      0 0 15px rgba(255, 255, 0, 0.8),
      0 0 25px rgba(0, 255, 255, 0.8);

  animation: btnChaos 1.8s infinite;
}

.back-btn:hover {
  transform: scale(1.2) rotate(-6deg);
}

/* 🔮 ANIMATIONS */
@keyframes titleGlow {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

@keyframes titleBounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-12px); }
}

@keyframes artistSpin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes metaPulse {
  0%, 100% { transform: scale(1) rotate(-2deg); }
  50% { transform: scale(1.2) rotate(2deg); }
}

@keyframes audioFloat {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

@keyframes btnChaos {
  0%, 100% { transform: rotate(0deg); }
  25% { transform: rotate(-3deg); }
  75% { transform: rotate(3deg); }
}

@keyframes bgSpin {
  from { filter: hue-rotate(0deg); }
  to { filter: hue-rotate(360deg); }
}
.equalizer {
  display: flex;
  gap: 6px;
  justify-content: center;
  margin-top: 1rem;
}

.equalizer span {
  width: 8px;
  height: 20px;
  background: linear-gradient(180deg, #ff00ff, #00ffff);
  border-radius: 4px;
  animation: equalize 1.2s infinite ease-in-out;
}

.equalizer span:nth-child(1) { animation-delay: 0s; }
.equalizer span:nth-child(2) { animation-delay: 0.1s; }
.equalizer span:nth-child(3) { animation-delay: 0.2s; }
.equalizer span:nth-child(4) { animation-delay: 0.3s; }
.equalizer span:nth-child(5) { animation-delay: 0.4s; }
.equalizer span:nth-child(6) { animation-delay: 0.5s; }

@keyframes equalize {
  0%, 100% { height: 10px; opacity: 0.6; }
  50% { height: 40px; opacity: 1; }
}

</style>
