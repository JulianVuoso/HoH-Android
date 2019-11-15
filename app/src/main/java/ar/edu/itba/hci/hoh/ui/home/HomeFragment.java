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

import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.Elements.Routine;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.LinearLayoutPagerManager;
import ar.edu.itba.hci.hoh.ui.MarginItemDecorator;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;
import ar.edu.itba.hci.hoh.ui.device.DeviceAdapter;
import ar.edu.itba.hci.hoh.ui.rooms.RoomsAdapter;
import ar.edu.itba.hci.hoh.ui.rooms.RoomsFragmentDirections;
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

    // TODO: VER SI HACE FALTA PONERLE LAS FLECHAS PARA SCROLLEAR ENTRE ELEMENTOS O SE ENTIENDE SIN ESO
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvFavDevices = root.findViewById(R.id.rv_favorite_devices);
        managerFavDevices = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, 2);
        rvFavDevices.setLayoutManager(managerFavDevices);
        rvFavDevices.addItemDecoration(new MarginItemDecorator((int) getResources().getDimension(R.dimen.card_spacing)));
        adapterFavDevices = new DeviceAdapter(favDevices, new OnItemClickListener<Device>() {
            @Override
            public void onItemClick(Device element) {
                // TODO: OPEN DIALOG
            }
        });
        rvFavDevices.setAdapter(adapterFavDevices);
        getFavDeviceList();
        emptyDeviceCard = root.findViewById(R.id.empty_fav_devices_card);
        TextView tvEmptyDevice = emptyDeviceCard.findViewById(R.id.card_no_element_text);
        tvEmptyDevice.setText(R.string.empty_fav_device_list);

        rvFavRooms = root.findViewById(R.id.rv_favorite_rooms);
        managerFavRooms = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, 2);
        rvFavRooms.setLayoutManager(managerFavRooms);
        rvFavRooms.addItemDecoration(new MarginItemDecorator((int) getResources().getDimension(R.dimen.card_spacing)));
        adapterFavRooms = new RoomsAdapter(favRooms, new OnItemClickListener<Room>() {
            @Override
            public void onItemClick(Room room) {
                HomeFragmentDirections.ActionSelectRoom action = HomeFragmentDirections.actionSelectRoom(room, room.getName());
                Navigation.findNavController(root).navigate(action);
            }
        });
        rvFavRooms.setAdapter(adapterFavRooms);
        getFavRoomList();
        emptyRoomCard = root.findViewById(R.id.empty_fav_rooms_card);
        TextView tvEmptyRoom = emptyRoomCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_fav_room_list);

        rvFavRoutines = root.findViewById(R.id.rv_favorite_routines);
        managerFavRoutines = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, 2);
        rvFavRoutines.setLayoutManager(managerFavRoutines);
        rvFavRoutines.addItemDecoration(new MarginItemDecorator((int) getResources().getDimension(R.dimen.card_spacing)));
        adapterFavRoutines = new RoutinesAdapter(favRoutines, new OnItemClickListener<Routine>() {
            @Override
            public void onItemClick(Routine routine) {
                // TODO: OPEN CONFIRMATION DIALOG TO EXECUTE ROUTINE
                RoutinesFragment.executeRoutine(routine, getContext());
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
        // TODO: SACAR TODOS LOS CONTEXT
        favDevices.clear();
        requestTag.add(Api.getInstance(this.getContext()).getDevices(new Response.Listener<ArrayList<Device>>() {
            @Override
            public void onResponse(ArrayList<Device> response) {
                for (Device device : response) {
                    if (device.getMeta().isFavorite())
                        favDevices.add(device);
                }
                if (!favDevices.isEmpty())
                    emptyDeviceCard.setVisibility(View.GONE);
                adapterFavDevices.notifyDataSetChanged();
                Log.v(MainActivity.LOG_TAG, "ACTUALICE FAV DEVICES");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: VER QUE HACER CON ERROR
                Log.e(MainActivity.LOG_TAG, String.format("ERROR AL ACTUALIZAR FAV DEVICES. El error es %s", error.toString()));
            }
        }));
    }

    private void getFavRoomList() {
        // TODO: SACAR TODOS LOS CONTEXT
        favRooms.clear();
        requestTag.add(Api.getInstance(this.getContext()).getRooms(new Response.Listener<ArrayList<Room>>() {
            @Override
            public void onResponse(ArrayList<Room> response) {
                for (Room room : response) {
                    if (room.getMeta().isFavorite())
                        favRooms.add(room);
                }
                if (!favRooms.isEmpty())
                    emptyRoomCard.setVisibility(View.GONE);
                adapterFavRooms.notifyDataSetChanged();
                Log.v(MainActivity.LOG_TAG, "ACTUALICE FAV ROOMS");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: VER QUE HACER CON ERROR
                Log.e(MainActivity.LOG_TAG, String.format("ERROR AL ACTUALIZAR FAV ROOMS. El error es %s", error.toString()));
            }
        }));
    }

    private void getFavRoutineList() {
        // TODO: SACAR TODOS LOS CONTEXT
        favRoutines.clear();
        requestTag.add(Api.getInstance(this.getContext()).getRoutines(new Response.Listener<ArrayList<Routine>>() {
            @Override
            public void onResponse(ArrayList<Routine> response) {
                for (Routine routine : response) {
                    if (routine.getMeta().isFavorite())
                        favRoutines.add(routine);
                }
                if (!favRoutines.isEmpty())
                    emptyRoutineCard.setVisibility(View.GONE);
                adapterFavRoutines.notifyDataSetChanged();
                Log.v(MainActivity.LOG_TAG, "ACTUALICE FAV ROUTINES");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: VER QUE HACER CON ERROR
                Log.e(MainActivity.LOG_TAG, String.format("ERROR AL ACTUALIZAR FAV ROUTINES. El error es %s", error.toString()));
            }
        }));
    }
}