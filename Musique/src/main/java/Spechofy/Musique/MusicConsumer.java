package Spechofy.Musique;

import Spechofy.Musique.entity.ProfilEntity;
import Spechofy.Musique.repository.ProfilRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicConsumer {

    @Autowired
    private ProfilRepository ProfilRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "Profil-response-topic", groupId = "playlist-service-group")
    public void consume(String message) {
        try {
            List<ProfilEntity> Profils = objectMapper.readValue(message, new TypeReference<List<ProfilEntity>>() {});
            ProfilRepository.saveAll(Profils);
            System.out.println("✅ Utilisateurs insérés avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
