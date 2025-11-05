package org.example.backend.repository;
import org.example.backend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByTitleContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String title, String artistName);

}