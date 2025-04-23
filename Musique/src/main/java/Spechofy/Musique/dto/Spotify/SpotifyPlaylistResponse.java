package Spechofy.Musique.dto.Spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import Spechofy.Musique.dto.PlaylistDto;

import java.util.List;
import java.util.stream.Collectors;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyPlaylistResponse {

    @JsonProperty("items")
    private List<PlaylistItem> items;

    public List<PlaylistItem> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItem> items) {
        this.items = items;
    }

    public List<PlaylistDto> toPlaylistDtoList() {
        if (items == null) {
            return List.of(); // retourne une liste vide pour éviter les NullPointerExceptions
        }
    
        return items.stream()
                    .map(item -> new PlaylistDto(item.getName(), item.getId(), item.getOwner()))
                    .collect(Collectors.toList());
    }
    

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlaylistItem {
        @JsonProperty("name")
        private String name;

        @JsonProperty("id")
        private String id;

        @JsonProperty("owner")
        private String owner;

        public String getName() {
            return name;
        }

        public String getOwner() {
           return owner;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }
    }
}

