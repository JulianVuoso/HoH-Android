package ar.edu.itba.hci.hoh.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.elements.ApiRequest;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Room;

public class RoomRepository extends Repository {
    private static RoomRepository instance;

    private RoomRepository(Application application) {
        super(application);
    }

    public static synchronized RoomRepository getInstance(Application application) {
        if (instance == null) {
            instance = new RoomRepository(application);
        }
        return instance;
    }

    public ApiRequest<Room> addRoom(Room room) {
        final MutableLiveData<Result<Room>> result = new MutableLiveData<>();
        String uuid = this.api.addRoom(room, getListener(result), getErrorListener(this.api, result));
        return new ApiRequest<>(uuid, result);
    }

    public ApiRequest<Boolean> modifyRoom(Room room) {
        final MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        String uuid = this.api.modifyRoom(room, getListener(result), getErrorListener(api, result));
        return new ApiRequest<>(uuid, result);
    }

    public ApiRequest<Boolean> deleteRoom(String id) {
        final MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        String uuid = this.api.deleteRoom(id, getListener(result), getErrorListener(api, result));
        return new ApiRequest<>(uuid, result);
    }

    public ApiRequest<Room> getRoom(String id) {
        final MutableLiveData<Result<Room>> result = new MutableLiveData<>();
        String uuid = this.api.getRoom(id, getListener(result), getErrorListener(api, result));
        return new ApiRequest<>(uuid, result);
    }

    public ApiRequest<ArrayList<Room>> getRooms() {
        final MutableLiveData<Result<ArrayList<Room>>> result = new MutableLiveData<>();
        String uuid = this.api.getRooms(getListener(result), getErrorListener(api, result));
        return new ApiRequest<>(uuid, result);
    }
}
