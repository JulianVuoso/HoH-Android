package ar.edu.itba.hci.hoh.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

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

    public LiveData<Result<Room>> addRoom(Room room) {
        final MutableLiveData<Result<Room>> result = new MutableLiveData<>();
        this.api.addRoom(room, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<Boolean>> modifyRoom(Room room) {
        final MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        this.api.modifyRoom(room, getListener(result), getErrorListener(api, result));
        return result;
    }

    public LiveData<Result<Boolean>> deleteRoom(String id) {
        final MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        this.api.deleteRoom(id, getListener(result), getErrorListener(api, result));
        return result;
    }

    public LiveData<Result<Room>> getRoom(String id) {
        final MutableLiveData<Result<Room>> result = new MutableLiveData<>();
        this.api.getRoom(id, getListener(result), getErrorListener(api, result));
        return result;
    }

    public LiveData<Result<ArrayList<Room>>> getRooms() {
        final MutableLiveData<Result<ArrayList<Room>>> result = new MutableLiveData<>();
        this.api.getRooms(getListener(result), getErrorListener(api, result));
        return result;
    }
}
