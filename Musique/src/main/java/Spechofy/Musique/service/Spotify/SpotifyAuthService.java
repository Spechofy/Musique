package Spechofy.Musique.service.Spotify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import Spechofy.Musique.entity.MusiqueEntity;
import Spechofy.Musique.entity.PlaylistEntity;
import Spechofy.Musique.repository.MusiqueRepository;
import Spechofy.Musique.repository.PlaylistRepository;

import java.util.List;
import java.util.Map;

@Service
public class SpotifyAuthService {

    private final String clientId = "24a2559b1dcc4ea5aa895fc40dbb5e8f";
    private final String clientSecret = "90d0390a794b46559f216e0c8c4ff0bc";
    String redirectUri = "http://127.0.0.1:7272/spotify/callback";

    private String accessToken;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusiqueRepository MusiqueRepository;


    public String getAccessToken() {
        return this.accessToken;
    }
    

    public String exchangeCodeForToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);
    
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
    
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
    
        ResponseEntity<Map> response = restTemplate.postForEntity(
            "https://accounts.spotify.com/api/token",
            request,
            Map.class
        );
    
        String accessToken = (String) response.getBody().get("access_token");
    
        this.accessToken = accessToken;
    
        return accessToken;
    }

    public List<Map<String, Object>> fetchUserPlaylists() {
        RestTemplate restTemplate = new RestTemplate();
    
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> request = new HttpEntity<>(headers);
    
        ResponseEntity<Map> response = restTemplate.exchange(
            "https://api.spotify.com/v1/me/playlists",
            HttpMethod.GET,
            request,
            Map.class
        );
    
        List<Map<String, Object>> playlists = (List<Map<String, Object>>) response.getBody().get("items");
    
        for (Map<String, Object> playlist : playlists) {
            String name = (String) playlist.get("name");
            String description = (String) playlist.get("description");
            String spotifyPlaylistId = (String) playlist.get("id");
    
            PlaylistEntity entity = new PlaylistEntity();
            entity.setName(name);
            entity.setDescription(description);
            entity.setProfilId(1); // 🧪 à adapter avec un vrai ID plus tard
    
            // Vérifie si elle n'existe pas déjà
            if (!playlistRepository.existsByName(name)) {
                PlaylistEntity saved = playlistRepository.save(entity);
    
                // 🔥 Enregistrement des musiques
                saveTracksForPlaylist(spotifyPlaylistId, saved.getPlaylistId());
            }
        }
    
        return playlists;
    }

    public void saveTracksForPlaylist(String spotifyPlaylistId, Long localPlaylistId) {
        RestTemplate restTemplate = new RestTemplate();
    
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
    
        String url = "https://api.spotify.com/v1/playlists/" + spotifyPlaylistId + "/tracks?limit=100";
    
        while (url != null) {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
    
            for (Map<String, Object> item : items) {
                Map<String, Object> track = (Map<String, Object>) item.get("track");
    
                if (track == null) continue;
    
                String title = (String) track.get("name");
                List<Map<String, Object>> artists = (List<Map<String, Object>>) track.get("artists");
                String artist = artists.size() > 0 ? (String) artists.get(0).get("name") : "Inconnu";
    
                MusiqueEntity musique = new MusiqueEntity();
                musique.setTitle(title);
                musique.setArtiste(artist);
                musique.setGenre(null); // Toujours optionnel ici
                musique.setPlaylistId(localPlaylistId.intValue());
    
                MusiqueRepository.save(musique);
            }
    
            url = (String) response.getBody().get("next"); // pagination
        }
    }
    
    
}
