package ar.edu.itba.hci.hoh.dialogs;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.elements.Device;

class RefrigeratorDialog extends DeviceDialog {
    private AlertDialog dialog;
    private Button modeNormal, modeVacation, modeParty;
    private SeekBar fridgeBar, freezerBar;
    private TextView fridgeText, freezerText;

    RefrigeratorDialog(Fragment fragment, Device device) {
        super(fragment, device);
    }

    void openDialog() {
        LayoutInflater inflater = fragment.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_device_refrigerator, null);
        this.dialog = new AlertDialog.Builder(fragment.getContext()).setView(dialogView).create();
        setDialogHeader(dialogView);

        fridgeBar = dialogView.findViewById(R.id.fridge_temp_bar);
        fridgeText = dialogView.findViewById(R.id.fridge_temp_text);
        freezerBar = dialogView.findViewById(R.id.freezer_temp_bar);
        freezerText = dialogView.findViewById(R.id.freezer_temp_text);

        //         Initial values
        String initFridgeTemperature = device.getState().getTemperature() + "°C";
        fridgeText.setText(initFridgeTemperature);
        fridgeBar.setMax(6);
        fridgeBar.setKeyProgressIncrement(1);
        fridgeBar.setProgress(device.getState().getTemperature() - 2);
        fridgeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = (progress + 2) + "°C";
                fridgeText.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                execAction("setTemperature", getParams(seekBar.getProgress() + 2));
            }
        });

        String initFreezerTemperature = device.getState().getFreezerTemperature() + "°C";
        freezerText.setText(initFreezerTemperature);
        freezerBar.setMax(12);
        freezerBar.setKeyProgressIncrement(1);
        freezerBar.setProgress(device.getState().getFreezerTemperature() + 20);
        freezerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = (progress - 20) + "°C";
                freezerText.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                execAction("setFreezerTemperature", getParams(seekBar.getProgress() - 20));
            }
        });

        initButtons(dialogView);
        modeVacation.setOnClickListener(v -> {
            toggleButton(modeVacation, true); toggleButton(modeParty, false); toggleButton(modeNormal, false);
            execAction("setMode", getParams("vacation"));
        });
        modeParty.setOnClickListener(v -> {
            toggleButton(modeVacation, false); toggleButton(modeParty, true); toggleButton(modeNormal, false);
            execAction("setMode", getParams("party"));
        });
        modeNormal.setOnClickListener(v -> {
            toggleButton(modeVacation, false); toggleButton(modeParty, false); toggleButton(modeNormal, true);
            execAction("setMode", getParams("default"));
        });

        this.dialog.show();
    }

    void closeDialog() {
        super.cancelTimer();
        dialog.dismiss();
    }

    private void initButtons(View dialogView) {
        modeVacation = dialogView.findViewById(R.id.fridge_mode_vac);
        modeParty = dialogView.findViewById(R.id.fridge_mode_party);
        modeNormal = dialogView.findViewById(R.id.fridge_mode_normal);
        setButtons();
    }

    private void setButtons() {
        switch (device.getState().getMode()){
            case "vacation":
                toggleButton(modeVacation, true); toggleButton(modeParty, false); toggleButton(modeNormal, false);
                break;
            case "party":
                toggleButton(modeVacation, false); toggleButton(modeParty, true); toggleButton(modeNormal, false);
                break;
            default:
                toggleButton(modeVacation, false); toggleButton(modeParty, false); toggleButton(modeNormal, true);
                break;
        }
    }

    void reloadData() {
        Log.e(MainActivity.LOG_TAG, "actualizando");
        String initFridgeTemperature = device.getState().getTemperature() + "°C";
        fridgeText.setText(initFridgeTemperature);
        fridgeBar.setProgress(device.getState().getTemperature() - 2);
        String initFreezerTemperature = device.getState().getFreezerTemperature() + "°C";
        freezerText.setText(initFreezerTemperature);
        freezerBar.setProgress(device.getState().getFreezerTemperature() + 20);
        setButtons();
    }
}
