package org.example.backend.repository;

import org.example.backend.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface SongRepository extends JpaRepository<Song, Long> {
    //    Page<Song> findByTitleContainingIgnoreCaseOrGenresContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String title, String genre,String artistName, Pageable pageable
//    );
    @Query("SELECT s FROM Song s " +
            "WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%')) " +
            "OR LOWER(s.artist.name) LIKE LOWER(CONCAT('%', :artistName, '%')) " +
            "OR EXISTS (SELECT 1 FROM s.genres g WHERE LOWER(g) LIKE LOWER(CONCAT('%', :genre, '%')))")
    Page<Song> search(@Param("title") String title,
                      @Param("genre") String genre,
                      @Param("artistName") String artistName,
                      Pageable pageable);

    Optional<SongAudioProjection> findProjectedById(Long id);

}