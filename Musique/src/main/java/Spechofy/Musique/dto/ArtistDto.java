package Spechofy.Musique.dto;

public class ArtistDto {
    private String name;
    private String id;

    public ArtistDto(String name, String id) {
        this.name = name;
        this.id = id;
    }

    // Getters and setters
    public String getName() {
        return name;
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
}
