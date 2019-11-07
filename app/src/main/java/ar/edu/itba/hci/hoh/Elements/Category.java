package ar.edu.itba.hci.hoh.Elements;

public class Category {
    private String name;
    private int drawableId;

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
}
