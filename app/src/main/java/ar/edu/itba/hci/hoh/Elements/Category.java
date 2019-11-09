package ar.edu.itba.hci.hoh.Elements;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    public String[] getTypes() {
        return types;
    }

    public boolean checkDeviceType(DeviceType type) {
        for (String name : types) {
            if (name.equals(type.getName()))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Category getCategoryFromType(DeviceType type, List<Category> categoryList) {
        for (Category category : categoryList) {
            if (category.checkDeviceType(type))
                return category;
        }
        return null;
    }
}
