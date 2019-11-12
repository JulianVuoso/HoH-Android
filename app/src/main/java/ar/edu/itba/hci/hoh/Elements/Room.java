package ar.edu.itba.hci.hoh.Elements;

import java.io.Serializable;

public class Room implements Serializable {
    private String id;
    private String name;
    private RoomMeta meta;

    public Room(String id, String name, String image, boolean favorite) {
        this.id = id;
        this.name = name;
        this.meta = new RoomMeta(image, favorite);
    }

    public Room(String id, String name, RoomMeta meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RoomMeta getMeta() {
        return meta;
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
