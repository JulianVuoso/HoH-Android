package ar.edu.itba.hci.hoh.ui.rooms;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.RestartListener;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.GridLayoutAutofitManager;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class RoomsFragment extends Fragment {

    private RoomsViewModel roomsViewModel;

    private RecyclerView rvRooms;
    private GridLayoutManager gridLayoutManager;
    private RoomsAdapter adapter;

    private CardView emptyCard;

    private RestartListener restartListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        roomsViewModel = ViewModelProviders.of(this).get(RoomsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_rooms, container, false);

        rvRooms = root.findViewById(R.id.rv_rooms);
        gridLayoutManager = new GridLayoutAutofitManager(this.getContext(), (int) getResources().getDimension(R.dimen.img_card_width), GridLayoutManager.VERTICAL, false);
        rvRooms.setLayoutManager(gridLayoutManager);
        adapter = new RoomsAdapter(room -> {
            RoomsFragmentDirections.ActionSelectRoom action = RoomsFragmentDirections.actionSelectRoom(room, room.getName());
            Navigation.findNavController(root).navigate(action);
        });
        rvRooms.setAdapter(adapter);
        getRoomList();
        emptyCard = root.findViewById(R.id.empty_rooms_card);
        TextView tvEmptyRoom = emptyCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_room_list);

        restartListener = () -> {
            roomsViewModel.reloadRooms();
            getRoomList();
        };

        MainActivity.setRestartListener(restartListener);

        return root;
    }

    private void getRoomList() {
        roomsViewModel.getRooms().observe(this, rooms -> {
            if (rooms != null && !rooms.isEmpty())
                emptyCard.setVisibility(View.GONE);
            else
                emptyCard.setVisibility(View.VISIBLE);
            if (rooms != null)
                rooms.sort(Room.getComparator());
            adapter.setRooms(rooms);
            Log.v(MainActivity.LOG_TAG, "ACTUALICE ROOMS");
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        roomsViewModel.cancelRequests();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.removeRestartListener(restartListener);
    }
}