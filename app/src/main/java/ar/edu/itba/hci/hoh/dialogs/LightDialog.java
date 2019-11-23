package ar.edu.itba.hci.hoh.dialogs;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.elements.Device;

class LightDialog extends DeviceDialog {
    private AlertDialog dialog;
    private Switch sw;
    private SeekBar brightness;
    private TextView brightnessText;
    private ColorPicker picker;

    LightDialog(Fragment fragment, Device device) {
        super(fragment, device);
    }

    AlertDialog openDialog() {
        LayoutInflater inflater = fragment.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_device_light, null);
        this.dialog = new AlertDialog.Builder(fragment.getContext()).setView(dialogView).create();
//        this.dialog.setOnDismissListener(dialog -> DialogCreator.closeDialog());
        setDialogHeader(dialogView);

        sw = dialogView.findViewById(R.id.light_switch);
        sw.setChecked(device.getState().getStatus().equals("on"));
        brightness = dialogView.findViewById(R.id.brightness);
        brightnessText = dialogView.findViewById(R.id.brightness_text);
        picker = dialogView.findViewById(R.id.picker);

        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            device.getState().setStatus((isChecked) ? "on" : "off");
            execAction((isChecked) ? "turnOn" : "turnOff");
        });

        String initBrightness = device.getState().getBrightness() + "%";
        brightnessText.setText(initBrightness);
        brightness.setMax(100);
        brightness.setProgress(device.getState().getBrightness());
        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = progress + "%";
                brightnessText.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                device.getState().setBrightness(seekBar.getProgress());
                execAction("setBrightness", getParams(seekBar.getProgress()));
            }
        });

        SVBar svBar = dialogView.findViewById(R.id.svbar);
        String initColor = device.getState().getColor();
        picker.setShowOldCenterColor(true);
        picker.setColor(Color.parseColor("#" + initColor));
        picker.setOldCenterColor(Color.parseColor("#" + initColor));
        picker.addSVBar(svBar);
        picker.getColor();
        picker.setOnColorChangedListener(color -> {
            String hexColor = Integer.toHexString(color).substring(2);
            device.getState().setColor(hexColor);
            execAction("setColor", getParams(hexColor));
        });



        this.dialog.show();

        return this.dialog;
    }

    void closeDialog() {
        super.cancelTimer();
        dialog.dismiss();
    }


    void reloadData() {
        Log.e(MainActivity.LOG_TAG, "actualizando");
//        sw.setChecked(device.getState().getStatus().equals("on"));
//        String initBrightness = device.getState().getBrightness() + "%";
//        brightnessText.setText(initBrightness);
//        brightness.setProgress(device.getState().getBrightness());
//        picker.setColor(Color.parseColor("#" + device.getState().getColor()));
    }
}
