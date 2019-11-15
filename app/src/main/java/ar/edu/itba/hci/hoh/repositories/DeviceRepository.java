package ar.edu.itba.hci.hoh.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Routine;

public class DeviceRepository extends Repository {
    private static DeviceRepository instance;

    private DeviceRepository(Application application) {
        super(application);
    }

    public static synchronized DeviceRepository getInstance(Application application) {
        if (instance == null) {
            instance = new DeviceRepository(application);
        }
        return instance;
    }

    public LiveData<Result<Device>> addDevice(Device device) {
        final MutableLiveData<Result<Device>> result = new MutableLiveData<>();
        this.api.addDevice(device, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<Boolean>> modifyDevice(Device device) {
        final MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        this.api.modifyDevice(device, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<Boolean>> deleteDevice(String id) {
        final MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        this.api.deleteDevice(id, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<Device>> getDevice(String id) {
        final MutableLiveData<Result<Device>> result = new MutableLiveData<>();
        this.api.getDevice(id, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<ArrayList<Device>>> getDevices() {
        final MutableLiveData<Result<ArrayList<Device>>> result = new MutableLiveData<>();
        this.api.getDevices(getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<ArrayList<Device>>> getDevicesFromType(String id) {
        final MutableLiveData<Result<ArrayList<Device>>> result = new MutableLiveData<>();
        this.api.getDevicesFromType(id, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<ArrayList<Device>>> getDevicesFromRoom(String id) {
        final MutableLiveData<Result<ArrayList<Device>>> result = new MutableLiveData<>();
        this.api.getDevicesFromRoom(id, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    // TODO: DEVUELVE STRING O BOOLEAN, UNIFICAR con OBJECT
    public LiveData<Result<Boolean>> execAction(String id, String action, String[] param) {
        final MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        this.api.execAction(id, action, param, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<Boolean>> execAction(String id, String action) {
        final MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        this.api.execAction(id, action, getListener(result), getErrorListener(this.api, result));
        return result;
    }
}
