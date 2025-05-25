package Spechofy.Musique;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Spechofy.Musique.entity.PlaylistEntity;
import Spechofy.Musique.repository.PlaylistRepository;
import java.util.List;


@Service
public class MusicProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String REQUEST_TOPIC = "get-user-topic";

    public void requestUsers() {
        kafkaTemplate.send(REQUEST_TOPIC, "request-users");
    }

    @Autowired
    private PlaylistRepository playlistRepository;

    public void sendAllPlaylists() {
        List<PlaylistEntity> playlists = playlistRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String playlistsJson = mapper.writeValueAsString(playlists);
            kafkaTemplate.send("playlist-topic", playlistsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
