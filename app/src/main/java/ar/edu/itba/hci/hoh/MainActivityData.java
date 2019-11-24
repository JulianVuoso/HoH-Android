package ar.edu.itba.hci.hoh;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.ApiRequest;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Room;

public class MainActivityData {
    private LiveData<Result<ArrayList<DeviceType>>> types;
    private List<String> requestTags = new ArrayList<>();

    public MainActivityData() {
        super();
        reloadTypes();
    }

    LiveData<ArrayList<DeviceType>> getDeviceTypes() {
        return Transformations.map(this.types, MyApplication.getTransformFunction());
    }

    void reloadTypes() {
        ApiRequest<ArrayList<DeviceType>> typeRequest = MyApplication.getInstance().getDeviceTypeRepository().getDeviceTypes();
        requestTags.add(typeRequest.getUuid());
        this.types = typeRequest.getLiveData();
    }

    LiveData<Room> getRoom(String id) {
        ApiRequest<Room> getRequest = MyApplication.getInstance().getRoomRepository().getRoom(id);
        requestTags.add(getRequest.getUuid());
        return Transformations.map(getRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    void cancelRequests() {
        for (String uuid : requestTags)
            MyApplication.getInstance().getDeviceTypeRepository().cancelRequest(uuid);
    }
}
