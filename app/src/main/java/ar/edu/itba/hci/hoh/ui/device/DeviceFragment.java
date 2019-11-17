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

// TODO: COMO APLICO VIEWMODEL ACA? NECESITO APPENDEAR COSAS A LA LISTA, SON VARIOS FETCH JUNTOS
public class DeviceFragment extends Fragment {

    private DeviceViewModel deviceViewModel;

    private RecyclerView rvDevices;
    private GridLayoutManager gridLayoutManager;
    private DeviceListAdapter adapter;

    private List<Device> data = new ArrayList<>();

    private Category category;

    private List<String> requestTag = new ArrayList<>();

    private CardView emptyCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
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

        emptyCard = root.findViewById(R.id.empty_device_card);
        TextView tvEmptyRoom = emptyCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_device_list);

        if (category != null) {
            for (final DeviceType type : category.getTypes()) {
                deviceViewModel.addDeviceType(type);
            }
            getDevicesList();
        }

        return root;
    }

    private void getDevicesList() {
        deviceViewModel.getDevicesFromCategory().observe(this, devices -> {
            if (devices != null)
                data.addAll(devices);
            if (!data.isEmpty())
                emptyCard.setVisibility(View.GONE);
            adapter.updatedDataSet();
            Log.v(MainActivity.LOG_TAG, "ACTUALICE DISPOSITIVOS");
        });
    }

}