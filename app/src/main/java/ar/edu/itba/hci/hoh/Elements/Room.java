package ar.edu.itba.hci.hoh.Elements;

import java.util.Objects;

public class Room {
    private String id;
    private String name;
    private String image;   // HACE FALTA? SE USA ACA?
    private boolean favorite;

    public Room(String id, String name, String image, boolean favorite) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
