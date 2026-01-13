package org.example.backend.repository;
import org.example.backend.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SongRepository extends JpaRepository<Song, Long> {
    Page<Song> findByTitleContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String title, String artistName, Pageable pageable
    );

    Optional<SongAudioProjection> findProjectedById(Long id);

}