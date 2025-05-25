package Spechofy.Musique.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MusiqueFav")
public class MusiqueFavEntity {

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

    @Column(name = "rang", nullable = false)
    private Integer rang;

    @Column(name = "profilid")
    private Integer profilId;

    
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

    public Integer getRang() {
        return rang;
    }

    public void setRang(Integer rang) {
        this.rang = rang;
    }

    public Integer getProfilId() {
        return profilId;
    }

    public void setProfil(Integer profil) {
        this.profilId = profil;
    }
}
