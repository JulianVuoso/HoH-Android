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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class RoomsFragment extends Fragment {

    private RoomsViewModel roomsViewModel;

    private RecyclerView rvRooms;
    private GridLayoutManager gridLayoutManager;
    private RoomsAdapter adapter;

    private List<Room> rooms = new ArrayList<>();

    private String requestTag;

    private CardView emptyCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        roomsViewModel = ViewModelProviders.of(this).get(RoomsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_rooms, container, false);

        rvRooms = root.findViewById(R.id.rv_rooms);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        gridLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
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

        return root;
    }

    private void getRoomList() {
        roomsViewModel.getRooms().observe(this, rooms -> {
            if (rooms != null && !rooms.isEmpty())
                emptyCard.setVisibility(View.GONE);
            adapter.setRooms(rooms);
            Log.v(MainActivity.LOG_TAG, "ACTUALICE ROOMS");
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO: VER ADONDE MANDARLE EL CANCEL REQUEST, NO TENGO EL TAG.
        // TODO: SACAR CONTEXT
        Api.getInstance(this.getContext()).cancelRequest(requestTag);
    }

    @Override
    public void onResume() {
        super.onResume();
        emptyCard.setVisibility(View.VISIBLE);
        roomsViewModel.reloadRooms();
        getRoomList();
    }
}