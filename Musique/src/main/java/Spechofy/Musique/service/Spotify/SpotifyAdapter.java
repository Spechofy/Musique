package Spechofy.Musique.service.Spotify;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;


import Spechofy.Musique.dto.ArtistDto;
import Spechofy.Musique.dto.PlaylistDto;
import Spechofy.Musique.dto.Spotify.SpotifyArtistResponse;
import Spechofy.Musique.dto.Spotify.SpotifyPlaylistResponse;
import Spechofy.Musique.service.MusicService;

@Service
public class SpotifyAdapter implements MusicService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<ArtistDto> getTopArtists(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifyArtistResponse> response = restTemplate.exchange(
            "https://api.spotify.com/v1/me/top/artists",
            HttpMethod.GET,
            request,
            SpotifyArtistResponse.class
        );

        return response.getBody().toArtistDtoList();
    }

    @Override
    public List<PlaylistDto> getUserPlaylists(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifyPlaylistResponse> response = restTemplate.exchange(
            "https://api.spotify.com/v1/me/playlists",
            HttpMethod.GET,
            request,
            SpotifyPlaylistResponse.class
        );

        return response.getBody().toPlaylistDtoList();
    }
}