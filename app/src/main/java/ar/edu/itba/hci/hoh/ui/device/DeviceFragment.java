package ar.edu.itba.hci.hoh.ui.device;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Category;
import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class DeviceFragment extends Fragment {

    private DeviceViewModel devicesViewModel;

    private RecyclerView rvDevices;
    private GridLayoutManager gridLayoutManager;
    private DeviceListAdapter adapter;

    private List<Device> data = new ArrayList<>();

    private Category category;

    private List<String> requestTag = new ArrayList<>();

    //Dialog dialog;
    AlertDialog.Builder dialog;
    Button close_dialog, close;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_device, container, false);

        if (getArguments() != null)
            category = DeviceFragmentArgs.fromBundle(getArguments()).getCategory();

        rvDevices = root.findViewById(R.id.rv_list_category_devices);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        // SI PONGO ESTO DE ABAJO, QUEDA PIOLA PARA CUANDO ROTO. PODRIAMOS VER DE ADAPTAR EL SPAN COUNT
//        gridLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
//        rvDevices.setLayoutManager(gridLayoutManager);

        close = root.findViewById(R.id.close_dialog);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rvDevices.setLayoutManager(manager);
        adapter = new DeviceListAdapter(data, new OnItemClickListener<Device>() {
            @Override
            public void onItemClick(Device element) {
                // TODO: OPEN DIALOG
                createDialog(element);
                dialog.show();
                /*close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });*/
            }
        });
        rvDevices.setAdapter(adapter);


        // METODO DE PRUEBA PARA PONER DISPOSITIVOS EN LA LISTA
//        fillData();

        //close_dialog = root.findViewById(R.id.close_dialog);

        if (category != null) {
            for (final DeviceType type : category.getTypes()) {
                requestTag.add(Api.getInstance(this.getContext()).getDevicesFromType(type.getId(), new Response.Listener<ArrayList<Device>>() {
                    @Override
                    public void onResponse(ArrayList<Device> response) {
                        for (Device device : response)
                            device.setType(type);

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
        }

        return root;
    }

    private void createDialog(Device element){

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.settings_door, null);

        dialog = new AlertDialog.Builder(getActivity())
                .setView(mView);

        TextView name = mView.findViewById(R.id.dev_name);
        TextView room = mView.findViewById(R.id.dev_room);
        ImageView img = mView.findViewById(R.id.dev_img);
        LinearLayout panel = mView.findViewById(R.id.panel);
        LinearLayout panel2 = mView.findViewById(R.id.panel2);

        name.setText(element.getName());
        room.setText(element.getRoom().getName());
        img.setImageResource(DeviceType.getDeviceTypeDrawable(element.getType()));
        panel.setVisibility(View.GONE);
        panel2.setVisibility(View.VISIBLE);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        //dialog.getWindow().setLayout(width, height);
        //close = dialog.findViewById(R.id.close_dialog);
    }


    private void fillData() {
        List<Device> auxList = new ArrayList<>();

        DeviceType lamp = new DeviceType("1234", "lamp");
        DeviceType speaker = new DeviceType("2345", "speaker");
        Room living = new Room("1122", "Living Room", "...", false);
        Room kitchen = new Room("2233", "Kitchen", "...", false);
        auxList.add(new Device("1234", "Table Lamp", lamp, kitchen, false));
        auxList.add(new Device("1234", "Front Light", lamp, living, false));
        auxList.add(new Device("1234", "Garden Light", lamp, kitchen, false));

        auxList.add(new Device("1234", "Table Speaker", speaker, kitchen, false));
        auxList.add(new Device("1234", "Juli's Speaker", speaker, living, false));

        for (Device dev : auxList) {
            if (category != null && category.checkDeviceType(dev.getType()))
                data.add(dev);
        }
    }
}