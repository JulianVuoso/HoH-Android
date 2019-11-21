package ar.edu.itba.hci.hoh.dialogs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.elements.ApiRequest;

public class DialogData {
    private List<String> requestTags = new ArrayList<>();

    public DialogData() {
        super();
    }

    LiveData<ArrayList<Object>> execRoutine(String id) {
        ApiRequest<ArrayList<Object>> execRequest = MyApplication.getInstance().getRoutineRepository().execRoutine(id);
        requestTags.add(execRequest.getUuid());
        return Transformations.map(execRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    void cancelRequests() {
        for (String uuid : requestTags)
            MyApplication.getInstance().getDeviceRepository().cancelRequest(uuid);
        requestTags.clear();
    }
}
