package ar.edu.itba.hci.hoh.Elements;

import ar.edu.itba.hci.hoh.R;

public class Device {
    private String id;
    private String name;
    private DeviceType type;
    private Object state;   // TODO: QUE ONDA ESTE STATE? VER SI HACE FALTA UNO POR TIPO DE DISP
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

    public String getState() {
//        return state;
        return "On";
    }

    public DeviceMeta getMeta() {
        return meta;
    }

    private class DeviceMeta {
        private boolean favorite;

        public DeviceMeta(boolean favorite) {
            this.favorite = favorite;
        }

        public boolean isFavorite() {
            return favorite;
        }
    }
}
