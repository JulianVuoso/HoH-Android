package ar.edu.itba.hci.hoh.ui.room;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class RoomFragment extends Fragment {

    private RoomViewModel roomViewModel;

    private RecyclerView rvDevices;
    private GridLayoutManager gridLayoutManager;
    private RoomListAdapter adapter;

    private List<Device> data = new ArrayList<>();

    private Room room;

    private List<String> requestTag = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        roomViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
        View root = inflater.inflate(R.layout.fragment_room, container, false);

        if (getArguments() != null)
            room = RoomFragmentArgs.fromBundle(getArguments()).getRoom();

        rvDevices = root.findViewById(R.id.rv_list_room_devices);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        // SI PONGO ESTO DE ABAJO, QUEDA PIOLA PARA CUANDO ROTO. PODRIAMOS VER DE ADAPTAR EL SPAN COUNT
//        gridLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
//        rvDevices.setLayoutManager(gridLayoutManager);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rvDevices.setLayoutManager(manager);
        adapter = new RoomListAdapter(data, new OnItemClickListener<Device>() {
            @Override
            public void onItemClick(Device element) {
                // TODO: OPEN DIALOG
            }
        });
        rvDevices.setAdapter(adapter);

        if (room != null) {
            requestTag.add(Api.getInstance(this.getContext()).getDevicesFromRoom(room.getId(), new Response.Listener<ArrayList<Device>>() {
                @Override
                public void onResponse(ArrayList<Device> response) {
                    for (Device device : response)
                        device.setRoom(room);

                    data.addAll(response);
                    adapter.updatedDataSet();
                    Log.v(MainActivity.LOG_TAG, "ACTUALICE DISPOSITIVOS");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: VER QUE HACER CON ERROR
                    Log.e(MainActivity.LOG_TAG, String.format("ERROR AL ACTUALIZAR DISPOSITIVOS. El error es %s", error.toString()));
                }
            }));
        }

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_room_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        // TODO: VER COMO SETEAR EL BOTON SEGUN ESTADO DE FAVORITO (ROOM) --> NO PUEDO COMO ABAJO PORQUE AUN NO ESTA CREADO
//        if (room != null && room.getMeta().isFavorite())
//            menu.getItem(R.id.action_favorite).setIcon(R.drawable.ic_star_white_24dp);
//        else
//            menu.getItem(R.id.action_favorite).setIcon(R.drawable.ic_star_border_white_24dp);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() != R.id.action_favorite)
            return super.onOptionsItemSelected(item);

        room.getMeta().setFavorite(!room.getMeta().isFavorite());

        requestTag.add(Api.getInstance(this.getContext()).modifyRoom(room, new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                if (response) {
                    if (room != null && room.getMeta().isFavorite())
                        item.setIcon(R.drawable.ic_star_white_24dp);
                    else
                        item.setIcon(R.drawable.ic_star_border_white_24dp);
                } else {
                    // TODO: VER QUE HACER CON ERROR
                    Log.e(MainActivity.LOG_TAG, "NO SE PUDO ACTUALIZAR FAV");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: VER QUE HACER CON ERROR
                Log.e(MainActivity.LOG_TAG, String.format("ERROR AL MODIFICAR HABITACION. El error es %s", error.toString()));
            }
        }));

        return true;
    }
}