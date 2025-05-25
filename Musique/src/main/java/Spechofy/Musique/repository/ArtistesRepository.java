package Spechofy.Musique.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import Spechofy.Musique.entity.ArtisteEntity;

public interface ArtistesRepository extends JpaRepository<ArtisteEntity, Long> {
    boolean existsByProfilIdAndRang(Integer profilId, Integer rang);

}

