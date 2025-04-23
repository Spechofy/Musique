package Spechofy.Musique.dto;

public class PlaylistDto {

    private String id;
    private String name;
    private String owner;
    
    public PlaylistDto(String id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }
    

    
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setowner(String owner) {
        this.owner = owner;
    }
}

