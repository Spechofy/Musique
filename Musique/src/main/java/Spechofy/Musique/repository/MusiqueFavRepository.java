package Spechofy.Musique.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import Spechofy.Musique.entity.MusiqueFavEntity;

public interface MusiqueFavRepository extends JpaRepository<MusiqueFavEntity, Long> {
    boolean existsByProfilIdAndRang(Integer profilId, Integer rang);

}


