package ar.edu.itba.hci.hoh.Elements;

import ar.edu.itba.hci.hoh.R;

public class RoomMeta {
    private String image;
    private boolean favorite;

    public RoomMeta(String image, boolean favorite) {
        this.image = image;
        this.favorite = favorite;
    }

    public String getImage() {
        // NO PUEDO MOVER ESTO AL CONSTRUCTOR PORQUE EL GSON NO LO USA
        return image.substring(0, image.length() - ".jpg".length());
    }

    public boolean isFavorite() {
        return favorite;
    }
}
