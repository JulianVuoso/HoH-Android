package ar.edu.itba.hci.hoh.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private String name;
    private int drawableId;
    private List<DeviceType> types = new ArrayList<>();

    public Category(String name, int drawableId, List<DeviceType> types) {
        this.name = name;
        this.drawableId = drawableId;
        this.types = types;
    }

    public Category(String name, int drawableId) {
        this.name = name;
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public List<DeviceType> getTypes() {
        return types;
    }

    public void addType(DeviceType type) {
        types.add(type);
    }

    public boolean checkDeviceType(DeviceType type) {
        for (DeviceType element : types) {
            if (element.equals(type))
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

    public static Category getCategoryFromType(DeviceType type, List<Category> categoryList) {
        for (Category category : categoryList) {
            if (category.checkDeviceType(type))
                return category;
        }
        return null;
    }
}
