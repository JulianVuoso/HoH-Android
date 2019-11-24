package ar.edu.itba.hci.hoh.elements;

import java.io.Serializable;

public class DeviceState implements Serializable {
    private String status;
    /* Light: status + */
    private String color;
    private int brightness;
    /* Door: status + */
    private String lock;
    /* Blind: status + */
    private int level;
    /* Ac: status + */
    private int temperature;
    private String mode;
    private String verticalSwing;
    private String horizontalSwing;
    private String fanSpeed;
    /* Speaker: status + */
    private int volume;
    private String genre;
    private Song song;
    /* Refrigerator: NO STATE + temperature + mode + */
    private int freezerTemperature;
    /* Oven: status + temperature + */
    private String heat;
    private String grill;
    private String convection;

    /* LIGHT */
    public DeviceState(String status, String color, int brightness) {
        this.status = status;
        this.color = color;
        this.brightness = brightness;
    }

    /* DOOR */
    public DeviceState(String status, String lock) {
        this.status = status;
        this.lock = lock;
    }

    /* BLIND */
    public DeviceState(String status, int level) {
        this.status = status;
        this.level = level;
    }

    /* AC */
    public DeviceState(String status, int temperature, String mode, String verticalSwing, String horizontalSwing, String fanSpeed) {
        this.status = status;
        this.temperature = temperature;
        this.mode = mode;
        this.verticalSwing = verticalSwing;
        this.horizontalSwing = horizontalSwing;
        this.fanSpeed = fanSpeed;
    }

    /* SPEAKER */
    public DeviceState(String status, int volume, String genre, Song song) {
        this.status = status;
        this.volume = volume;
        this.genre = genre;
        this.song = song;
    }

    /* REFRIGERATOR */
    public DeviceState(int temperature, String mode, int freezerTemperature) {
        this.temperature = temperature;
        this.mode = mode;
        this.freezerTemperature = freezerTemperature;
    }

    /* OVEN */
    public DeviceState(String status, int temperature, String heat, String grill, String convection) {
        this.status = status;
        this.temperature = temperature;
        this.heat = heat;
        this.grill = grill;
        this.convection = convection;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public int getBrightness() {
        return brightness;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getMode() {
        return mode;
    }

    public String getVerticalSwing() {
        return verticalSwing;
    }

    public String getHorizontalSwing() {
        return horizontalSwing;
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public int getVolume() {
        return volume;
    }

    public String getGenre() {
        return genre;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getFreezerTemperature() {
        return freezerTemperature;
    }

    public String getHeat() {
        return heat;
    }

    public String getGrill() {
        return grill;
    }

    public String getConvection() {
        return convection;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setVerticalSwing(String verticalSwing) {
        this.verticalSwing = verticalSwing;
    }

    public void setHorizontalSwing(String horizontalSwing) {
        this.horizontalSwing = horizontalSwing;
    }

    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setFreezerTemperature(int freezerTemperature) {
        this.freezerTemperature = freezerTemperature;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public void setGrill(String grill) {
        this.grill = grill;
    }

    public void setConvection(String convection) {
        this.convection = convection;
    }

    public class Song {
        private String title;
        private String artist;
        private String album;
        private String duration;
        private String progress;

        public Song(String title, String artist, String album, String duration, String progress) {
            this.title = title;
            this.artist = artist;
            this.album = album;
            this.duration = duration;
            this.progress = progress;
        }

        public String getTitle() {
            return title;
        }

        public String getArtist() {
            return artist;
        }

        public String getAlbum() {
            return album;
        }

        public String getDuration() {
            return duration;
        }

        public String getProgress() {
            return progress;
        }
    }

    @Override
    public String toString() {
        return "DeviceState{" +
                "status='" + status + '\'' +
                ", color='" + color + '\'' +
                ", brightness=" + brightness +
                ", lock='" + lock + '\'' +
                ", level=" + level +
                ", temperature=" + temperature +
                ", mode='" + mode + '\'' +
                ", verticalSwing='" + verticalSwing + '\'' +
                ", horizontalSwing='" + horizontalSwing + '\'' +
                ", fanSpeed='" + fanSpeed + '\'' +
                ", volume=" + volume +
                ", genre='" + genre + '\'' +
                ", song=" + song +
                ", freezerTemperature=" + freezerTemperature +
                ", heat='" + heat + '\'' +
                ", grill='" + grill + '\'' +
                ", convection='" + convection + '\'' +
                '}';
    }
}
