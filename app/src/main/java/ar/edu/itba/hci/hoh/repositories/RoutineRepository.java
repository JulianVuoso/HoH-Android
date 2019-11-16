package ar.edu.itba.hci.hoh.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

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

    public LiveData<Result<Routine>> getRoutine(String id) {
        final MutableLiveData<Result<Routine>> result = new MutableLiveData<>();
        this.api.getRoutine(id, getListener(result), getErrorListener(this.api, result));
        return result;
    }

    public LiveData<Result<ArrayList<Routine>>> getRoutines() {
        final MutableLiveData<Result<ArrayList<Routine>>> result = new MutableLiveData<>();
        this.api.getRoutines(getListener(result), getErrorListener(this.api, result));
        return result;
    }

    // TODO: CAMBIAR A ARRAY LIST DE OBJECT, DEVUELVE LISTA DE BOOLEAN Y STRINGS
    public LiveData<Result<Boolean>> execRoutine(String id) {
        final MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        this.api.execRoutine(id, getListener(result), getErrorListener(this.api, result));
        return result;
    }
}
