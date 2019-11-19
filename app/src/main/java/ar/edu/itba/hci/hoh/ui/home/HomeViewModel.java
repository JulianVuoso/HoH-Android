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
        return Transformations.map(this.devices, MyApplication.getTransformFunction());
    }

    LiveData<ArrayList<Room>> getRooms() {
        return Transformations.map(this.rooms, MyApplication.getTransformFunction());
    }

    LiveData<ArrayList<Routine>> getRoutines() {
        return Transformations.map(this.routines, MyApplication.getTransformFunction());
    }

    LiveData<ArrayList<Object>> execRoutine(String id) {
        LiveData<Result<ArrayList<Object>>> execResult = MyApplication.getInstance().getRoutineRepository().execRoutine(id);
        return Transformations.map(execResult, MyApplication.getTransformFunction());
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