package org.example.backend.controller;

import org.example.backend.model.Song;
import org.example.backend.repository.SongRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/songs")
@CrossOrigin(origins = "*")
public class SongController {

    private final SongRepository repository;

    public SongController(SongRepository songRepository) {
        this.repository = songRepository;
    }

    @GetMapping
    public List<Song> getAllSongs() {
        return this.repository.findAll();
    }
    @PostMapping
    public Song addSong(@RequestBody Song song) {
        song.setId(null);
        return this.repository.save(song);
    }


}
