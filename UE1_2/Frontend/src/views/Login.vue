<script setup lang="ts">
import {ref} from "vue";
import {useUserStore} from "@/store/userStore.ts";

const username = ref("");
const password = ref("");
const userStore = useUserStore();
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <h1 class="title">🚀 Welcome Back, Superstar!</h1>

      <form class="login-form" @submit.prevent="userStore.login(username, password)">
        <input
            type="text"
            v-model="username"
            placeholder="👤 Username"
        />

        <input
            type="password"
            v-model="password"
            placeholder="🔒 Password"
        />

        <button type="submit">
          💥 LET ME IN
        </button>
      </form>
    </div>
  </div>
</template>


<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: radial-gradient(circle at top left, #111, #000);
  overflow: hidden;
}

/* 🌀 Floating Neon Card */
.login-card {
  width: 380px;
  padding: 2.5rem;
  border-radius: 2rem;
  background: linear-gradient(135deg, #111, #222);
  box-shadow:
      0 0 25px rgba(255, 0, 255, 0.6),
      0 0 50px rgba(0, 255, 255, 0.4);
  animation: floatCard 4s ease-in-out infinite;
}

/* 🌈 Titel mit Glow */
.title {
  text-align: center;
  font-size: 1.8rem;
  margin-bottom: 2rem;
  text-transform: uppercase;
  letter-spacing: 2px;

  background: linear-gradient(90deg, #ff00ff, #ffff00, #00ffff, #ff00ff);
  background-size: 300% 300%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;

  animation: rainbowFlow 4s linear infinite;
}

/* 📦 Form Layout */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

/* 🌀 Inputs */
.login-form input {
  padding: 0.9rem;
  border-radius: 1.5rem;
  border: 3px solid transparent;

  background:
      linear-gradient(#111, #111) padding-box,
      linear-gradient(135deg, #ff00ff, #00ffff, #ffff00) border-box;

  color: white;
  font-size: 1rem;
  text-align: center;

  box-shadow:
      0 0 10px rgba(255, 0, 255, 0.5),
      0 0 15px rgba(0, 255, 255, 0.4);

  transition: all 0.3s ease;
  animation: inputWobble 3s infinite ease-in-out;
}

.login-form input::placeholder {
  color: rgba(255, 255, 255, 0.6);
  font-style: italic;
}

/* Hover Chaos */
.login-form input:hover {
  transform: scale(1.05) rotate(1deg);
}

/* Focus = Super Saiyan Mode */
.login-form input:focus {
  outline: none;
  transform: scale(1.08);
  box-shadow:
      0 0 20px rgba(255, 255, 0, 0.9),
      0 0 40px rgba(255, 0, 255, 0.8);
}

/* 🚀 Button */
.login-form button {
  padding: 1rem;
  border-radius: 2rem;
  border: none;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;

  background: linear-gradient(135deg, #ff00ff, #00ffff, #ffff00);
  background-size: 200% 200%;

  color: #111;
  text-transform: uppercase;
  letter-spacing: 1px;

  box-shadow:
      0 0 15px rgba(255, 255, 0, 0.8),
      0 0 25px rgba(0, 255, 255, 0.6);

  transition: all 0.25s ease;
  animation: buttonPulse 2s infinite ease-in-out;
}

.login-form button:hover {
  transform: scale(1.15) rotate(-3deg);
  animation: rainbowFlow 1s linear infinite;
}

.login-form button:active {
  transform: scale(0.9) rotate(5deg);
}

/* ✨ Animations */
@keyframes floatCard {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes rainbowFlow {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

@keyframes inputWobble {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(1.2deg); }
}

@keyframes buttonPulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

</style>