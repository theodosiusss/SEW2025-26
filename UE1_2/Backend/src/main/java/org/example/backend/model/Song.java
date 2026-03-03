package org.example.backend.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title muss vorhanden sein")
    private String title;

    @ElementCollection
    @CollectionTable(name = "song_genres", joinColumns = @JoinColumn(name = "song_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    @NotBlank(message = "Länge muss vorhanden sein")
    @Pattern(
            regexp = "^[0-9]{1,2}:[0-5][0-9]$",
            message = "Länge muss im richtigen Format sein mm:ss"
    )
    private String length;

    @ManyToOne (fetch = FetchType.EAGER)
    private Artist artist;

    @ManyToOne (fetch = FetchType.EAGER)
    private User user;


    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @JsonIgnore
    private String musicDataUrl;


    @Version
    private Long version= 0L;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getMusicDataUrl() {
        return musicDataUrl;
    }

    public void setMusicDataUrl(String musicDataUrl) {
        this.musicDataUrl = musicDataUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
