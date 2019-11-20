package ar.edu.itba.hci.hoh.ui.routines;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.ApiRequest;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Routine;
import ar.edu.itba.hci.hoh.ui.RequestViewModel;

public class RoutinesViewModel extends RequestViewModel {

    private LiveData<Result<ArrayList<Routine>>> routines;

    public RoutinesViewModel() {
        super();
        reloadRoutines();
    }

    LiveData<ArrayList<Routine>> getRoutines() {
        return Transformations.map(this.routines, MyApplication.getTransformFunction());
    }

    LiveData<ArrayList<Object>> execRoutine(String id) {
        ApiRequest<ArrayList<Object>> execRequest = MyApplication.getInstance().getRoutineRepository().execRoutine(id);
        requestTags.add(execRequest.getUuid());
        return Transformations.map(execRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    void reloadRoutines() {
        ApiRequest<ArrayList<Routine>> routineRequest = MyApplication.getInstance().getRoutineRepository().getRoutines();
        requestTags.add(routineRequest.getUuid());
        this.routines = routineRequest.getLiveData();
    }
}