<script setup lang="ts">

defineProps({
  song : {}
})
</script>

<template>
  <div class="song">
    <h1>{{ song.title }}</h1>
    <h2>{{ song.artist }}</h2>
    <div>
      <p>{{song.length}}</p>
      <p>{{song.genre}}</p>
    </div>

  </div>
</template>

<style scoped>
.song {
  background: linear-gradient(135deg, #ff00ff, #00ffff, #ffff00);
  color: #111;
  padding: 1.5rem;
  margin: 1rem 0;
  border-radius: 2rem;
  box-shadow: 0 0 20px rgba(255, 0, 255, 0.6),
  0 0 30px rgba(0, 255, 255, 0.6);
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  animation: wiggle 2s infinite ease-in-out;
}

.song:hover {
  transform: rotate(-2deg) scale(1.05);
  box-shadow: 0 0 30px rgba(255, 255, 0, 0.8),
  0 0 50px rgba(0, 255, 255, 0.8);
}

.song h1 {
  font-size: 2rem;
  font-weight: bold;
  color: #ff0077;
  text-shadow: 2px 2px 0 #00ffea, -2px -2px 0 #ffff00;
  animation: bounce 1.5s infinite alternate;
}

.song h2 {
  font-size: 1.5rem;
  margin: 0.5rem 0;
  color: #00ffcc;
  text-shadow: 1px 1px 0 #ff00ff;
  animation: spinny 3s linear infinite;
}

.song p {
  font-size: 1rem;
  font-weight: bold;
  margin: 0.25rem;
  background: #000;
  color: #fff;
  padding: 0.3rem 0.8rem;
  border-radius: 999px;
  display: inline-block;
  transform: rotate(-2deg);
  animation: pulse 1.2s infinite;
}

/* Animationen */
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

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.2); }
}
</style>