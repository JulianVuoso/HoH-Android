package ar.edu.itba.hci.hoh.elements;

import java.io.Serializable;

public class Device implements Serializable {
    private String id;
    private String name;
    private DeviceType type;
    private DeviceState state;
    private Room room;
    private DeviceMeta meta;

    public Device(String id, String name, DeviceType type, Room room, boolean favorite) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.room = room;
        this.meta = new DeviceMeta(favorite);
    }

    public Device(String id, String name, DeviceType type, Room room, DeviceMeta meta) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.room = room;
        this.meta = meta;
    }

    public Device(String id, String name, DeviceMeta meta) {
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

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public DeviceState getState() {
        return state;
    }

    public DeviceMeta getMeta() {
        return meta;
    }

    public class DeviceMeta implements Serializable{
        private boolean favorite;

        public DeviceMeta(boolean favorite) {
            this.favorite = favorite;
        }

        public boolean isFavorite() {
            return favorite;
        }

        public void setFavorite(boolean favorite) {
            this.favorite = favorite;
        }
    }
}
