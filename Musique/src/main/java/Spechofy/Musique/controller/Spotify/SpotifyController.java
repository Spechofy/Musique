package Spechofy.Musique.controller.Spotify;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Spechofy.Musique.dto.ArtistDto;
import Spechofy.Musique.dto.PlaylistDto;
import Spechofy.Musique.service.Spotify.MusicFacadeService;
import Spechofy.Musique.service.Spotify.SpotifyAuthService;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @Autowired
    private MusicFacadeService musicFacadeService;

    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        String token = spotifyAuthService.getAccessToken();
        return ResponseEntity.ok(token);
    }
    

    @GetMapping("/artists")
    public ResponseEntity<List<ArtistDto>> getTopArtists(@RequestParam("access_token") String token) {
        List<ArtistDto> artists = musicFacadeService.getTopArtists();
        return ResponseEntity.ok(artists);
    }


    @GetMapping("/playlists")
    public ResponseEntity<List<PlaylistDto>> getPlaylists() {
        String token = spotifyAuthService.getAccessToken();
        List<PlaylistDto> playlists = musicFacadeService.getUserPlaylists();
        return ResponseEntity.ok(playlists);
    }


    @GetMapping("/authorize")
    public ResponseEntity<Void> authorize() {
        String clientId = "24a2559b1dcc4ea5aa895fc40dbb5e8f";
        String redirectUri = "http://127.0.0.1:7272/spotify/callback"; // doit matcher Spotify Dev Console
        String scopes = "user-top-read playlist-read-private";

        String url = "https://accounts.spotify.com/authorize?" +
            "client_id=" + clientId +
            "&response_type=code" +
            "&redirect_uri=" + redirectUri +
            "&scope=" + scopes;

        return ResponseEntity.status(302).header("Location", url).build();
    }

    


    @GetMapping("/callback")
public ResponseEntity<String> callback(@RequestParam("code") String code) {
    try {
        String accessToken = spotifyAuthService.exchangeCodeForToken(code);
        List<Map<String, Object>> playlists = spotifyAuthService.fetchUserPlaylists();
        
        // Tu peux logguer les playlists ici si tu veux
        System.out.println("Playlists récupérées : " + playlists.size());

        return ResponseEntity.ok("✅ Playlists bien insérées !");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("❌ Une erreur est survenue : " + e.getMessage());
    }
}


}
