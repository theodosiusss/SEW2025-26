<script setup lang="ts">
import {onMounted, ref, useTemplateRef} from "vue";
import axios from "axios";
import type {SongInterface} from "@/interfaces.ts";
import router from "@/router";

const props = defineProps<{ id?: String}>();
const changeButton = useTemplateRef('changeButton');


const isError = ref(false);
const isSuccessful = ref(false);

function resetMessages(): void {
  isError.value = false;
  isSuccessful.value = false;
}


const name = ref("");
const artist = ref("");
const genre = ref("");
const length = ref("");

const song = ref<SongInterface | undefined>(undefined);

onMounted(() => {
  if(props.id) {
    axios.get(`http://localhost:8080/api/songs/${props.id}`).then((res) => {
      song.value = res.data;
      console.log(res.data);
      console.log(song.value);

      if(song.value) {
        name.value= song.value.title;
        artist.value= song.value.artist;
        genre.value= song.value.genre;
        length.value= song.value.length;
      }
    }).catch((err) => {
      if(changeButton.value) {
        changeButton.value.style.display ="none";
      }
      alert(err);
      setTimeout(()=> {
        router.push("/")

      },500)
    })

  }
})


function handleSubmit() {
  if (!name.value || !artist.value || !genre.value || !length.value) {
    isError.value = true;
    isSuccessful.value = false;
    return;
  }


if(song.value){
  axios.put("http://localhost:8080/api/songs/"+song.value.id, {
    "title": name.value,
    "artist": artist.value,
    "genre": genre.value,
    "length": length.value,
  }, {headers: {"Content-Type": "application/json"}}).then((res) => {
    if (res.status === 200) {
      console.log(res.data);
      isSuccessful.value = true;
      if(changeButton.value) {
        changeButton.value.style.display ="none";
      }
     setTimeout(() => {
       router.push('/');
     },500)

    } else {
      isError.value = true;
    }
  });

} else {
  axios.post("http://localhost:8080/api/songs", {
    "title": name.value,
    "artist": artist.value,
    "genre": genre.value,
    "length": length.value,
  }, {headers: {"Content-Type": "application/json"}}).then((res) => {


    if (res.status === 200) {
      console.log(res.data);
      isSuccessful.value = true;
      name.value = "";
      genre.value = "";
      length.value = "";
      artist.value = "";

    } else {
      isError.value = true;
    }
  });
}

}

</script>

<template>
  <!-- Erfolgsmeldung -->
  <p v-if="isSuccessful" class="success-msg">✅ Song erfolgreich gespeichert!</p>

  <!-- Fehlermeldung -->
  <p v-if="isError" class="error-msg">❌ Fehler beim Speichern des Songs!</p>

  <div class="page">
    <div class="form-container">
      <h1 class="title">🎶 Neuen Song hinzufügen</h1>
      <form class="song-form" @submit.prevent>
        <label>
          Titel
          <input required type="text" v-model="name" @focus="resetMessages" placeholder="z. B. Bohemian Rhapsody"/>
        </label>

        <label>
          Künstler
          <input required type="text" v-model="artist" @focus="resetMessages" placeholder="z. B. Queen"/>
        </label>

        <label>
          Genre
          <input required type="text" v-model="genre" @focus="resetMessages" placeholder="z. B. Rock"/>
        </label>

        <label>
          Länge
          <input required type="text" v-model="length" @focus="resetMessages" placeholder="z. B. 5:55"/>
        </label>

        <button v-if="!props.id" type="button" @click="handleSubmit">➕ Song speichern</button>
        <button v-else ref="changeButton" type="button" @click="handleSubmit">➕ Änderungen speichern</button>
      </form>
    </div>
    <RouterLink class="link" to="/">Zurück</RouterLink>

  </div>
</template>

<style scoped>
.link{
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
