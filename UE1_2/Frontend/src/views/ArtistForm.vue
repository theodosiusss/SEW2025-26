<script setup lang="ts">
import {reactive, ref} from "vue";
import axios from "axios";
import Form from "@/views/Form.vue";
import {alphaNum, required} from "@vuelidate/validators";
import useVuelidate from "@vuelidate/core";

const state = reactive({
  name: '',
})

const rules = {
  name: {required},
}

const v$ = useVuelidate(rules, state);


const isError = ref(false);
const isSuccessful = ref(false);

function resetMessages(): void {
  isError.value = false;
  isSuccessful.value = false;
  v$.value.$reset();
}

function handleSubmit() {
  v$.value.$validate();
  if (v$.value.$error) {
    isError.value = true;
    isSuccessful.value = false;
    return;
  }
  axios.post("http://localhost:8080/api/artists", {
    "name": state.name,
  }, {headers: {"Content-Type": "application/json"}}).then((res) => {

    if (res.status === 200) {
      console.log(res.data);
      isSuccessful.value = true;
      v$.value.$reset();
      state.name = "";
    } else {
      isError.value = true;
    }
  });

}
</script>

<template>
  <!-- Erfolgsmeldung -->
  <p v-if="isSuccessful" class="success-msg">✅ Künstler erfolgreich gespeichert!</p>

  <!-- Fehlermeldung -->
  <p v-if="isError" class="error-msg">❌ Fehler beim Speichern des Künstlers!</p>

  <div class="page">
    <div class="form-container">
      <h1 class="title">🎶 Neuen Künster hinzufügen</h1>
      <form class="song-form" @submit.prevent>
        <label>Name</label>
        <input v-model="state.name" @focus="resetMessages" @blur="v$.name.$touch"/>
        <p class="error-text-wrapper">
          <span v-if="v$.name.$error" class="error-text">Fehler beim Namen</span>
        </p>
        <button :disabled="v$.$invalid" type="button" @click="handleSubmit">➕ Künstler speichern</button>
      </form>
    </div>
    <RouterLink class="link" to="/artists">Zurück</RouterLink>

  </div>
</template>


<style scoped>
.error-text-wrapper {
  min-height: 22px; /* genug Platz für 1 Zeile Fehlertext */
}

.error-text {
  color: #d9534f;
  font-size: 0.9rem;
  margin: 0;
}

.link {
  background-color: white;
  padding: 10px 40px 10px 40px;
  border-radius: 5px;
  font-size: 20px;
  color: black;
}

.page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

}

.success-msg {
  background: #d4edda;
  color: #155724;
  padding: 0.8rem;
  border-radius: 0.5rem;
  margin-bottom: 1rem;
  text-align: center;
  font-weight: 600;
}

.error-msg {
  background: #f8d7da;
  color: #721c24;
  padding: 0.8rem;
  border-radius: 0.5rem;
  margin-bottom: 1rem;
  text-align: center;
  font-weight: 600;
}

.form-container {
  min-width: 500px;
  max-width: 500px;
  margin: 2rem auto;
  padding: 2rem;
  border-radius: 1rem;
  background: #f8f9fa;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.title {
  text-align: center;
  margin-bottom: 1.5rem;
  font-size: 1.8rem;
  color: #333;
}

.song-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

label {
  display: flex;
  flex-direction: column;
  font-weight: 600;
  color: #444;
}

input {
  padding: 0.6rem 0.8rem;
  border-radius: 0.5rem;
  border: 2px solid #ccc;
  outline: none;
  font-size: 1rem;
  transition: border 0.2s, box-shadow 0.2s;
}

input:focus {
  border-color: #ff0077;
  box-shadow: 0 0 8px rgba(255, 0, 119, 0.4);
}

button {
  margin-top: 1rem;
  padding: 0.8rem;
  background: linear-gradient(135deg, #ff00ff, #00ffff);
  border: none;
  border-radius: 0.5rem;
  font-size: 1.1rem;
  font-weight: bold;
  color: white;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

button:hover {
  transform: translateY(-2px) scale(1.05);
  box-shadow: 0 6px 15px rgba(0, 255, 255, 0.5);
}
</style>
