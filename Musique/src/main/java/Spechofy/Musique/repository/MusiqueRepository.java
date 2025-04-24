package Spechofy.Musique.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import Spechofy.Musique.entity.MusiqueEntity;

public interface MusiqueRepository extends JpaRepository<MusiqueEntity, Long> {
    List<MusiqueEntity> findByPlaylistId(Integer playlistId);
}
