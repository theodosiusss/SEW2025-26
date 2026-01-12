package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.model.Song;
import org.example.backend.repository.SongRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/songs")
@CrossOrigin(origins = "*")
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
        return this.repository.findByTitleContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(search,search,pageable);
    }

    @GetMapping("/{id}")
    public Song getSongById(@PathVariable long id) {
        return this.repository.findById(id).orElseThrow();
    }
    @PostMapping
    public Song addSong(@Valid  @RequestBody Song song) {
        song.setId(null);
        return this.repository.save(song);
    }
    @PutMapping("/{id}")
    public Song changeSong(@PathVariable Long id, @Valid @RequestBody Song song) {
        song.setId(id);
        return this.repository.save(song);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}
