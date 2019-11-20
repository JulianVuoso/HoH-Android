package ar.edu.itba.hci.hoh.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.elements.ApiRequest;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.elements.Routine;

public class RoutineRepository extends Repository {
    private static RoutineRepository instance;

    private RoutineRepository(Application application) {
        super(application);
    }

    public static synchronized RoutineRepository getInstance(Application application) {
        if (instance == null) {
            instance = new RoutineRepository(application);
        }
        return instance;
    }

    public ApiRequest<Routine> getRoutine(String id) {
        final MutableLiveData<Result<Routine>> result = new MutableLiveData<>();
        String uuid = this.api.getRoutine(id, getListener(result), getErrorListener(this.api, result));
        return new ApiRequest<>(uuid, result);
    }

    public ApiRequest<ArrayList<Routine>> getRoutines() {
        final MutableLiveData<Result<ArrayList<Routine>>> result = new MutableLiveData<>();
        String uuid = this.api.getRoutines(getListener(result), getErrorListener(this.api, result));
        return new ApiRequest<>(uuid, result);
    }

    public ApiRequest<ArrayList<Object>> execRoutine(String id) {
        final MutableLiveData<Result<ArrayList<Object>>> result = new MutableLiveData<>();
        String uuid = this.api.execRoutine(id, getListener(result), getErrorListener(this.api, result));
        return new ApiRequest<>(uuid, result);
    }
}
