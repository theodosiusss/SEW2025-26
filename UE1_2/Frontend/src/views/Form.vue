<script setup lang="ts">
import {onMounted, reactive, ref, useTemplateRef} from "vue";
import axios from "axios";
import type {ArtistInterface, SongInterface} from "@/interfaces.ts";
import router from "@/router";
import {required} from "@vuelidate/validators";
import useVuelidate from "@vuelidate/core";

const props = defineProps<{ id?: String }>();
const changeButton = useTemplateRef('changeButton');

const GenreList = ["Pop", "Rock", "Metal", "Country", "Schlager", "Emo", "Brainrot", "Hyperpop", "Rap"]

const isError = ref(false);
const isSuccessful = ref(false);



const artists = ref<Array<ArtistInterface>>([]);
const song = ref<SongInterface | undefined>(undefined);
const etag = ref<string | null>(null);

const matchLength = (value: string) =>
    /^[0-9]{1,2}:[0-5][0-9]$/.test(value);

const state = reactive({
  name: '',
  artist: null as number | null,
  genres: [] as string[],
  length: '',
  file: null as File | null
})

const rules = {
  name: {required},
  artist: {required},
  genres: {required},
  length: {required,matchLength}
}
const v$ = useVuelidate(rules, state);
function resetMessages(): void {
  isError.value = false;
  isSuccessful.value = false;
}

onMounted(() => {

  axios.get("http://localhost:8080/api/artists").then((res) => {
    artists.value = res.data;
  })

  if (props.id) {
    axios.get(`http://localhost:8080/api/songs/${props.id}`).then((res) => {
      song.value = res.data;
      etag.value = res.headers['etag'];
      console.log(etag.value);

      console.log(res.headers);
      console.log(song.value);

      if (song.value) {
        state.name = song.value.title;
        state.artist = song.value.artist?.id;
        state.genres = song.value.genres;
        state.length = song.value.length;
      }
    }).catch((err) => {
      if (changeButton.value) {
        changeButton.value.style.display = "none";
      }
      alert(err);
      setTimeout(() => {
        router.push("/")

      }, 500)
    })

  }
})


async function handleSubmit() {
  const isValid = await v$.value.$validate();

  if (!isValid) {
    isError.value = true;
    isSuccessful.value = false;
    return;
  }
  if (!song.value && !state.file) {
    isError.value = true;
    alert("Bitte eine Musikdatei auswählen");
    return;
  }

  const formData = new FormData();

  formData.append(
      "song",
      new Blob(
          [JSON.stringify({
            title: state.name,
            genres: state.genres,
            length: state.length,
            artist: { id: state.artist }
          })],
          { type: "application/json" }
      )
  );
  if (state.file) {
    formData.append("file", state.file);
  }


  if (song.value) {
    axios.put("http://localhost:8080/api/songs/" + song.value.id, formData,
        { headers: { "Content-Type": "multipart/form-data", "If-Match": etag.value } })
        .then((res) => {
      if (res.status === 200) {
        console.log(res.data);
        isSuccessful.value = true;
        if (changeButton.value) {
          changeButton.value.style.display = "none";
        }
        setTimeout(() => {
          router.push('/');
        }, 500)

      }
      else if(res.status === 412){
        alert("⚠️ Der Song wurde inzwischen geändert. Bitte neu laden.");
        setTimeout(() => {
          window.location.reload();
        }, 200)
      }
      else {
        isError.value = true;
        alert(res.data);
      }
    }).catch(err => {
      if(err.response.status == 412){
        alert("⚠️ Der Song wurde inzwischen geändert. Bitte neu laden.");
        setTimeout(() => {
          window.location.reload();
        }, 200)
      }
    });

  } else {
    axios.post("http://localhost:8080/api/songs", formData,
        { headers: { "Content-Type": "multipart/form-data" } })
        .then((res) => {


      if (res.status === 200) {
        console.log(res.data);
        isSuccessful.value = true;
        state.name = "";
        state.genres = [];
        state.length = "";
        state.artist = null;
        v$.value.$reset();

      } else {
        isError.value = true;
      }
    }).catch((err) => {
      alert(err);
      isError.value = true;
    });
  }

}

function onFileChange(event: Event) {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files.length > 0) {
    state.file = input.files[0];
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

        <!-- Titel -->
        <label>
          Titel
          <input
              required
              type="text"
              v-model="state.name"
              @focus="resetMessages"
              @blur="v$.name.$touch"
              placeholder="z. B. Bohemian Rhapsody"
          />
          <p v-if="v$.name.$error" class="validation-error">
            Titel ist erforderlich.
          </p>
        </label>

        <!-- Künstler -->
        <label>
          Künstler
          <select
              required
              v-model="state.artist"
              @focus="resetMessages"
              @blur="v$.artist.$touch"
              name="artist"
              id="artist"
          >
            <option disabled :value="null">-- Bitte Künstler wählen --</option>
            <option
                v-for="a in artists"
                :key="a.id"
                :value="a.id"
            >
              {{ a.name }}
            </option>
          </select>

          <p v-if="v$.artist.$error" class="validation-error">
            Bitte wähle einen Künstler aus.
          </p>
        </label>

        <!-- Genre -->
        <label>
          Genre
          <select
              required
              multiple
              v-model="state.genres"
              @focus="resetMessages"
              @blur="v$.genres.$touch"
          >
            <option disabled :value="null">-- Bitte Genre</option>/
            <option v-for="genre in GenreList" :value="genre" :key="genre"> {{genre}} </option>
          </select>

          <p v-if="v$.genres.$error" class="validation-error">
            Genre ist erforderlich.
          </p>
        </label>

        <!-- Länge -->
        <label>
          Länge
          <input
              required
              type="text"
              v-model="state.length"
              @focus="resetMessages"
              @blur="v$.length.$touch"
              placeholder="z. B. 5:55"
          />

          <!-- 2 mögliche Fehler: required + matchLength -->
          <p v-if="v$.length.$error" class="validation-error">
            <span v-if="v$.length.required?.$invalid">
              Länge ist erforderlich.
            </span>
            <span v-else-if="v$.length.matchLength?.$invalid">
              Länge muss im Format mm:ss sein.
            </span>
          </p>
        </label>
        <!-- Musikdatei -->
        <label>
          Musikdatei (mp3)
          <input
              type="file"
              accept="audio/*"
              @change="onFileChange"

          />

        </label>
        <p v-if="song">
          🎵 Aktuelle Datei bleibt erhalten, wenn keine neue ausgewählt wird
        </p>



        <!-- Buttons -->
        <button
            v-if="!props.id"
            :disabled="v$.$invalid"
            type="button"
            @click="handleSubmit"
        >
          ➕ Song speichern
        </button>

        <button
            v-else
            ref="changeButton"
            :disabled="v$.$invalid"
            type="button"
            @click="handleSubmit"
        >
          ➕ Änderungen speichern
        </button>
      </form>
    </div>

    <RouterLink class="link" to="/">Zurück</RouterLink>
  </div>
</template>

<style scoped>
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
.validation-error {
  color: #d9534f;
  font-size: 0.9rem;
  margin-top: 4px;
}

</style>
