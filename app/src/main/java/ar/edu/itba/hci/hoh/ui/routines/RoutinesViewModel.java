package ar.edu.itba.hci.hoh.ui.routines;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Routine;

public class RoutinesViewModel extends ViewModel {

    private LiveData<Result<ArrayList<Routine>>> routines;

    public RoutinesViewModel() {
        super();
        reloadRoutines();
    }

    LiveData<ArrayList<Routine>> getRoutines() {
        return Transformations.map(this.routines, MyApplication.getTransformFunction());
    }

    LiveData<Boolean> execRoutine(String id) {
        LiveData<Result<Boolean>> execResult = MyApplication.getInstance().getRoutineRepository().execRoutine(id);
        return Transformations.map(execResult, MyApplication.getTransformFunction());
    }

    void reloadRoutines() {
        this.routines = MyApplication.getInstance().getRoutineRepository().getRoutines();
    }
}