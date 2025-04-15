package Spechofy.Musique.controller;

import Spechofy.Musique.service.SpotifyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyController {

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        String token = spotifyAuthService.getAccessToken();
        return ResponseEntity.ok(token);
    }

}
