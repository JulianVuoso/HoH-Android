package ar.edu.itba.hci.hoh.ui.room;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.ApiRequest;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.elements.Routine;
import ar.edu.itba.hci.hoh.ui.RequestViewModel;

public class RoomViewModel extends RequestViewModel {
    private LiveData<Result<ArrayList<Device>>> devices;
    private Room room;

    public RoomViewModel() {
        super();
    }

    public void setRoom(Room room) {
        this.room = room;
        ApiRequest<ArrayList<Device>> deviceRequest = MyApplication.getInstance().getDeviceRepository().getDevicesFromRoom(room.getId());
        requestTags.add(deviceRequest.getUuid());
        this.devices = deviceRequest.getLiveData();
    }

    LiveData<ArrayList<Device>> getDevicesFromRoom() {
        return Transformations.map(this.devices, MyApplication.getTransformFunction());
    }

    LiveData<Boolean> modifyRoom(Room room) {
        ApiRequest<Boolean> modifyRequest = MyApplication.getInstance().getRoomRepository().modifyRoom(room);
        requestTags.add(modifyRequest.getUuid());
        return Transformations.map(modifyRequest.getLiveData(), MyApplication.getTransformFunction());
    }
}
