package ar.edu.itba.hci.hoh.ui;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.MyApplication;

public abstract class RequestViewModel extends ViewModel {
    protected List<String> requestTags = new ArrayList<>();

    public RequestViewModel() {
        super();
    }

    public void cancelRequests() {
        // Any repository can be used to cancel requests, method declared in Repository
        for (String uuid : requestTags)
            MyApplication.getInstance().getDeviceRepository().cancelRequest(uuid);
    }
}
