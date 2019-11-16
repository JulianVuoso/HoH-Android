package ar.edu.itba.hci.hoh;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.elements.Result;

public class MainActivityData {
    private LiveData<Result<ArrayList<DeviceType>>> types;

    public MainActivityData() {
        super();
        reloadTypes();
    }

    LiveData<ArrayList<DeviceType>> getDeviceTypes() {
        return Transformations.map(this.types, new Function<Result<ArrayList<DeviceType>>, ArrayList<DeviceType>>() {
            @Override
            public ArrayList<DeviceType> apply(Result<ArrayList<DeviceType>> result) {
                Error error = result.getError();
                if (error != null)
                    MyApplication.makeToast(error.getDescription().get(0));
                return result.getResult();
            }
        });
    }

    void reloadTypes() {
        this.types = MyApplication.getInstance().getDeviceTypeRepository().getDeviceTypes();
    }
}
