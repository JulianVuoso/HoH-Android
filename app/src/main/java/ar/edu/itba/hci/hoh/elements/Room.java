package ar.edu.itba.hci.hoh.elements;

import java.io.Serializable;
import java.util.Comparator;

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

    public static Comparator<Room> getComparator() {
        return (r1, r2) -> {
            int ret = r1.getName().compareTo(r2.getName());
            if (ret == 0)
                ret = r1.getId().compareTo(r2.getName());
            return ret;
        };
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
