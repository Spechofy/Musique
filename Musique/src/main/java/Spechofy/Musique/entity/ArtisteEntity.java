package Spechofy.Musique.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Artistes")
public class ArtisteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artisteid")
    private Long artisteid;

    @Column(name = "name", nullable = false)
    private String name;

    // Utilisation de @ElementCollection pour les genres
    @ElementCollection
    @CollectionTable(name = "artiste_genres", joinColumns = @JoinColumn(name = "artisteid"))
    @Column(name = "genre")
    private List<String> genres;

    @Column(name = "rang", nullable = false)
    private Integer rang;

    @Column(name = "profilid")
    private Integer profilId;

    public Long getArtisteid() {
        return artisteid;
    }

    public void setArtisteid(Long artisteid) {
        this.artisteid = artisteid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
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
