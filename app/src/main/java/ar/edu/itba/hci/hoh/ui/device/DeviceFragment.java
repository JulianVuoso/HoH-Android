package ar.edu.itba.hci.hoh.ui.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Category;
import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class DeviceFragment extends Fragment {

    private DeviceViewModel devicesViewModel;

    private RecyclerView rvDevices;
    private GridLayoutManager gridLayoutManager;
    private DeviceListAdapter adapter;

    private List<Device> data = new ArrayList<>();

    private Category category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_device, container, false);

        root.setLabelFor(R.id.navigation_device);

        if (getArguments() != null)
            category = DeviceFragmentArgs.fromBundle(getArguments()).getCategory();

        // METODO DE PRUEBA PARA PONER DISPOSITIVOS EN LA LISTA
        fillData();

        rvDevices = root.findViewById(R.id.rv_list_category_devices);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        // SI PONGO ESTO DE ABAJO, QUEDA PIOLA PARA CUANDO ROTO. PODRIAMOS VER DE ADAPTAR EL SPAN COUNT
//        gridLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
//        rvDevices.setLayoutManager(gridLayoutManager);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rvDevices.setLayoutManager(manager);
        adapter = new DeviceListAdapter(data, new OnItemClickListener<Device>() {
            @Override
            public void onItemClick(Device element) {
                // TODO: OPEN DIALOG
            }
        });
        rvDevices.setAdapter(adapter);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: VER COMO SETEAR EL BOTON SEGUN ESTADO DE FAVORITO (ROOM)
//        menu.getItem(R.id.action_favorite).setIcon(R.drawable.ic_star_white_24dp);
        inflater.inflate(R.menu.appbar_room_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != R.id.action_favorite)
            return super.onOptionsItemSelected(item);
        // TODO: TOGGLEAR FAVORITES BIEN
        item.setIcon(R.drawable.ic_star_border_white_24dp);
        return true;
    }

    private void fillData() {
        List<Device> auxList = new ArrayList<>();

        DeviceType lamp = new DeviceType("1234", "lamp");
        DeviceType speaker = new DeviceType("2345", "speaker");
        Room living = new Room("1122", "Living Room", "...", false);
        Room kitchen = new Room("2233", "Kitchen", "...", false);
        auxList.add(new Device("1234", "Table Lamp", lamp, kitchen, false));
        auxList.add(new Device("1234", "Front Light", lamp, living, false));
        auxList.add(new Device("1234", "Garden Light", lamp, kitchen, false));

        auxList.add(new Device("1234", "Table Speaker", speaker, kitchen, false));
        auxList.add(new Device("1234", "Juli's Speaker", speaker, living, false));

        for (Device dev : auxList) {
            if (category != null && category.checkDeviceType(dev.getType()))
                data.add(dev);
        }
    }
}