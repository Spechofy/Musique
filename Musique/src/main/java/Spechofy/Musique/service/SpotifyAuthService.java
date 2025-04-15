package Spechofy.Musique.service;

import Spechofy.Musique.dto.SpotifyTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class SpotifyAuthService {

    private final String clientId = "24a2559b1dcc4ea5aa895fc40dbb5e8f";
    private final String clientSecret = "90d0390a794b46559f216e0c8c4ff0bc";
    private final String tokenUrl = "https://accounts.spotify.com/api/token";

    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodedAuth);

        String body = "grant_type=client_credentials";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<SpotifyTokenResponse> response = restTemplate
                .postForEntity(tokenUrl, request, SpotifyTokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getAccessToken();
        } else {
            throw new RuntimeException("Erreur lors de la récupération du token Spotify : " + response.getStatusCode());
        }
    }
}
