<script setup lang="ts">
import {useUserStore} from "@/store/userStore.ts";
import {onMounted} from "vue";

const auth = useUserStore();

onMounted(async () => {
  await auth.checkLogin(false)
})

</script>

<template>
  <header class="goofy-header">
    <div class="nav-left">
      <RouterLink
          v-if="$route.name != 'login' && !auth.isLoggedIn"
          to="/login"
          class="nav-link"
      >
        Login
      </RouterLink>

      <RouterLink
          v-else-if="$route.name === 'login'"
          to="/"
          class="nav-link"
      >
        Home
      </RouterLink>
    </div>

    <div class="nav-center">
      <span class="username" v-if="auth.isLoggedIn">
        👾 {{ auth.userName }}
      </span>
    </div>

    <div class="nav-right">
      <button
          v-if="auth.isLoggedIn"
          class="logout-btn"
          @click="auth.logout()"
      >
        Logout
      </button>
    </div>
  </header>

  <RouterView/>
</template>

<style scoped>

.goofy-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.2rem 2rem;
  margin-bottom: 2rem;

  background: linear-gradient(
      90deg,
      #ff00ff,
      #00ffff,
      #ffff00,
      #ff00ff
  );
  background-size: 300% 300%;
  animation: headerFlow 6s linear infinite;

  border-radius: 2rem;
  box-shadow:
      0 0 25px rgba(255, 0, 255, 0.7),
      0 0 40px rgba(0, 255, 255, 0.6);

  position: sticky;
  top: 1rem;
  z-index: 100;
}

.nav-link {
  padding: 0.7rem 1.5rem;
  border-radius: 2rem;
  font-weight: bold;
  text-transform: uppercase;
  text-decoration: none;

  background: #111;
  color: #fff;

  box-shadow:
      0 0 15px rgba(255, 255, 0, 0.7),
      0 0 25px rgba(255, 0, 255, 0.6);

  transition: all 0.3s ease;
  animation: navBounce 2s infinite ease-in-out;
}

.nav-link:hover {
  transform: scale(1.2) rotate(-5deg);
  box-shadow:
      0 0 25px rgba(255, 255, 0, 1),
      0 0 40px rgba(0, 255, 255, 1);
}

/* Username Neon */
.username {
  font-size: 1.3rem;
  font-weight: bold;

  background: linear-gradient(
      90deg,
      #ffff00,
      #ff00ff,
      #00ffff,
      #ffff00
  );
  background-size: 300% 300%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;

  animation: usernameGlow 3s linear infinite;
  text-shadow:
      0 0 10px rgba(255, 255, 0, 0.8),
      0 0 20px rgba(0, 255, 255, 0.6);
}

/* Logout Button Chaos */
.logout-btn {
  padding: 0.6rem 1.4rem;
  border-radius: 1.5rem;
  border: none;
  font-weight: bold;
  text-transform: uppercase;
  cursor: pointer;

  background: linear-gradient(135deg, #ff0000, #ff8800);
  color: white;

  box-shadow:
      0 0 15px rgba(255, 0, 0, 0.7),
      0 0 30px rgba(255, 136, 0, 0.7);

  animation: logoutPulse 1.5s infinite;
  transition: all 0.2s ease;
}

.logout-btn:hover {
  transform: rotate(-8deg) scale(1.2);
  box-shadow:
      0 0 25px rgba(255, 0, 0, 1),
      0 0 40px rgba(255, 255, 0, 1);
}

.logout-btn:active {
  transform: scale(0.9);
}

/* 🔥 Animationen */
@keyframes headerFlow {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

@keyframes navBounce {
  0%,100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}

@keyframes usernameGlow {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

@keyframes logoutPulse {
  0%,100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}
</style>
