package Spechofy.Musique.service.Spotify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import Spechofy.Musique.entity.ArtisteEntity;
import Spechofy.Musique.entity.MusiqueEntity;
import Spechofy.Musique.entity.MusiqueFavEntity;
import Spechofy.Musique.entity.PlaylistEntity;
import Spechofy.Musique.repository.ArtistesRepository;
import Spechofy.Musique.repository.MusiqueFavRepository;
import Spechofy.Musique.repository.MusiqueRepository;
import Spechofy.Musique.repository.PlaylistRepository;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private ArtistesRepository artistesRepository;

    @Autowired
    private MusiqueFavRepository musiqueFavRepository;



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
    
            
            if (!playlistRepository.existsByName(name)) {
                PlaylistEntity saved = playlistRepository.save(entity);
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
                musique.setGenre(null); // A voir pour modifier
                musique.setPlaylistId(localPlaylistId.intValue());
    
                MusiqueRepository.save(musique);
            }
    
            url = (String) response.getBody().get("next");
        }
    }

    public void refreshDatabase() {
        List<Map<String, Object>> playlists = fetchUserPlaylists();
    
        for (Map<String, Object> playlist : playlists) {
            String name = (String) playlist.get("name");
            String description = (String) playlist.get("description");
            String spotifyPlaylistId = (String) playlist.get("id");
    
            PlaylistEntity entity = playlistRepository.findByName(name).orElse(null);
    
            // Si la playlist n'existe pas encore
            if (entity == null) {
                entity = new PlaylistEntity();
                entity.setName(name);
                entity.setDescription(description);
                entity.setProfilId(1); // à adapter
                entity = playlistRepository.save(entity);
            }
    
            Long localPlaylistId = entity.getPlaylistId();
    
            // Récupère les musiques actuelles de Spotify
            List<MusiqueEntity> spotifyTracks = getTracksFromSpotifyPlaylist(spotifyPlaylistId, localPlaylistId);
    
            // Récupère les musiques locales
            List<MusiqueEntity> localTracks = MusiqueRepository.findByPlaylistId(localPlaylistId.intValue());
    
            // Supprime celles qui n'existent plus
            for (MusiqueEntity localTrack : localTracks) {
                boolean stillExists = spotifyTracks.stream().anyMatch(
                    t -> t.getTitle().equals(localTrack.getTitle()) &&
                         t.getArtiste().equals(localTrack.getArtiste())
                );
    
                if (!stillExists) {
                    MusiqueRepository.delete(localTrack);
                }
            }
    
            // Ajoute celles qui sont nouvelles
            for (MusiqueEntity spotifyTrack : spotifyTracks) {
                boolean alreadyExists = localTracks.stream().anyMatch(
                    t -> t.getTitle().equals(spotifyTrack.getTitle()) &&
                         t.getArtiste().equals(spotifyTrack.getArtiste())
                );
    
                if (!alreadyExists) {
                    MusiqueRepository.save(spotifyTrack);
                }
            }
        }
    }

    public List<MusiqueEntity> getTracksFromSpotifyPlaylist(String spotifyPlaylistId, Long localPlaylistId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        List<MusiqueEntity> result = new ArrayList<>();
        String url = "https://api.spotify.com/v1/playlists/" + spotifyPlaylistId + "/tracks";
        
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
                musique.setGenre(null);
                musique.setPlaylistId(localPlaylistId.intValue());

                result.add(musique);
            }

            url = (String) response.getBody().get("next"); // ✅ C’est déjà un String
        }

        return result;
    }

    public void fetchTopArtists(int profilId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        String url = "https://api.spotify.com/v1/me/top/artists?limit=5";

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        List<Map<String, Object>> artists = (List<Map<String, Object>>) response.getBody().get("items");

        int rang = 1;

        for (Map<String, Object> artist : artists) {
            if (artistesRepository.existsByProfilIdAndRang(profilId, rang)) {
                rang++;
                continue;
            }
            
            String name = (String) artist.get("name");
            List<String> genres = (List<String>) artist.get("genres");

            ArtisteEntity entity = new ArtisteEntity();
            entity.setName(name);
            entity.setGenres(genres);
            entity.setRang(rang++);
            entity.setProfil(profilId); // 🔗 Associe à un utilisateur

            artistesRepository.save(entity);
        }
    }

    public void fetchTopTracks(int profilId) {
        RestTemplate restTemplate = new RestTemplate();
    
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> request = new HttpEntity<>(headers);
    
        String url = "https://api.spotify.com/v1/me/top/tracks?limit=5";
    
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        List<Map<String, Object>> tracks = (List<Map<String, Object>>) response.getBody().get("items");
    
        int rang = 1;
    
        for (Map<String, Object> track : tracks) {
            // Vérification avant insertion
            if (musiqueFavRepository.existsByProfilIdAndRang(profilId, rang)) {
                rang++;
                continue; // Déjà existant, on saute
            }
    
            String title = (String) track.get("name");
            List<Map<String, Object>> artists = (List<Map<String, Object>>) track.get("artists");
            String artiste = artists.size() > 0 ? (String) artists.get(0).get("name") : "Inconnu";
    
            MusiqueFavEntity entity = new MusiqueFavEntity();
            entity.setTitle(title);
            entity.setArtiste(artiste);
            entity.setGenre(null);
            entity.setRang(rang++);
            entity.setProfil(profilId);
    
            musiqueFavRepository.save(entity);
        }
    }

    public Map<String, String> getCurrentlyPlaying() {
        RestTemplate restTemplate = new RestTemplate();
    
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> request = new HttpEntity<>(headers);
    
        ResponseEntity<Map> response = restTemplate.exchange(
            "https://api.spotify.com/v1/me/player/currently-playing",
            HttpMethod.GET,
            request,
            Map.class
        );
    
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            // Aucun morceau en cours
            return null;
        }
    
        Map<String, Object> body = response.getBody();
        if (body == null || body.get("item") == null) {
            return null; // Pas d'item
        }
    
        Map<String, Object> item = (Map<String, Object>) body.get("item");
    
        // Récupération du titre
        Object titleObj = item.get("name");
        String title = titleObj != null ? titleObj.toString() : "Titre inconnu";
    
        // Récupération de l'artiste
        List<Map<String, Object>> artists = (List<Map<String, Object>>) item.get("artists");
        String artistName = artists != null && !artists.isEmpty() ? artists.get(0).get("name").toString() : "Artiste inconnu";
    
        // (Optionnel) Récupération de l'URL de l'album ou de la musique
        Map<String, Object> externalUrls = (Map<String, Object>) item.get("external_urls");
        String spotifyUrl = externalUrls != null ? externalUrls.get("spotify").toString() : null;
    
        // (Optionnel) Récupération de l'image de l'album
        Map<String, Object> album = (Map<String, Object>) item.get("album");
        List<Map<String, Object>> images = album != null ? (List<Map<String, Object>>) album.get("images") : null;
        String albumImageUrl = images != null && !images.isEmpty() ? images.get(0).get("url").toString() : null;
    
        Map<String, String> result = new HashMap<>();
        result.put("title", title);
        result.put("artist", artistName);
        if (spotifyUrl != null) {
            result.put("spotify_url", spotifyUrl);
        }
        if (albumImageUrl != null) {
            result.put("album_image", albumImageUrl);
        }
    
        return result;
    }
    

    
}
