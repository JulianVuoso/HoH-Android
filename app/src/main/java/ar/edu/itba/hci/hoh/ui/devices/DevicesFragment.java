package ar.edu.itba.hci.hoh.ui.devices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.hci.hoh.Elements.Category;
import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;
import ar.edu.itba.hci.hoh.ui.device.DeviceFragment;


public class DevicesFragment extends Fragment {

    private DevicesViewModel devicesViewModel;

    private RecyclerView rvCategories;
    private GridLayoutManager gridLayoutManager;
    private static DevicesAdapter adapter;

//    public static List<Category> categories = new ArrayList<>();
//    private static String requestTag;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = ViewModelProviders.of(this).get(DevicesViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_devices, container, false);

        rvCategories = root.findViewById(R.id.rv_categories);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        gridLayoutManager = new GridLayoutManager(this.getContext(), 3, GridLayoutManager.VERTICAL, false);
        rvCategories.setLayoutManager(gridLayoutManager);
        adapter = new DevicesAdapter(MainActivity.categories, new OnItemClickListener<Category>() {
            @Override
            public void onItemClick(Category category) {
                DevicesFragmentDirections.ActionSelectCategory action = DevicesFragmentDirections.actionSelectCategory(category, category.getName());
                Navigation.findNavController(root).navigate(action);
            }
        });
        rvCategories.setAdapter(adapter);

//        if (categories.size() == 0) getCategoryList(this.getContext());

        return root;
    }

    public static void notifyAdapter() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    /*private static void getCategoryList(Context context) {
        requestTag = Api.getInstance(context).getDeviceTypes(new Response.Listener<ArrayList<DeviceType>>() {
            @Override
            public void onResponse(ArrayList<DeviceType> response) {
                Category lights = new Category("Lights", R.drawable.ic_light_black_60dp);
                Category openings = new Category("Doors & Blinds", R.drawable.ic_door_black_60dp);
                Category ac = new Category("Air Conditioning", R.drawable.ic_door_black_60dp); // TODO: CHANGE CATEGORY PIC
                Category appliances = new Category("Appliances", R.drawable.ic_fridge_black_60dp);
                Category entertainment = new Category("Entertainment", R.drawable.ic_entertainment_black_60dp);

                for (DeviceType type : response) {
                    switch (type.getName()) {
                        case "lamp":    lights.addType(type);
                                        break;
                        case "door":
                        case "blinds":  openings.addType(type);
                                        break;
                        case "ac":      ac.addType(type);
                                        break;
                        case "refrigerator":
                        case "oven":    appliances.addType(type);
                                        break;
                        case "speaker": entertainment.addType(type);
                                        break;
                    }
                }
                categories.add(lights);
                categories.add(openings);
                categories.add(ac);
                categories.add(appliances);
                categories.add(entertainment);
                adapter.notifyDataSetChanged();
                Log.v(MainActivity.LOG_TAG, "ACTUALICE CATEGORIAS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                MainActivity.handleError(error);
                // TODO: VER QUE HACER CON ERROR
                Log.e(MainActivity.LOG_TAG, String.format("ERROR AL ACTUALIZAR CATEGORIAS. El error es %s", error.toString()));
            }
        });
    }*/

//    @Override
//    public void onStop() {
//        super.onStop();
//        Api.getInstance(this.getContext()).cancelRequest(requestTag);
//    }
}