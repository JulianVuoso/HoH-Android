package ar.edu.itba.hci.hoh.ui.home;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.elements.Routine;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.LinearLayoutPagerManager;
import ar.edu.itba.hci.hoh.ui.MarginItemDecorator;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;
import ar.edu.itba.hci.hoh.ui.device.DeviceAdapter;
import ar.edu.itba.hci.hoh.ui.rooms.RoomsAdapter;
import ar.edu.itba.hci.hoh.ui.routines.RoutinesAdapter;
import ar.edu.itba.hci.hoh.ui.routines.RoutinesFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView rvFavDevices, rvFavRooms, rvFavRoutines;
    private LinearLayoutManager managerFavDevices, managerFavRooms, managerFavRoutines;
    private DeviceAdapter adapterFavDevices;
    private RoomsAdapter adapterFavRooms;
    private RoutinesAdapter adapterFavRoutines;

    private List<Device> favDevices = new ArrayList<>();
    private List<Room> favRooms = new ArrayList<>();
    private List<Routine> favRoutines = new ArrayList<>();

    private List<String> requestTag = new ArrayList<>();

    private CardView emptyDeviceCard, emptyRoomCard, emptyRoutineCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        /* FAV DEVICES */
        rvFavDevices = root.findViewById(R.id.rv_favorite_devices);
        managerFavDevices = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, getResources().getDimension(R.dimen.device_card_width));
        rvFavDevices.setLayoutManager(managerFavDevices);
        rvFavDevices.addItemDecoration(new MarginItemDecorator((int) getResources().getDimension(R.dimen.home_card_spacing)));
        adapterFavDevices = new DeviceAdapter(new OnItemClickListener<Device>() {
            @Override
            public void onItemClick(Device element) {
                // TODO: OPEN DIALOG
            }
        });
        adapterFavDevices.setDevices(favDevices);
        rvFavDevices.setAdapter(adapterFavDevices);
        getFavDeviceList();
        emptyDeviceCard = root.findViewById(R.id.empty_fav_devices_card);
        TextView tvEmptyDevice = emptyDeviceCard.findViewById(R.id.card_no_element_text);
        tvEmptyDevice.setText(R.string.empty_fav_device_list);

        /* FAV ROOMS */
        rvFavRooms = root.findViewById(R.id.rv_favorite_rooms);
//        managerFavRooms = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, 2);
        managerFavRooms = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, getResources().getDimension(R.dimen.img_card_width));
        rvFavRooms.setLayoutManager(managerFavRooms);
        rvFavRooms.addItemDecoration(new MarginItemDecorator((int) getResources().getDimension(R.dimen.home_card_spacing)));
        adapterFavRooms = new RoomsAdapter(new OnItemClickListener<Room>() {
            @Override
            public void onItemClick(Room room) {
                HomeFragmentDirections.ActionSelectRoom action = HomeFragmentDirections.actionSelectRoom(room, room.getName());
                Navigation.findNavController(root).navigate(action);
            }
        });
        adapterFavRooms.setRooms(favRooms);
        rvFavRooms.setAdapter(adapterFavRooms);
        getFavRoomList();
        emptyRoomCard = root.findViewById(R.id.empty_fav_rooms_card);
        TextView tvEmptyRoom = emptyRoomCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_fav_room_list);

        /* FAV ROUTINES */
        rvFavRoutines = root.findViewById(R.id.rv_favorite_routines);
        managerFavRoutines = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, getResources().getDimension(R.dimen.img_card_width));
        rvFavRoutines.setLayoutManager(managerFavRoutines);
        rvFavRoutines.addItemDecoration(new MarginItemDecorator((int) getResources().getDimension(R.dimen.home_card_spacing)));
        adapterFavRoutines = new RoutinesAdapter(new OnItemClickListener<Routine>() {
            @Override
            public void onItemClick(Routine routine) {
                // TODO: OPEN CONFIRMATION DIALOG TO EXECUTE ROUTINE
                executeRoutine(routine);
            }
        });
        rvFavRoutines.setAdapter(adapterFavRoutines);
        getFavRoutineList();
        emptyRoutineCard = root.findViewById(R.id.empty_fav_routines_card);
        TextView tvEmptyRoutine = emptyRoutineCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoutine.setText(R.string.empty_fav_routine_list);

        return root;
    }

    private void getFavDeviceList() {
        homeViewModel.getDevices().observe(this, devices -> {
            favDevices.clear();
            if (devices != null) {
                for (Device device : devices)
                    if (device.getMeta().isFavorite())
                        favDevices.add(device);
            }
            if (!favDevices.isEmpty())
                emptyDeviceCard.setVisibility(View.GONE);
            adapterFavDevices.notifyDataSetChanged();
            Log.v(MainActivity.LOG_TAG, "ACTUALICE FAV DEVICES");
        });
    }

    private void getFavRoomList() {
        homeViewModel.getRooms().observe(this, rooms -> {
            favRooms.clear();
            if (rooms != null) {
                for (Room room : rooms)
                    if (room.getMeta().isFavorite())
                        favRooms.add(room);
            }
            if (!favRooms.isEmpty())
                emptyRoomCard.setVisibility(View.GONE);
            adapterFavRooms.notifyDataSetChanged();
            Log.v(MainActivity.LOG_TAG, "ACTUALICE FAV Rooms");
        });
    }

    private void getFavRoutineList() {
        homeViewModel.getRoutines().observe(this, routines -> {
            favRoutines.clear();
            if (routines != null) {
                for (Routine routine : routines)
                    if (routine.getMeta().isFavorite())
                        favRoutines.add(routine);
            }
            if (!favRoutines.isEmpty())
                emptyRoutineCard.setVisibility(View.GONE);
            adapterFavRoutines.notifyDataSetChanged();
            Log.v(MainActivity.LOG_TAG, "ACTUALICE FAV Routines");
        });
    }

    private void executeRoutine(Routine routine) {
        homeViewModel.execRoutine(routine.getId()).observe(this, result -> {
            if (result != null)
                MyApplication.makeToast(String.format("%s %s", routine.getName(), getResources().getString(R.string.routine_exec_message)));
        });
    }

    // TODO: VER DONDE PUEDO RECARGAR VISTAS AL VOLVER DE CONFIG
//    @Override
//    public void onResume() {
//        super.onResume();
//        emptyDeviceCard.setVisibility(View.VISIBLE);
//        emptyRoomCard.setVisibility(View.VISIBLE);
//        emptyRoutineCard.setVisibility(View.VISIBLE);
//        homeViewModel.reloadDevices();
//        getFavDeviceList();
//        homeViewModel.reloadRooms();
//        getFavRoomList();
//        homeViewModel.reloadRoutines();
//        getFavRoutineList();
//    }
}