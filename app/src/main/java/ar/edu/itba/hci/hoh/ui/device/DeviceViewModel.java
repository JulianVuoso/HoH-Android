package ar.edu.itba.hci.hoh.ui.device;

import android.util.Log;
import android.util.Pair;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.ApiRequest;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Routine;
import ar.edu.itba.hci.hoh.ui.RequestViewModel;

public class DeviceViewModel extends RequestViewModel {

    private MediatorLiveData<Pair<Result<ArrayList<Device>>, DeviceType>> devices;
    private Map<DeviceType, LiveData<Result<ArrayList<Device>>>> typesData = new HashMap<>();

    public DeviceViewModel() {
        super();
        this.devices = new MediatorLiveData<>();
    }

    void addDeviceType(DeviceType type) {
        if (typesData.containsKey(type)) return;
        createTypeLiveData(type);
    }

    private void createTypeLiveData(DeviceType type) {
        LiveData<Result<ArrayList<Device>>> auxDevices = getDevicesFromType(type.getId());
        typesData.put(type, auxDevices);
        this.devices.addSource(auxDevices, value -> this.devices.setValue(new Pair<>(value, type)));
    }

    LiveData<ArrayList<Device>> getDevicesFromCategory() {
        return Transformations.map(this.devices, new Function<Pair<Result<ArrayList<Device>>, DeviceType>, ArrayList<Device>>() {
            @Override
            public ArrayList<Device> apply(Pair<Result<ArrayList<Device>>, DeviceType> result) {
                if (result.first.getResult() != null) {
                    for (Device device : result.first.getResult())
                        device.setType(result.second);
                }
                Error error = result.first.getError();
                if (error != null)
                    MyApplication.makeToast(error);
                return result.first.getResult();
            }
        });
    }

    private LiveData<Result<ArrayList<Device>>> getDevicesFromType(String id) {
        ApiRequest<ArrayList<Device>> deviceRequest = MyApplication.getInstance().getDeviceRepository().getDevicesFromType(id);
        requestTags.add(deviceRequest.getUuid());
        return deviceRequest.getLiveData();
    }

    LiveData<Device> getDevice(String id) {
        ApiRequest<Device> getRequest = MyApplication.getInstance().getDeviceRepository().getDevice(id);
        requestTags.add(getRequest.getUuid());
        return Transformations.map(getRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    void reloadDevices() {
        this.devices = new MediatorLiveData<>();
        for (DeviceType type : typesData.keySet())
            createTypeLiveData(type);
    }
}