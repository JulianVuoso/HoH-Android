package ar.edu.itba.hci.hoh.ui.rooms;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;
import ar.edu.itba.hci.hoh.ui.devices.DevicesAdapter;
import ar.edu.itba.hci.hoh.ui.devices.DevicesFragmentDirections;

public class RoomsFragment extends Fragment {

    private RoomsViewModel roomsViewModel;

    private RecyclerView rvRooms;
    private GridLayoutManager gridLayoutManager;
    private RoomsAdapter adapter;

    private List<Room> rooms = new ArrayList<>();

    private String requestTag;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        roomsViewModel = ViewModelProviders.of(this).get(RoomsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_rooms, container, false);

        rvRooms = root.findViewById(R.id.rv_rooms);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        gridLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
        rvRooms.setLayoutManager(gridLayoutManager);
        adapter = new RoomsAdapter(rooms, new OnItemClickListener<Room>() {
            @Override
            public void onItemClick(Room room) {
                RoomsFragmentDirections.ActionSelectRoom action = RoomsFragmentDirections.actionSelectRoom(room, room.getName());
                Navigation.findNavController(root).navigate(action);
            }
        });
        rvRooms.setAdapter(adapter);

        rooms.clear();
        getRoomList();

        return root;
    }

    private void getRoomList() {
        requestTag = Api.getInstance(this.getContext()).getRooms(new Response.Listener<ArrayList<Room>>() {
            @Override
            public void onResponse(ArrayList<Room> response) {
                rooms.addAll(response);
                adapter.notifyDataSetChanged();
                Log.v(MainActivity.LOG_TAG, "ACTUALICE ROOMS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: VER QUE HACER CON ERROR
                Log.e(MainActivity.LOG_TAG, String.format("ERROR AL ACTUALIZAR ROOMS. El error es %s", error.toString()));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Api.getInstance(this.getContext()).cancelRequest(requestTag);
    }
}