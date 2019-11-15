package ar.edu.itba.hci.hoh.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.elements.Result;

public class DeviceTypeRepository extends Repository {
    private static DeviceTypeRepository instance;

    private DeviceTypeRepository(Application application) {
        super(application);
    }

    public static synchronized DeviceTypeRepository getInstance(Application application) {
        if (instance == null) {
            instance = new DeviceTypeRepository(application);
        }
        return instance;
    }

    public LiveData<Result<DeviceType>> getDeviceType(String id) {
        final MutableLiveData<Result<DeviceType>> result = new MutableLiveData<>();
        this.api.getDeviceType(id, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<ArrayList<DeviceType>>> getDeviceTypes() {
        final MutableLiveData<Result<ArrayList<DeviceType>>> result = new MutableLiveData<>();
        this.api.getDeviceTypes(getListener(result), getErrorListener(this.api, result));
        return result;
    }
}
