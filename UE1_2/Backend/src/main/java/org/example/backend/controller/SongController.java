package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.model.Song;
import org.example.backend.repository.SongAudioProjection;
import org.example.backend.repository.SongRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/songs")
@CrossOrigin(origins = "*",
        exposedHeaders = "eTag"
)
public class SongController {

    private final SongRepository repository;

    public SongController(SongRepository songRepository) {
        this.repository = songRepository;
    }

    @GetMapping
    public Page<Song> getAllSongs(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @GetMapping("/search/{search}")
    public Page<Song> searchSong(@PathVariable String search, Pageable pageable) {
        return this.repository.search(search, search,search, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable long id) {

        Song song = repository.findById(id).orElseThrow();

        String etag = "\"song-" + song.getId() + "-v" + song.getVersion() + "\"";

        return ResponseEntity
                .ok()
                .eTag(etag)
                .body(song);
    }

    @PostMapping(consumes = "multipart/form-data")
    public Song addSong(@Valid @RequestPart("song") Song song,
                        @RequestPart("file") MultipartFile file) throws IOException {
        song.setId(null);
        String base64 = java.util.Base64.getEncoder()
                .encodeToString(file.getBytes());
        String mimeType = file.getContentType();

        String dataUrl = "data:" + mimeType + ";base64," + base64;

        song.setMusicDataUrl(dataUrl);

        return this.repository.save(song);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> changeSong(
            @PathVariable Long id,
            @RequestHeader("If-Match") String ifMatch,
            @Valid @RequestPart("song") Song updatedSong,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {

        Song existingSong = repository.findById(id)
                .orElseThrow();

        String currentEtag = "\"song-" + existingSong.getId() + "-v" + existingSong.getVersion() + "\"";

        if (!currentEtag.equals(ifMatch)) {
            return ResponseEntity
                    .status(HttpStatus.PRECONDITION_FAILED)
                    .body("Resource was modified by another user.");
        }

        existingSong.setTitle(updatedSong.getTitle());
        existingSong.setGenres(updatedSong.getGenres());
        existingSong.setLength(updatedSong.getLength());
        existingSong.setArtist(updatedSong.getArtist());

        if (file != null && !file.isEmpty()) {
            String base64 = java.util.Base64.getEncoder()
                    .encodeToString(file.getBytes());
            String mimeType = file.getContentType();

            String dataUrl = "data:" + mimeType + ";base64," + base64;
            existingSong.setMusicDataUrl(dataUrl);
        }

        return ResponseEntity.ok(repository.save(existingSong));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/audio")
    public ResponseEntity<byte[]> getSongAudio(@PathVariable Long id) {

        SongAudioProjection projection = repository.findProjectedById(id)
                .orElseThrow(() -> new RuntimeException("Song nicht gefunden"));

        String dataUrl = projection.getMusicDataUrl();
        String base64 = dataUrl.substring(dataUrl.indexOf(",") + 1);
        byte[] audioBytes = java.util.Base64.getDecoder().decode(base64);
        String mimeType = dataUrl.substring(dataUrl.indexOf(':')+1, dataUrl.indexOf(";")); // "audio/mpeg"


        return ResponseEntity.ok()
                .header("Content-Type", mimeType)
                .body(audioBytes);
    }



}
