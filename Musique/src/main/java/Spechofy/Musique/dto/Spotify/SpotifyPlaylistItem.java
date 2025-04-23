package Spechofy.Musique.dto.Spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyPlaylistItem {

    private String id;
    private String name;
    private String owner;

    @JsonProperty("tracks")
    private TracksInfo tracks;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public TracksInfo getTracks() {
        return tracks;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setTracks(TracksInfo tracks) {
        this.tracks = tracks;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TracksInfo {
        @JsonProperty("total")
        private int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
