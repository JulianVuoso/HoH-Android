package ar.edu.itba.hci.hoh.ui.room;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.elements.Routine;

public class RoomViewModel extends ViewModel {
    private LiveData<Result<ArrayList<Device>>> devices;
    private Room room;

    public RoomViewModel() {
        super();
    }

    public void setRoom(Room room) {
        this.room = room;
        this.devices = MyApplication.getInstance().getDeviceRepository().getDevicesFromRoom(room.getId());
    }

    LiveData<ArrayList<Device>> getDevicesFromRoom() {
        return Transformations.map(this.devices, new Function<Result<ArrayList<Device>>, ArrayList<Device>>() {
            @Override
            public ArrayList<Device> apply(Result<ArrayList<Device>> result) {
                Error error = result.getError();
                if (error != null)
                    MyApplication.makeToast(error.getDescription().get(0));
                return result.getResult();
            }
        });
    }

    LiveData<Boolean> modifyRoom(Room room) {
        LiveData<Result<Boolean>> modifyResult = MyApplication.getInstance().getRoomRepository().modifyRoom(room);
        return Transformations.map(modifyResult, new Function<Result<Boolean>, Boolean>() {
            @Override
            public Boolean apply(Result<Boolean> result) {
                Error error = result.getError();
                if (error != null)
                    MyApplication.makeToast(error.getDescription().get(0));
                return result.getResult();
            }
        });
    }
}
