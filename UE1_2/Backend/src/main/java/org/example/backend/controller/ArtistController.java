package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.model.Artist;
import org.example.backend.repository.ArtistRepository;
import org.example.backend.repository.SongRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/artists")
@CrossOrigin(origins = "*")
public class ArtistController {
    private final ArtistRepository repository;

    public ArtistController(ArtistRepository ArtistRepository) {
        this.repository = ArtistRepository;
    }

    @GetMapping
    public List<Artist> getAllArtists() {
        return this.repository.findAll();
    }
    @GetMapping("/{id}")
    public Artist getArtistById(@PathVariable long id) {
        return this.repository.findById(id).orElseThrow();
    }
    @PostMapping
    public Artist saveArtist(@Valid @RequestBody Artist artist) {
        artist.setId(null);
        return this.repository.save(artist);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtist(@PathVariable Long id, @Valid @RequestBody Artist artist,
                               @RequestHeader("If-Match") long ifMatch
                               ) {
        Artist existingArtist = repository.findById(id).orElseThrow();
        if (existingArtist.getVersion() != ifMatch) {
            return ResponseEntity
                    .status(HttpStatus.PRECONDITION_FAILED)
                    .body("Resource was modified by another user.");
        }
        existingArtist.setName(artist.getName());
        return ResponseEntity.ok(repository.save(existingArtist));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
