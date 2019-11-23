package ar.edu.itba.hci.hoh.ui.device;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.RestartListener;
import ar.edu.itba.hci.hoh.dialogs.DialogCreator;
import ar.edu.itba.hci.hoh.elements.Category;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class DeviceFragment extends Fragment {

    private DeviceViewModel deviceViewModel;

    private RecyclerView rvDevices;
    private GridLayoutManager gridLayoutManager;
    private DeviceListAdapter adapter;

    private List<Device> data = new ArrayList<>();

    private Category category;

    private List<String> requestTag = new ArrayList<>();

    private CardView emptyCard;
    private ProgressBar loading;

    private RestartListener restartListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_device, container, false);

        if (getArguments() != null)
            category = DeviceFragmentArgs.fromBundle(getArguments()).getCategory();

        rvDevices = root.findViewById(R.id.rv_list_category_devices);
//        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        GridLayoutManager manager;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        if ((displayMetrics.widthPixels / displayMetrics.density) > 800)
            manager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
        else
            manager = new GridLayoutManager(this.getContext(), 1, GridLayoutManager.VERTICAL, false);
        rvDevices.setLayoutManager(manager);
        adapter = new DeviceListAdapter(data, element -> {
            AlertDialog dialog = DialogCreator.createDialog(this, element);
            if (dialog != null) {
                dialog.setOnDismissListener(dialog1 -> {
                    DialogCreator.closeDialog();
                    updateFragment();
                });
            }
        });
        rvDevices.setAdapter(adapter);

        emptyCard = root.findViewById(R.id.empty_device_card);
        TextView tvEmptyRoom = emptyCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_device_list);
        loading = root.findViewById(R.id.device_loading_bar);

        if (category != null) {
            for (final DeviceType type : category.getTypes()) {
                deviceViewModel.addDeviceType(type);
            }
            getDevicesList();
        }

        restartListener = this::updateFragment;
        MainActivity.setRestartListener(restartListener);

        return root;
    }

    private void updateFragment() {
        deviceViewModel.reloadDevices();
        getDevicesList();
    }

    private void getDevicesList() {
        data.clear();
        adapter.clearDataSet();
        deviceViewModel.getDevicesFromCategory().observe(this, devices -> {
            if (devices != null) {
                data.clear();
                adapter.clearDataSet();
                devices.sort(Device.getRoomComparator());
                data.addAll(devices);
            }
            loading.setVisibility(View.GONE);
            if (!data.isEmpty())
                emptyCard.setVisibility(View.GONE);
            else
                emptyCard.setVisibility(View.VISIBLE);
            adapter.updatedDataSet();
            Log.v(MainActivity.LOG_TAG, "ACTUALICE DISPOSITIVOS");
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        deviceViewModel.cancelRequests();
        DialogCreator.closeDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.removeRestartListener(restartListener);
    }
}