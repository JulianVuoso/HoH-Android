package ar.edu.itba.hci.hoh.ui.devices;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Category;
import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.device.DeviceFragment;


public class DevicesFragment extends Fragment {

    private DevicesViewModel devicesViewModel;

    private RecyclerView rvCategories;
    private GridLayoutManager gridLayoutManager;
    private DevicesAdapter adapter;

    private List<Category> data = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = ViewModelProviders.of(this).get(DevicesViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_devices, container, false);

        // METODO DE PRUEBA PARA PONER DISPOSITIVOS EN LA LISTA
        fillData();

        rvCategories = root.findViewById(R.id.rv_categories);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        gridLayoutManager = new GridLayoutManager(this.getContext(), 3);
        rvCategories.setLayoutManager(gridLayoutManager);
        adapter = new DevicesAdapter(data, new DevicesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                DevicesFragmentDirections.ActionSelectCategory action = DevicesFragmentDirections.actionSelectCategory(category, category.getName());
                Navigation.findNavController(root).navigate(action);
            }
        });
        rvCategories.setAdapter(adapter);

        return root;
    }

    private void fillData() {
        data.add(new Category("Lights", R.drawable.ic_light_black_60dp, "lamp"));
        data.add(new Category("Doors & Blinds", R.drawable.ic_door_black_60dp, "door", "blinds"));
        data.add(new Category("Air Conditioning", R.drawable.ic_door_black_60dp, "ac"));
        data.add(new Category("Appliances", R.drawable.ic_fridge_black_60dp, "refrigerator", "oven"));
        data.add(new Category("Entertainment", R.drawable.ic_entertainment_black_60dp, "speaker"));
    }
}