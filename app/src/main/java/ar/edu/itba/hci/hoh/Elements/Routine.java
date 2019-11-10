package ar.edu.itba.hci.hoh.Elements;

public class Routine {
    private String id;
    private String name;
    private RoutineMeta meta;

    public Routine(String id, String name, RoutineMeta meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
    }

    public Routine(String id, String name, String img) {
        this.id = id;
        this.name = name;
        this.meta = new RoutineMeta(img);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RoutineMeta getMeta() {
        return meta;
    }

    private class RoutineMeta {
        private String img;

        public RoutineMeta(String img) {
            this.img = img;
        }

        public String getImg() {
            return img;
        }
    }
}
