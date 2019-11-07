package ar.edu.itba.hci.hoh.Elements;

import ar.edu.itba.hci.hoh.R;

public class Device {
    private String id;
    private String name;
    private DeviceType type;
    private Room room;
    private String state = "On";   // QUE ONDA ESTE STATE?
    private boolean favorite;

    public Device(String id, String name, DeviceType type, Room room, boolean favorite) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.room = room;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DeviceType getType() {
        return type;
    }

    public Room getRoom() {
        return room;
    }

    public String getState() {
        return state;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
