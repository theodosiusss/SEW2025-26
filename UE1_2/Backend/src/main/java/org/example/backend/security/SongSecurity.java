package org.example.backend.security;

import org.example.backend.repository.SongRepository;
import org.springframework.stereotype.Component;

@Component
public class SongSecurity {

    private final SongRepository songRepository;

    public SongSecurity(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public boolean isOwner(Long songId, String username) {
        return songRepository.findById(songId)
                .map(song -> song.getUser()
                        .getUsername()
                        .equals(username))
                .orElse(false);
    }
}
