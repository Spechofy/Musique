package Spechofy.Musique.repository;

import Spechofy.Musique.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    boolean existsByName(String name);
}
