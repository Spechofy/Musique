package Spechofy.Musique.service;

import java.util.List;

import Spechofy.Musique.dto.ArtistDto;
import Spechofy.Musique.dto.PlaylistDto;

public interface MusicService {
    List<ArtistDto> getTopArtists(String accessToken);
    List<PlaylistDto> getUserPlaylists(String accessToken);
}
