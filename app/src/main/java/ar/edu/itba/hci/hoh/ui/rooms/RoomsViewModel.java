package ar.edu.itba.hci.hoh.ui.rooms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Room;

public class RoomsViewModel extends ViewModel {

    private LiveData<Result<ArrayList<Room>>> rooms;

    public RoomsViewModel() {
        super();
        reloadRooms();
    }

    LiveData<ArrayList<Room>> getRooms() {
        return Transformations.map(this.rooms, (result) -> {
            Error error = result.getError();
            if (error != null)
                MyApplication.makeToast(error.getDescription().get(0));
            return result.getResult();
        });
    }

    void reloadRooms() {
        this.rooms = MyApplication.getInstance().getRoomRepository().getRooms();
    }
}