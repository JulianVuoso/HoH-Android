package ar.edu.itba.hci.hoh.ui.room;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class RoomFragment extends Fragment {

    private RoomViewModel roomViewModel;

    private RecyclerView rvDevices;
    private GridLayoutManager gridLayoutManager;
    private static RoomListAdapter adapter;

    private List<Device> data = new ArrayList<>();

    private Room room;

    private List<String> requestTag = new ArrayList<>();

    private CardView emptyCard;

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
                // TODO: OPEN DIALOGue
            }
        });
        rvDevices.setAdapter(adapter);

        emptyCard = root.findViewById(R.id.empty_device_room_card);
        TextView tvEmptyRoom = emptyCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_device_list);

        if (room != null) {
            // TODO: SACAR CONTEXT
            requestTag.add(Api.getInstance(this.getContext()).getDevicesFromRoom(room.getId(), new Response.Listener<ArrayList<Device>>() {
                @Override
                public void onResponse(ArrayList<Device> response) {
                    for (Device device : response)
                        device.setRoom(room);
                    if (!data.isEmpty())
                        emptyCard.setVisibility(View.GONE);
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

    public static void notifyAdapter() {
        if (adapter != null)
            adapter.updatedDataSet();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (room != null && room.getMeta().isFavorite())
            inflater.inflate(R.menu.appbar_room_menu_fav, menu);
        else
            inflater.inflate(R.menu.appbar_room_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        // TODO: VER COMO SETEAR BIEN EL BOTON SEGUN ESTADO DE FAVORITO (ROOM) --> NO PUEDO COMO ABAJO PORQUE AUN NO ESTA CREADO
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

        // TODO: SACAR CONTEXT
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