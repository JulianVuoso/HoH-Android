package ar.edu.itba.hci.hoh.ui.home;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.elements.Routine;

public class HomeViewModel extends ViewModel {

    private LiveData<Result<ArrayList<Device>>> devices;
    private LiveData<Result<ArrayList<Room>>> rooms;
    private LiveData<Result<ArrayList<Routine>>> routines;

    public HomeViewModel() {
        super();
        reloadDevices();
        reloadRooms();
        reloadRoutines();
    }

    LiveData<ArrayList<Device>> getDevices() {
        return Transformations.map(this.devices, new Function<Result<ArrayList<Device>>, ArrayList<Device>>() {
            @Override
            public ArrayList<Device> apply(Result<ArrayList<Device>> result) {
                Error error = result.getError();
                if (error != null)
                    MyApplication.makeToast(error.getDescription().get(0));
                return result.getResult();
            }
        });
    }

    LiveData<ArrayList<Room>> getRooms() {
        return Transformations.map(this.rooms, new Function<Result<ArrayList<Room>>, ArrayList<Room>>() {
            @Override
            public ArrayList<Room> apply(Result<ArrayList<Room>> result) {
                Error error = result.getError();
                if (error != null)
                    MyApplication.makeToast(error.getDescription().get(0));
                return result.getResult();
            }
        });
    }

    LiveData<ArrayList<Routine>> getRoutines() {
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

    LiveData<Boolean> execRoutine(String id) {
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

    void reloadDevices() {
        this.devices = MyApplication.getInstance().getDeviceRepository().getDevices();
    }

    void reloadRooms() {
        this.rooms = MyApplication.getInstance().getRoomRepository().getRooms();
    }

    void reloadRoutines() {
        this.routines = MyApplication.getInstance().getRoutineRepository().getRoutines();
    }
}