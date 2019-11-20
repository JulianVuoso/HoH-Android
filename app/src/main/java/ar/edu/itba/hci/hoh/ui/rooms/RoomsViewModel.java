package ar.edu.itba.hci.hoh.ui.rooms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.ApiRequest;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.ui.RequestViewModel;

public class RoomsViewModel extends RequestViewModel {

    private LiveData<Result<ArrayList<Room>>> rooms;

    public RoomsViewModel() {
        super();
        reloadRooms();
    }

    LiveData<ArrayList<Room>> getRooms() {
        return Transformations.map(this.rooms, MyApplication.getTransformFunction());
    }

    void reloadRooms() {
        ApiRequest<ArrayList<Room>> roomRequest = MyApplication.getInstance().getRoomRepository().getRooms();
        requestTags.add(roomRequest.getUuid());
        this.rooms = roomRequest.getLiveData();
    }
}