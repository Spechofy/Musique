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
import Spechofy.Musique.MusicProducer;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @Autowired
    private MusicFacadeService musicFacadeService;


    @Autowired
    private MusicProducer MusicProducer;

    @GetMapping("/")
    public String home() {
        return """
            <html>
                <body style="font-family: sans-serif;">
                    <h3>🔧 Voici les endpoints disponibles pour interagir avec l'API Spotify :</h3>
                    <ul>
                        <li>▶️ <code>/spotify/authorize</code> – Lance la connexion à l'API Spotify (OAuth)</li>
                        <li>🔄 <code>/spotify/callback?code=xxx</code> – Callback de Spotify, échange le code contre un token et récupère les playlists</li>
                        <li>🧾 <code>/spotify/token</code> – Récupère le token d'accès actuel</li>
                        <li>🎤 <code>/spotify/artists?access_token=xxx</code> – Récupère les artistes préférés de l'utilisateur (token requis)</li>
                        <li>🎶 <code>/spotify/playlists</code> – Récupère les playlists de l'utilisateur connecté</li>
                    </ul>
                    <p>✨ Amusez-vous bien avec l'API Spotify !</p>
                </body>
            </html>
            """;
    }
    


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
        String redirectUri = "http://127.0.0.1:7272/spotify/callback";
        String scopes = "user-top-read playlist-read-private user-read-currently-playing";


        String url = "https://accounts.spotify.com/authorize?" +
            "client_id=" + clientId +
            "&response_type=code" +
            "&redirect_uri=" + redirectUri +
            "&scope=" + scopes;

        return ResponseEntity.status(302).header("Location", url).build();
    }


    @GetMapping("/callback")
    public ResponseEntity<Void> callback(@RequestParam("code") String code) {
        try {
            String accessToken = spotifyAuthService.exchangeCodeForToken(code);
            List<Map<String, Object>> playlists = spotifyAuthService.fetchUserPlaylists();
            spotifyAuthService.fetchTopArtists(1); // 🧪 À remplacer par un vrai profilId plus tard*
            spotifyAuthService.fetchTopTracks(1); // 🧪 À remplacer avec un vrai profilId aussi


            
            System.out.println("Playlists récupérées : " + playlists.size());

            
            return ResponseEntity.status(302)
                    .header("Location", "/spotify/success")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .header("Location", "/spotify/error?message=" + e.getMessage())
                    .build();
        }
    }

    @GetMapping("/success")
    public String success() throws InterruptedException {
        MusicProducer.requestUsers();
        Thread.sleep(1000);
        MusicProducer.sendAllPlaylists();
        Thread.sleep(1000);
        MusicProducer.sendAllMusiques();
        Thread.sleep(1000);

        MusicProducer.sendAllMusiquesFav();
        Thread.sleep(2000);
        return "✅ Connexion réussie et playlists récupérées ! Vous pouvez fermer cette page.";
    }

    @GetMapping("/error")
    public String error(@RequestParam("message") String msg) {
        return "❌ Une erreur est survenue : " + msg;
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refreshDatabase() {
        try {
            spotifyAuthService.refreshDatabase();
            Thread.sleep(1000);

            MusicProducer.requestUsers();
            Thread.sleep(1000);
            MusicProducer.sendAllPlaylists();
            Thread.sleep(1000);
            MusicProducer.sendAllMusiques();
            Thread.sleep(1000);

            MusicProducer.sendAllMusiquesFav();
            Thread.sleep(2000);
            
            return ResponseEntity.ok("✅ La base de données a été rafraîchie avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Une erreur est survenue : " + e.getMessage());
        }
    }

    @GetMapping("/actual")
    public ResponseEntity<String> getCurrentlyPlayingTrack() {
        try {
            Map<String, String> currentTrack = spotifyAuthService.getCurrentlyPlaying();

            if (currentTrack == null) {
                return ResponseEntity.ok("🎵 Aucune musique n'est en cours de lecture !");
            }

            String response = "🎶 Titre : " + currentTrack.get("title") + " | Artiste : " + currentTrack.get("artist");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erreur lors de la récupération du morceau actuel.");
        }
    }

}
