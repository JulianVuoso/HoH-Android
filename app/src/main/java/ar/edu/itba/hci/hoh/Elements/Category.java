package ar.edu.itba.hci.hoh.Elements;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;
    private int drawableId;
    private String[] types;

    public Category(String name, int drawableId, String ... types) {
        this.name = name;
        this.drawableId = drawableId;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public boolean checkDeviceType(DeviceType type) {
        for (String name : types) {
            if (name.equals(type.getName()))
                return true;
        }
        return false;
    }
}
