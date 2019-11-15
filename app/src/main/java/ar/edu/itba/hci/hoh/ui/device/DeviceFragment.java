package ar.edu.itba.hci.hoh.ui.device;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

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

    private int orange = Color.rgb(255, 152, 0);

    //Dialog dialog;
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

        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rvDevices.setLayoutManager(manager);
        adapter = new DeviceListAdapter(data, new OnItemClickListener<Device>() {
            @Override
            public void onItemClick(Device element) {
                createDialog(element);
            }
        });
        rvDevices.setAdapter(adapter);

        // METODO DE PRUEBA PARA PONER DISPOSITIVOS EN LA LISTA
        // fillData();

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
        View root = inflater.inflate(R.layout.settings_dialog, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setView(root);
        final AlertDialog dialog = dialogBuilder.create();

        TextView name = root.findViewById(R.id.dev_name);
        TextView room = root.findViewById(R.id.dev_room);
        ImageView img = root.findViewById(R.id.dev_img);

        name.setText(element.getName());
        room.setText(element.getRoom().getName());
        img.setImageResource(DeviceType.getDeviceTypeDrawable(element.getType()));
        root.findViewById(R.id.close_dialog).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        setPanel(root, element);

        dialog.show();
//        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);
//        dialog.getWindow().setLayout(width, height);
    }

    private void setPanel(View root, Device element){
        switch (element.getType().getName()){
            case "lamp":
                root.findViewById(R.id.light_panel).setVisibility(View.VISIBLE);
                final SeekBar brightness = root.findViewById(R.id.brightness);
                final TextView brightnessText = root.findViewById(R.id.brightness_text);
                brightness.setProgress(element.getState().getBrightness());
                brightness.setMax(100);
                brightnessText.setText(element.getState().getBrightness()+"%");
                brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        brightness.setProgress(seekBar.getProgress());
                        brightnessText.setText(seekBar.getProgress()+"%");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                Switch sw = root.findViewById(R.id.light_switch);
                sw.setChecked( element.getState().getStatus().compareTo("on") == 0 ? true : false );
            break;
            case "door":
                root.findViewById(R.id.door_panel).setVisibility(View.VISIBLE);
                Button btnO = root.findViewById(R.id.door_open);
                Button btnC = root.findViewById(R.id.door_closed);
                Button btnL = root.findViewById(R.id.door_locked);
                if(element.getState().getStatus().compareTo("closed") == 0)
                    turnOn(btnC);
                else
                    turnOn(btnO);
                if(element.getState().getLock().compareTo("locked")==0)
                    turnOn(btnL);
                break;
            case "blinds":
                root.findViewById(R.id.window_panel).setVisibility(View.VISIBLE);
                Button openBtn = root.findViewById(R.id.window_open);
                Button closeBtn = root.findViewById(R.id.window_close);
                if(element.getState().getStatus().compareTo("opened")==0)
                    turnOn(openBtn);
                else
                    turnOn(closeBtn);
                final ProgressBar pB = root.findViewById(R.id.window_bar);
                final Device device = element;
                pB.setMax(100);
                pB.setProgressTintList(ColorStateList.valueOf(orange));
                pB.setProgress(device.getState().getLevel());
                openBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    while(device.getState().getStatus().compareTo("opened")==0){
                        pB.setProgress(device.getState().getLevel());
                        pB.setSecondaryProgress(device.getState().getLevel()+10);
                    }
                    }
                });
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    while(device.getState().getStatus().compareTo("closed")==0){
                        pB.setProgress(device.getState().getLevel());
                        pB.setSecondaryProgress(device.getState().getLevel()-10);
                    }
                    }
                });
            break;
            case "oven":
                root.findViewById(R.id.oven_panel).setVisibility(View.VISIBLE);
                Switch swOven = root.findViewById(R.id.oven_switch);
                swOven.setChecked(element.getState().getStatus().compareTo("on") == 0 ? true : false);
                final SeekBar ovenBar = root.findViewById(R.id.oven_temp_bar);
                final TextView ovenTemp = root.findViewById(R.id.oven_temp_text);
                ovenTemp.setText(element.getState().getTemperature()+"°C");
                ovenBar.setMax(220);
                ovenBar.setProgress(element.getState().getTemperature());
                ovenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        ovenBar.setProgress(seekBar.getProgress());
                        ovenTemp.setText(seekBar.getProgress()+"°C");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                switch (element.getState().getHeat()){
                    case "up":  Button heatUp = root.findViewById(R.id.heat_up);
                                turnOn(heatUp); break;
                    case "down":Button heatDown = root.findViewById(R.id.heat_down);
                                turnOn(heatDown); break;
                    default:    Button heatFull = root.findViewById(R.id.heat_full);
                                turnOn(heatFull); break;
                }
                switch (element.getState().getConvection()){
                    case "off": Button convOff = root.findViewById(R.id.conv_off);
                                turnOn(convOff); break;
                    case "full":Button convFull = root.findViewById(R.id.conv_full);
                                turnOn(convFull); break;
                    default:    Button convEcho = root.findViewById(R.id.conv_echo);
                                turnOn(convEcho); break;
                }
                switch (element.getState().getGrill()){
                    case "off": Button grillOff = root.findViewById(R.id.grill_off);
                                turnOn(grillOff); break;
                    case "full":Button grillFull = root.findViewById(R.id.grill_full);
                                turnOn(grillFull); break;
                    default:    Button grillEcho = root.findViewById(R.id.grill_echo);
                                turnOn(grillEcho); break;
                }
                break;
            case "refrigerator":
                root.findViewById(R.id.fridge_panel).setVisibility(View.VISIBLE);
                switch (element.getState().getMode()){
                    case "vacation":
                        Button vacation = root.findViewById(R.id.mode_vac);
                        turnOn(vacation); break;
                    case "party":
                        Button party = root.findViewById(R.id.mode_party);
                        turnOn(party); break;
                    default:
                        Button normal = root.findViewById(R.id.mode_normal);
                        turnOn(normal); break;
                }
                final SeekBar fridgeBar = root.findViewById(R.id.fridge_bar);
                final TextView fridgeText = root.findViewById(R.id.fridge_text);
                final SeekBar freezerBar = root.findViewById(R.id.freezer_bar);
                final TextView freezerText = root.findViewById(R.id.freezer_text);
                fridgeBar.setMax(12);
                fridgeBar.setKeyProgressIncrement(1);
                fridgeBar.setProgress(element.getState().getTemperature());
                fridgeText.setText(element.getState().getTemperature()+"°C");
                fridgeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        fridgeBar.setProgress(seekBar.getProgress());
                        fridgeText.setText(seekBar.getProgress()+"°C");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                freezerBar.setMax(12);
                freezerBar.setKeyProgressIncrement(1);
                freezerBar.setProgress(element.getState().getFreezerTemperature());
                freezerText.setText(element.getState().getFreezerTemperature()+"°C");
                freezerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        freezerBar.setProgress(seekBar.getProgress());
                        freezerText.setText(seekBar.getProgress()+"°C");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                break;
            case "ac":
                root.findViewById(R.id.aa_panel).setVisibility(View.VISIBLE);
                Switch swAA = root.findViewById(R.id.aa_switch);
                swAA.setChecked(element.getState().getStatus().compareTo("on") == 0 ? true : false);
                switch (element.getState().getMode()){
                    case "cold":
                        Button cold = root.findViewById(R.id.mode_cold);
                        turnOn(cold); break;
                    case "vent":
                        Button vent = root.findViewById(R.id.mode_vent);
                        turnOn(vent); break;
                    default:
                        Button heat = root.findViewById(R.id.mode_heat);
                        turnOn(heat); break;
                }
                final SeekBar tempAA = root.findViewById(R.id.aa_temp);
                final TextView AAtext = root.findViewById(R.id.aa_temp_text);
                tempAA.setMax(12);
                tempAA.setKeyProgressIncrement(1);
                tempAA.setProgress(element.getState().getTemperature());
                AAtext.setText(element.getState().getTemperature()+"°C");
                tempAA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        tempAA.setProgress(seekBar.getProgress());
                        AAtext.setText(seekBar.getProgress()+"°C");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                final SeekBar fanSpeed = root.findViewById(R.id.fan_speed);
                final TextView fanSpeedText = root.findViewById(R.id.fan_speed_text);
                fanSpeed.setMax(12);
                fanSpeed.setKeyProgressIncrement(1);
//                fanSpeed.setProgress(Integer.parseInt(element.getState().getFanSpeed()));
                fanSpeedText.setText(element.getState().getFanSpeed()+"°C");
                fanSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        fanSpeed.setProgress(seekBar.getProgress());
                        fanSpeedText.setText(seekBar.getProgress()+"°C");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                final SeekBar vertWings = root.findViewById(R.id.aa_temp);
                vertWings.setMax(100);
                vertWings.setKeyProgressIncrement(1);
//                vertWings.setProgress(Integer.parseInt(element.getState().getVerticalSwing()));
                vertWings.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        vertWings.setProgress(seekBar.getProgress());
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                final SeekBar horizWings = root.findViewById(R.id.aa_temp);
                horizWings.setMax(100);
                horizWings.setKeyProgressIncrement(1);
//                horizWings.setProgress(Integer.parseInt(element.getState().getHorizontalSwing()));
                horizWings.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        horizWings.setProgress(seekBar.getProgress());
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
            break;
            case "speaker":
                root.findViewById(R.id.speaker_panel).setVisibility(View.VISIBLE);
                TextView time = root.findViewById(R.id.time);
                TextView song = root.findViewById(R.id.song);
                TextView artist = root.findViewById(R.id.artist);
                //TODO alternar imagen de play o pause segun state
                //TODO ver el spinner del genero
                Spinner genre = root.findViewById(R.id.genre_selector);
                time.setText(element.getState().getSong().getProgress());
                song.setText(element.getState().getSong().getTitle());
                artist.setText(element.getState().getSong().getArtist());
//                String[] genres = {"Rock", "Pop", "Regueaton", "Cumbia"};
//                genre.setAdapter(genres);
                final SeekBar volume = root.findViewById(R.id.volume_bar);
                final TextView volumeTxt = root.findViewById(R.id.volume_text);
                volumeTxt.setText(element.getState().getVolume()+"%");
                volume.setMax(100);
                volume.setProgress(element.getState().getVolume());
                volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        volume.setProgress(seekBar.getProgress());
                        volumeTxt.setText(seekBar.getProgress()+"%");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });            break;
            default: break;
        }
    }

    private void turnOn(Button btn){
        btn.setBackgroundTintList(ColorStateList.valueOf(orange));
        return;
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