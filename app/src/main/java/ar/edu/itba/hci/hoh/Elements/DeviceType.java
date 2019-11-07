package ar.edu.itba.hci.hoh.Elements;

import ar.edu.itba.hci.hoh.R;

public class DeviceType {
    private String id;
    private String name;

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

    public static int getDeviceTypeDrawable(DeviceType type) {
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
