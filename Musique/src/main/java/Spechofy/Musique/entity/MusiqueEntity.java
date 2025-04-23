package Spechofy.Musique.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Musique")
public class MusiqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musicid")
    private Long musicId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artiste")
    private String artiste;

    @Column(name = "genre")
    private String genre;

    @Column(name = "playlistid")
    private Integer playlistId;

    
    public Long getMusicId() {
        return musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }
}
