package ar.edu.itba.hci.hoh.Elements;

public class RoomMeta {
    private String image;
    private boolean favorite;

    public RoomMeta(String image, boolean favorite) {
        this.image = image;
        this.favorite = favorite;
    }

    public String getImage() {
        return image;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
