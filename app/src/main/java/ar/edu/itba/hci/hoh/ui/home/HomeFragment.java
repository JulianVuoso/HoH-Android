package ar.edu.itba.hci.hoh.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.RestartListener;
import ar.edu.itba.hci.hoh.dialogs.DialogCreator;
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
    private ProgressBar loadingDevices, loadingRooms, loadingRoutines;

    private RestartListener restartListener;

    private ImageButton leftArrowFavDevices, rightArrowFavDevices, leftArrowFavRooms, rightArrowFavRooms, leftArrowFavRoutines, rightArrowFavRoutines;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        /* FAV DEVICES */
        rvFavDevices = root.findViewById(R.id.rv_favorite_devices);
        managerFavDevices = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, getResources().getDimension(R.dimen.device_card_width));
        rvFavDevices.setLayoutManager(managerFavDevices);
        rvFavDevices.addItemDecoration(new MarginItemDecorator((int) getResources().getDimension(R.dimen.home_card_spacing)));
        adapterFavDevices = new DeviceAdapter(element -> {
            AlertDialog dialog = DialogCreator.createDialog(this, element);
            if (dialog != null) {
                dialog.setOnDismissListener(dialog1 -> {
                    DialogCreator.closeDialog();
                    updateFragment();
                });
            }
        });
        adapterFavDevices.setDevices(favDevices);
        rvFavDevices.setAdapter(adapterFavDevices);
        getFavDeviceList();
        emptyDeviceCard = root.findViewById(R.id.empty_fav_devices_card);
        TextView tvEmptyDevice = emptyDeviceCard.findViewById(R.id.card_no_element_text);
        tvEmptyDevice.setText(R.string.empty_fav_device_list);
        loadingDevices = root.findViewById(R.id.fav_devices_loading_bar);

        leftArrowFavDevices = root.findViewById(R.id.left_arrow_favorite_devices);
        leftArrowFavDevices.setOnClickListener(getLeftArrowListener(managerFavDevices, rvFavDevices));
        rightArrowFavDevices = root.findViewById(R.id.right_arrow_favorite_devices);
        rightArrowFavDevices.setOnClickListener(v -> rvFavDevices.smoothScrollToPosition(managerFavDevices.findLastVisibleItemPosition() + 1));
        rvFavDevices.addOnScrollListener(getOnScrollListener(managerFavDevices, leftArrowFavDevices, rightArrowFavDevices, favDevices));

        /* FAV ROOMS */
        rvFavRooms = root.findViewById(R.id.rv_favorite_rooms);
//        managerFavRooms = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, 2);
        managerFavRooms = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, getResources().getDimension(R.dimen.img_card_width));
        rvFavRooms.setLayoutManager(managerFavRooms);
        rvFavRooms.addItemDecoration(new MarginItemDecorator((int) getResources().getDimension(R.dimen.home_card_spacing)));
        adapterFavRooms = new RoomsAdapter(room -> {
            HomeFragmentDirections.ActionSelectRoom action = HomeFragmentDirections.actionSelectRoom(room, room.getName());
            Navigation.findNavController(root).navigate(action);
        });
        adapterFavRooms.setRooms(favRooms);
        rvFavRooms.setAdapter(adapterFavRooms);
        getFavRoomList();
        emptyRoomCard = root.findViewById(R.id.empty_fav_rooms_card);
        TextView tvEmptyRoom = emptyRoomCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_fav_room_list);
        loadingRooms = root.findViewById(R.id.fav_rooms_loading_bar);

        leftArrowFavRooms = root.findViewById(R.id.left_arrow_favorite_rooms);
        leftArrowFavRooms.setOnClickListener(getLeftArrowListener(managerFavRooms, rvFavRooms));
        rightArrowFavRooms = root.findViewById(R.id.right_arrow_favorite_rooms);
        rightArrowFavRooms.setOnClickListener(v -> rvFavRooms.smoothScrollToPosition(managerFavRooms.findLastVisibleItemPosition() + 1));
        rvFavRooms.addOnScrollListener(getOnScrollListener(managerFavRooms, leftArrowFavRooms, rightArrowFavRooms, favRooms));

        /* FAV ROUTINES */
        rvFavRoutines = root.findViewById(R.id.rv_favorite_routines);
        managerFavRoutines = new LinearLayoutPagerManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false, getResources().getDimension(R.dimen.img_card_width));
        rvFavRoutines.setLayoutManager(managerFavRoutines);
        rvFavRoutines.addItemDecoration(new MarginItemDecorator((int) getResources().getDimension(R.dimen.home_card_spacing)));
        adapterFavRoutines = new RoutinesAdapter(routine -> DialogCreator.createDialog(this, routine));
        rvFavRoutines.setAdapter(adapterFavRoutines);
        getFavRoutineList();
        emptyRoutineCard = root.findViewById(R.id.empty_fav_routines_card);
        TextView tvEmptyRoutine = emptyRoutineCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoutine.setText(R.string.empty_fav_routine_list);
        loadingRoutines = root.findViewById(R.id.fav_routines_loading_bar);

        leftArrowFavRoutines = root.findViewById(R.id.left_arrow_favorite_routines);
        leftArrowFavRoutines.setOnClickListener(getLeftArrowListener(managerFavRoutines, rvFavRoutines));
        rightArrowFavRoutines = root.findViewById(R.id.right_arrow_favorite_routines);
        rightArrowFavRoutines.setOnClickListener(v -> rvFavRoutines.smoothScrollToPosition(managerFavRoutines.findLastVisibleItemPosition() + 1));
        rvFavRoutines.addOnScrollListener(getOnScrollListener(managerFavRoutines, leftArrowFavRoutines, rightArrowFavRoutines, favRoutines));

        restartListener = this::updateFragment;

        MainActivity.setRestartListener(restartListener);

        return root;
    }

    private void updateFragment() {
        homeViewModel.reloadDevices();
        getFavDeviceList();
        homeViewModel.reloadRooms();
        getFavRoomList();
        homeViewModel.reloadRoutines();
        getFavRoutineList();
    }

    private <T> RecyclerView.OnScrollListener getOnScrollListener(LinearLayoutManager manager, ImageButton leftArrow, ImageButton rightArrow, List<T> list) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (manager.findFirstCompletelyVisibleItemPosition() == 0)
                    leftArrow.setVisibility(View.INVISIBLE);
                else
                    leftArrow.setVisibility(View.VISIBLE);

                if (manager.findLastCompletelyVisibleItemPosition() == list.size() - 1)
                    rightArrow.setVisibility(View.INVISIBLE);
                else
                    rightArrow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };
    }

    private View.OnClickListener getLeftArrowListener(LinearLayoutManager manager, RecyclerView recyclerView) {
        return v -> {
            int pos = managerFavDevices.findFirstVisibleItemPosition();
            if (pos > 0)
                rvFavDevices.smoothScrollToPosition(pos - 1);
            else
                rvFavDevices.smoothScrollToPosition(0);
        };
    }

    private void configureInitialArrows(ImageButton leftArrow, ImageButton rightArrow, int size) {
        if (size == 0) {
            leftArrow.setVisibility(View.GONE);
            rightArrow.setVisibility(View.GONE);
            return;
        }
        leftArrow.setVisibility(View.INVISIBLE);
        rightArrow.setVisibility(View.VISIBLE);
    }

    private void getFavDeviceList() {
        homeViewModel.getDevices().observe(this, devices -> {
            favDevices.clear();
            if (devices != null) {
                for (Device device : devices)
                    if (device.getMeta().isFavorite())
                        favDevices.add(device);
            }
            loadingDevices.setVisibility(View.GONE);
            if (!favDevices.isEmpty())
                emptyDeviceCard.setVisibility(View.GONE);
            else
                emptyDeviceCard.setVisibility(View.VISIBLE);
            favDevices.sort(Device.getNameComparator());
            adapterFavDevices.notifyDataSetChanged();
            configureInitialArrows(leftArrowFavDevices, rightArrowFavDevices, favDevices.size());
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
            loadingRooms.setVisibility(View.GONE);
            if (!favRooms.isEmpty())
                emptyRoomCard.setVisibility(View.GONE);
            else
                emptyRoomCard.setVisibility(View.VISIBLE);
            favRooms.sort(Room.getComparator());
            adapterFavRooms.notifyDataSetChanged();
            configureInitialArrows(leftArrowFavRooms, rightArrowFavRooms, favRooms.size());
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
            loadingRoutines.setVisibility(View.GONE);
            if (!favRoutines.isEmpty())
                emptyRoutineCard.setVisibility(View.GONE);
            else
                emptyRoutineCard.setVisibility(View.VISIBLE);
            favRoutines.sort(Routine.getComparator());
            adapterFavRoutines.notifyDataSetChanged();
            configureInitialArrows(leftArrowFavRoutines, rightArrowFavRoutines, favRoutines.size());
            Log.v(MainActivity.LOG_TAG, "ACTUALICE FAV Routines");
        });
    }

    private void executeRoutine(Routine routine) {
        homeViewModel.execRoutine(routine.getId()).observe(this, result -> {
            if (result != null)
                MyApplication.makeToast(String.format("%s %s", routine.getName(), getResources().getString(R.string.routine_exec_message)));
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        homeViewModel.cancelRequests();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.removeRestartListener(restartListener);
    }
}