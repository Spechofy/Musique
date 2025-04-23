package Spechofy.Musique.service.Spotify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import Spechofy.Musique.dto.ArtistDto;
import Spechofy.Musique.dto.PlaylistDto;
import Spechofy.Musique.service.MusicService;

@Service
public class MusicFacadeService {

    private final MusicService musicService;
    private final SpotifyAuthService authService;
    private final RestTemplate restTemplate;

    @Autowired
    public MusicFacadeService(MusicService musicService, SpotifyAuthService authService, RestTemplate restTemplate) {
        this.musicService = musicService;
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    public List<ArtistDto> getTopArtists() {
        String token = authService.getAccessToken();
        return musicService.getTopArtists(token);
    }

    public List<PlaylistDto> getUserPlaylists() {
        String token = authService.getAccessToken();
        return fetchPlaylistsFromSpotify(token);
    }

    private List<PlaylistDto> fetchPlaylistsFromSpotify(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://api.spotify.com/v1/me/playlists";
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
            );
            
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
            List<PlaylistDto> playlists = new ArrayList<>();

            for (Map<String, Object> item : items) {
                String id = (String) item.get("id");
                String name = (String) item.get("name");

                Map<String, Object> owner = (Map<String, Object>) item.get("owner");
                String ownerName = owner != null ? (String) owner.get("display_name") : "unknown";

                playlists.add(new PlaylistDto(id, name, ownerName));
            }

            return playlists;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Log l'erreur ou gérer l'exception selon le besoin
            throw new RuntimeException("Error while fetching playlists from Spotify: " + e.getMessage());
        }
    }
}
