package Spechofy.Musique.dto.Spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import Spechofy.Musique.dto.ArtistDto;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyArtistResponse {

    @JsonProperty("artists")
    private Artists artists;

    public List<ArtistDto> toArtistDtoList() {
        return artists.getItems().stream().map(item -> new ArtistDto(item.getName(), item.getId())).collect(Collectors.toList());
    }

    public Artists getArtists() {
        return artists;
    }

    public void setArtists(Artists artists) {
        this.artists = artists;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Artists {
        private List<ArtistItem> items;

        public List<ArtistItem> getItems() {
            return items;
        }

        public void setItems(List<ArtistItem> items) {
            this.items = items;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ArtistItem {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
