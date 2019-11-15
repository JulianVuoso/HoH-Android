package ar.edu.itba.hci.hoh.Elements;

import java.io.Serializable;

public class Routine implements Serializable {
    private String id;
    private String name;
    private RoutineMeta meta;

    public Routine(String id, String name, RoutineMeta meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
    }

    public Routine(String id, String name, String img, boolean favorite) {
        this.id = id;
        this.name = name;
        this.meta = new RoutineMeta(img, favorite);
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

    public class RoutineMeta {
        private String img;
        private boolean favorite;

        public RoutineMeta(String img, boolean favorite) {
            this.img = img;
            this.favorite = favorite;
        }

        public String getImg() {
            return img.substring(0, img.length() - ".jpg".length());
        }

        public boolean isFavorite() {
            return favorite;
        }
    }
}
