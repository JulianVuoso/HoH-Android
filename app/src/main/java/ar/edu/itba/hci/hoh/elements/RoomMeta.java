package ar.edu.itba.hci.hoh.elements;

import java.io.Serializable;

public class RoomMeta implements Serializable {
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

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
