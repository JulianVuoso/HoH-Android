package ar.edu.itba.hci.hoh.dialogs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.elements.ApiRequest;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Room;

class DialogData {
    private List<String> requestTags = new ArrayList<>();

    DialogData() {
        super();
    }

    LiveData<ArrayList<Object>> execRoutine(String id) {
        ApiRequest<ArrayList<Object>> execRequest = MyApplication.getInstance().getRoutineRepository().execRoutine(id);
        requestTags.add(execRequest.getUuid());
        return Transformations.map(execRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    LiveData<Boolean> modifyDevice(Device device) {
        ApiRequest<Boolean> modifyRequest = MyApplication.getInstance().getDeviceRepository().modifyDevice(device);
        requestTags.add(modifyRequest.getUuid());
        return Transformations.map(modifyRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    LiveData<String> execAction(String id, String action, String[] param) {
        ApiRequest<String> execRequest = MyApplication.getInstance().getDeviceRepository().execAction(id, action, param);
        requestTags.add(execRequest.getUuid());
        return Transformations.map(execRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    LiveData<Integer> execAction(String id, String action, Integer[] param) {
        ApiRequest<Integer> execRequest = MyApplication.getInstance().getDeviceRepository().execAction(id, action, param);
        requestTags.add(execRequest.getUuid());
        return Transformations.map(execRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    LiveData<Boolean> execAction(String id, String action) {
        ApiRequest<Boolean> execRequest = MyApplication.getInstance().getDeviceRepository().execAction(id, action);
        requestTags.add(execRequest.getUuid());
        return Transformations.map(execRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    LiveData<Device> getDevice(String id) {
        ApiRequest<Device> getRequest = MyApplication.getInstance().getDeviceRepository().getDevice(id);
        requestTags.add(getRequest.getUuid());
        return Transformations.map(getRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    void cancelRequests() {
        for (String uuid : requestTags)
            MyApplication.getInstance().getDeviceRepository().cancelRequest(uuid);
        requestTags.clear();
    }
}
