package ar.edu.itba.hci.hoh.notifications;

import androidx.annotation.NonNull;
import androidx.room.*;

import java.util.Objects;

@Entity(tableName = "tuple")
public class Tuple {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "state")
    private String state;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    public Tuple(String id, String state, String name) {
        this.id = id;
        this.state = state;
        this.name = name;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setState(@NonNull String state) {
        this.state = state;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getState() {
        return state;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;
        Tuple device = (Tuple) o;
        return id.equals(device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}