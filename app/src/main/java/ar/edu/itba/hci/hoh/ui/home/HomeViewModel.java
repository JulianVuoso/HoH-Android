package ar.edu.itba.hci.hoh.ui.home;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.ApiRequest;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.elements.Routine;
import ar.edu.itba.hci.hoh.ui.RequestViewModel;

public class HomeViewModel extends RequestViewModel {

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
        ApiRequest<ArrayList<Object>> execRequest = MyApplication.getInstance().getRoutineRepository().execRoutine(id);
        requestTags.add(execRequest.getUuid());
        return Transformations.map(execRequest.getLiveData(), MyApplication.getTransformFunction());
    }

    void reloadDevices() {
        ApiRequest<ArrayList<Device>> deviceRequest = MyApplication.getInstance().getDeviceRepository().getDevices();
        requestTags.add(deviceRequest.getUuid());
        this.devices = deviceRequest.getLiveData();
    }

    void reloadRooms() {
        ApiRequest<ArrayList<Room>> roomRequest = MyApplication.getInstance().getRoomRepository().getRooms();
        requestTags.add(roomRequest.getUuid());
        this.rooms = roomRequest.getLiveData();
    }

    void reloadRoutines() {
        ApiRequest<ArrayList<Routine>> routineRequest = MyApplication.getInstance().getRoutineRepository().getRoutines();
        requestTags.add(routineRequest.getUuid());
        this.routines = routineRequest.getLiveData();
    }
}