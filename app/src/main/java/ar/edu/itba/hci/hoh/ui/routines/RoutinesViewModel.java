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

    public LiveData<ArrayList<Routine>> getRoutines() {
        return Transformations.map(this.routines, new Function<Result<ArrayList<Routine>>, ArrayList<Routine>>() {
            @Override
            public ArrayList<Routine> apply(Result<ArrayList<Routine>> result) {
                Error error = result.getError();
                if (error != null)
                    MyApplication.makeToast(error.getDescription().get(0));
                return result.getResult();
            }
        });
    }

    public LiveData<Boolean> execRoutine(String id) {
        LiveData<Result<Boolean>> execResult = MyApplication.getInstance().getRoutineRepository().execRoutine(id);
        return Transformations.map(execResult, new Function<Result<Boolean>, Boolean>() {
            @Override
            public Boolean apply(Result<Boolean> result) {
                Error error = result.getError();
                if (error != null)
                    MyApplication.makeToast(error.getDescription().get(0));
                return result.getResult();
            }
        });
    }

    void reloadRoutines() {
        this.routines = MyApplication.getInstance().getRoutineRepository().getRoutines();
    }
}