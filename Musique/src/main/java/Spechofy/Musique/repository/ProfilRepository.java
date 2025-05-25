package Spechofy.Musique.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Spechofy.Musique.entity.ProfilEntity;

@Repository
public interface ProfilRepository extends JpaRepository<ProfilEntity, Long> {
    // Tu peux ajouter des méthodes de recherche personnalisées ici, si nécessaire

    // Exemple de méthode pour trouver un profil par son userId
    ProfilEntity findByUserId(Integer userId);
}
