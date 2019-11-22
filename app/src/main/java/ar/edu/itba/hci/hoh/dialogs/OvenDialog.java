package ar.edu.itba.hci.hoh.dialogs;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.IntegerParam;
import ar.edu.itba.hci.hoh.elements.StringParam;

class OvenDialog extends DeviceDialog {
    private AlertDialog dialog;
    private Button heatUp, heatDown, heatFull;
    private Button convOff, convEco, convFull;
    private Button grillOff, grillEco, grillFull;

    OvenDialog(Fragment fragment, Device device) {
        super(fragment, device);
    }

    void openDialog() {
        LayoutInflater inflater = fragment.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_device_oven, null);
        this.dialog = new AlertDialog.Builder(fragment.getContext()).setView(dialogView).create();
        setDialogHeader(dialogView);

        SeekBar ovenBar = dialogView.findViewById(R.id.oven_temp_bar);
        TextView ovenTemp = dialogView.findViewById(R.id.oven_temp_text);
        Switch swOven = dialogView.findViewById(R.id.oven_switch);
        //        Initial Values
        swOven.setChecked(device.getState().getStatus().equals("on"));
        String initTemperature = device.getState().getTemperature() + "°C";
        ovenTemp.setText(initTemperature);
        ovenBar.setMax(140);
        ovenBar.setProgress(device.getState().getTemperature() - 90);

        swOven.setOnCheckedChangeListener((buttonView, isChecked) -> execAction((isChecked) ? "turnOn" : "turnOff"));
        ovenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = (progress + 90) + "°C";
                ovenTemp.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                execAction("setTemperature", getParams(seekBar.getProgress() + 90));
            }
        });

        initButtons(dialogView);
        heatUp.setOnClickListener(v -> {
            toggleButton(heatDown, false); toggleButton(heatUp, true); toggleButton(heatFull, false);
            execAction("setHeat", getParams("top"));
        });
        heatDown.setOnClickListener(v -> {
            toggleButton(heatDown, true); toggleButton(heatUp, false); toggleButton(heatFull, false);
            execAction("setHeat", getParams("bottom"));
        });
        heatFull.setOnClickListener(v -> {
            toggleButton(heatDown, false); toggleButton(heatUp, false); toggleButton(heatFull, true);
            execAction("setHeat", getParams("conventional"));
        });

        convFull.setOnClickListener(v -> {
            toggleButton(convEco, false); toggleButton(convFull, true); toggleButton(convOff, false);
            execAction("setConvection", getParams("normal"));
        });
        convEco.setOnClickListener(v -> {
            toggleButton(convEco, true); toggleButton(convFull, false); toggleButton(convOff, false);
            execAction("setConvection", getParams("eco"));
        });
        convOff.setOnClickListener(v -> {
            toggleButton(convEco, false); toggleButton(convFull, false); toggleButton(convOff, true);
            execAction("setConvection", getParams("off"));
        });

        grillFull.setOnClickListener(v -> {
            toggleButton(grillEco, false); toggleButton(grillFull, true); toggleButton(grillOff, false);
            execAction("setGrill", getParams("large"));
        });
        grillEco.setOnClickListener(v -> {
            toggleButton(grillEco, true); toggleButton(grillFull, false); toggleButton(grillOff, false);
            execAction("setGrill", getParams("eco"));
        });
        grillOff.setOnClickListener(v -> {
            toggleButton(grillEco, false); toggleButton(grillFull, false); toggleButton(grillOff, true);
            execAction("setGrill", getParams("off"));
        });

        this.dialog.show();
    }

    void closeDialog() {
        dialog.dismiss();
    }

    private void initButtons(View dialogView) {
        // Set Initial Modes
        heatUp = dialogView.findViewById(R.id.oven_heat_up);
        heatDown = dialogView.findViewById(R.id.oven_heat_down);
        heatFull = dialogView.findViewById(R.id.oven_heat_full);
        switch (device.getState().getHeat()) {
            case "down": toggleButton(heatDown, true);
                toggleButton(heatUp, false); toggleButton(heatFull, false);
                break;
            case "up":  toggleButton(heatUp, true);
                toggleButton(heatDown, false); toggleButton(heatFull, false);
                break;
            default:    toggleButton(heatFull, true);
                toggleButton(heatUp, false); toggleButton(heatDown, false);
                break;
        }
        convOff = dialogView.findViewById(R.id.oven_conv_off);
        convFull = dialogView.findViewById(R.id.oven_conv_full);
        convEco = dialogView.findViewById(R.id.oven_conv_eco);
        switch (device.getState().getConvection()){
            case "off": toggleButton(convOff, true);
                toggleButton(convFull, false); toggleButton(convEco, false);
                break;
            case "full":toggleButton(convFull, true);
                toggleButton(convOff, false); toggleButton(convEco, false);
                break;
            default:    toggleButton(convEco, true);
                toggleButton(convFull, false); toggleButton(convOff, false);
                break;
        }
        grillOff = dialogView.findViewById(R.id.oven_grill_off);
        grillFull = dialogView.findViewById(R.id.oven_grill_full);
        grillEco = dialogView.findViewById(R.id.oven_grill_eco);
        switch (device.getState().getGrill()){
            case "off": toggleButton(grillOff, true);
                toggleButton(grillEco, false); toggleButton(grillFull, false);
                break;
            case "full":toggleButton(grillFull, true);
                toggleButton(grillEco, false); toggleButton(grillOff, false);
                break;
            default:    toggleButton(grillEco, true);
                toggleButton(grillFull, false); toggleButton(grillOff, false);
                break;
        }
    }
}
