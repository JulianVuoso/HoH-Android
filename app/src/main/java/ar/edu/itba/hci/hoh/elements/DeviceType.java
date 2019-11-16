package ar.edu.itba.hci.hoh.elements;

import java.io.Serializable;

import ar.edu.itba.hci.hoh.R;

public class DeviceType implements Serializable {
    private String id;
    private String name;
    // VER SI HACE FALTA PONER ALGO DE ACTIONS (Si rompe el GSON, sino no)

    public DeviceType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceType)) return false;
        DeviceType type = (DeviceType) o;
        return id.equals(type.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static int getDeviceTypeDrawable(DeviceType type) {
        if (type == null) return R.drawable.ic_nodev_black_40dp;

        switch (type.name) {
            case "speaker":         return R.drawable.ic_speaker_black_40dp;
//            case "blinds":          return R.drawable.ic_blinds_black_50dp;
            case "lamp":            return R.drawable.ic_light_black_40dp;
//            case "oven":            return R.drawable.ic_oven_black_50dp;
//            case "ac":              return R.drawable.ic_airconditioner_black_50dp;
            case "door":            return R.drawable.ic_door_black_40dp;
            case "refrigerator":    return R.drawable.ic_fridge_black_40dp;
            default:                return R.drawable.ic_nodev_black_40dp;
        }
    }
}
