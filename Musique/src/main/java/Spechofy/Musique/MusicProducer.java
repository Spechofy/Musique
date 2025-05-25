package Spechofy.Musique;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Spechofy.Musique.entity.MusiqueEntity;
import Spechofy.Musique.entity.MusiqueFavEntity;
import Spechofy.Musique.entity.PlaylistEntity;
import Spechofy.Musique.repository.MusiqueFavRepository;
import Spechofy.Musique.repository.MusiqueRepository;
import Spechofy.Musique.repository.PlaylistRepository;
import java.util.List;


@Service
public class MusicProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String REQUEST_TOPIC = "get-user-topic";

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusiqueFavRepository musiqueFavRepository;

    public void sendAllMusiquesFav() {
        List<MusiqueFavEntity> favs = musiqueFavRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(favs);
            kafkaTemplate.send("musique-fav-topic", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void requestUsers() {
        kafkaTemplate.send(REQUEST_TOPIC, "request-users");
    }

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

     @Autowired
    private MusiqueRepository musiqueRepository;

    public void sendAllMusiques() {
        List<MusiqueEntity> musiques = musiqueRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(musiques);
            kafkaTemplate.send("musique-topic", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
