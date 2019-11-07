package ar.edu.itba.hci.hoh.ui.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.R;

public class DeviceFragment extends Fragment {

    private DeviceViewModel devicesViewModel;

    private RecyclerView rvDevices;
    private GridLayoutManager gridLayoutManager;
    private DeviceAdapter adapter;

    List<Device> data = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_device, container, false);

        // METODO DE PRUEBA PARA PONER DISPOSITIVOS EN LA LISTA
        fillData();

        rvDevices = root.findViewById(R.id.rv_devices_dev);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        gridLayoutManager = new GridLayoutManager(this.getContext(), 3, GridLayoutManager.VERTICAL, false);
//        new GridLayoutManager(this.getContext(), 3, )

        rvDevices.setLayoutManager(gridLayoutManager);
        adapter = new DeviceAdapter(data);
        rvDevices.setAdapter(adapter);

        return root;
    }

    private void fillData() {
        DeviceType lamp = new DeviceType("1234", "lamp");
        DeviceType speaker = new DeviceType("2345", "speaker");
        Room living = new Room("1122", "Living Room", "...", false);
        Room kitchen = new Room("2233", "Kitchen", "...", false);
        data.add(new Device("1234", "Table Lamp", lamp, kitchen, false));
        data.add(new Device("1234", "Front Light", lamp, living, false));
        data.add(new Device("1234", "Garden Light", lamp, kitchen, false));

        data.add(new Device("1234", "Table Speaker", speaker, kitchen, false));
        data.add(new Device("1234", "Juli's Speaker", speaker, living, false));
    }
}