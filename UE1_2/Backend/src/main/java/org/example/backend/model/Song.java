package org.example.backend.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title muss vorhanden sein")
    private String title;
    @NotBlank(message = "Genre muss vorhanden sein")
    private String genre;
    @NotBlank(message = "Länge muss vorhanden sein")
    @Pattern(
            regexp = "^[0-9]{1,2}:[0-5][0-9]$",
            message = "Länge muss im richtigen Format sein mm:ss"
    )
    private String length;

    @ManyToOne (fetch = FetchType.EAGER)
    private Artist artist;

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
