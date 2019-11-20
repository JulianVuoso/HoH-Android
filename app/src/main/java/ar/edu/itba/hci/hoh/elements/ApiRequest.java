package ar.edu.itba.hci.hoh.elements;

import androidx.lifecycle.LiveData;

public class ApiRequest<T> {
    private String uuid;
    private LiveData<Result<T>> liveData;

    public ApiRequest(String uuid, LiveData<Result<T>> liveData) {
        this.uuid = uuid;
        this.liveData = liveData;
    }

    public String getUuid() {
        return uuid;
    }

    public LiveData<Result<T>> getLiveData() {
        return liveData;
    }
}
