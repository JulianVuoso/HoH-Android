package ar.edu.itba.hci.hoh.ui.device;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.elements.Category;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class DeviceFragment extends Fragment {

    private DeviceViewModel devicesViewModel;

    private RecyclerView rvDevices;
    private GridLayoutManager gridLayoutManager;
    private DeviceListAdapter adapter;

    private List<Device> data = new ArrayList<>();

    private Category category;

    private List<String> requestTag = new ArrayList<>();

    private CardView emptyCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_device, container, false);

        if (getArguments() != null)
            category = DeviceFragmentArgs.fromBundle(getArguments()).getCategory();

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


        // METODO DE PRUEBA PARA PONER DISPOSITIVOS EN LA LISTA
//        fillData();

        emptyCard = root.findViewById(R.id.empty_device_card);
        TextView tvEmptyRoom = emptyCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_device_list);

        if (category != null) {
            for (final DeviceType type : category.getTypes()) {
                // TODO: SACAR CONTEXT
                requestTag.add(Api.getInstance(this.getContext()).getDevicesFromType(type.getId(), new Response.Listener<ArrayList<Device>>() {
                    @Override
                    public void onResponse(ArrayList<Device> response) {
                        for (Device device : response)
                            device.setType(type);
                        data.addAll(response);
                        if (!data.isEmpty())
                            emptyCard.setVisibility(View.GONE);
                        adapter.updatedDataSet();
                        Log.v(MainActivity.LOG_TAG, "ACTUALICE DISPOSITIVOS");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: VER QUE HACER CON ERROR
                        Log.e(MainActivity.LOG_TAG, String.format("ERROR AL ACTUALIZAR DISPOSITIVOS. El error es %s", error.toString()));
                    }
                }));
            }
        }

        return root;
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