package ar.edu.itba.hci.hoh.dialogs;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.elements.Device;

class AcDialog extends DeviceDialog {
    private AlertDialog dialog;
    private Button modeCold, modeVent, modeHeat;

    AcDialog(Fragment fragment, Device device) {
        super(fragment, device);
    }

    void openDialog() {
        LayoutInflater inflater = fragment.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_device_ac, null);
        this.dialog = new AlertDialog.Builder(fragment.getContext()).setView(dialogView).create();
        setDialogHeader(dialogView);

        Switch swAc = dialogView.findViewById(R.id.ac_switch);
        swAc.setChecked(device.getState().getStatus().equals("on"));
        swAc.setOnCheckedChangeListener((buttonView, isChecked) -> execAction((isChecked) ? "turnOn" : "turnOff"));

        initTemperature(dialogView);
        initFanSpeed(dialogView);
        initVertWings(dialogView);
        initHorizWings(dialogView);

        initButtons(dialogView);
        modeCold.setOnClickListener(v -> {
            toggleButton(modeCold, true); toggleButton(modeVent, false); toggleButton(modeHeat, false);
            execAction("setMode", getParams("cold"));
        });
        modeVent.setOnClickListener(v -> {
            toggleButton(modeCold, false); toggleButton(modeVent, true); toggleButton(modeHeat, false);
            execAction("setMode", getParams("fan"));
        });
        modeHeat.setOnClickListener(v -> {
            toggleButton(modeCold, false); toggleButton(modeVent, false); toggleButton(modeHeat, true);
            execAction("setMode", getParams("heat"));
        });

        this.dialog.show();
    }

    void closeDialog() {
        dialog.dismiss();
    }

    private void initButtons(View dialogView) {
        modeCold = dialogView.findViewById(R.id.ac_mode_cold);
        modeVent = dialogView.findViewById(R.id.ac_mode_vent);
        modeHeat = dialogView.findViewById(R.id.ac_mode_heat);
        switch (device.getState().getMode()){
            case "cold":
                toggleButton(modeCold, true); toggleButton(modeVent, false); toggleButton(modeHeat, false);
                break;
            case "fan":
                toggleButton(modeCold, false); toggleButton(modeVent, true); toggleButton(modeHeat, false);
                break;
            default:
                toggleButton(modeCold, false); toggleButton(modeVent, false); toggleButton(modeHeat, true);
                break;
        }
    }

    private void initTemperature(View dialogView) {
        SeekBar tempAc = dialogView.findViewById(R.id.ac_temp_bar);
        TextView tempAcText = dialogView.findViewById(R.id.ac_temp_text);
        String initAcTemp = device.getState().getTemperature() + "°C";
        tempAcText.setText(initAcTemp);
        tempAc.setMax(20);
        tempAc.setKeyProgressIncrement(1);
        tempAc.setProgress(device.getState().getTemperature() - 18);
        tempAc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = (progress + 18) + "°C";
                tempAcText.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                execAction("setTemperature", getParams(seekBar.getProgress() + 18));
            }
        });
    }

    private void initFanSpeed(View dialogView) {
        SeekBar fanSpeed = dialogView.findViewById(R.id.ac_fan_speed_bar);
        TextView fanSpeedText = dialogView.findViewById(R.id.ac_fan_speed_text);
        boolean auto = device.getState().getFanSpeed().equals("auto");
        if (auto) {
            fanSpeed.setEnabled(false);
            fanSpeedText.setVisibility(View.INVISIBLE);
            fanSpeed.setProgress(2);
        } else {
            fanSpeed.setEnabled(true);
            fanSpeedText.setVisibility(View.VISIBLE);
            fanSpeed.setProgress(Integer.valueOf(device.getState().getFanSpeed()) / 25 - 1);
            String initFanSpeed = device.getState().getFanSpeed() + "%";
            fanSpeedText.setText(initFanSpeed);
        }
        fanSpeed.setMax(3);
        fanSpeed.setKeyProgressIncrement(1);
        fanSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = ((progress + 1) * 25) + "%";
                fanSpeedText.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                execAction("setFanSpeed", getParams((seekBar.getProgress() + 1) * 25));
            }
        });

        // TODO: REVISAR SI ESTOS FAN SPEED, HOR SWING Y VERT SWING DEBEN SER SIEMPRE STRING O VAN PASANDO DE UNO A OTRO
        CheckBox auto_speed = dialogView.findViewById(R.id.ac_cb_speed);
        auto_speed.setChecked(auto);
        auto_speed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                execAction("setFanSpeed", getParams("auto"));
                fanSpeed.setEnabled(false);
                fanSpeedText.setVisibility(View.INVISIBLE);
                fanSpeed.setProgress(2);
            } else {
                execAction("setFanSpeed", getParams(50));
                fanSpeed.setEnabled(true);
                fanSpeedText.setVisibility(View.VISIBLE);
                fanSpeed.setProgress(1);
                String newText = "50%";
                fanSpeedText.setText(newText);
            }
        });
    }

    private void initVertWings(View dialogView) {
        SeekBar vertWings = dialogView.findViewById(R.id.ac_vert_wings_bar);
        TextView vertWingsText = dialogView.findViewById(R.id.ac_vert_wings_text);
        boolean auto = device.getState().getVerticalSwing().equals("auto");
        if (auto) {
            vertWings.setEnabled(false);
            vertWingsText.setVisibility(View.INVISIBLE);
            vertWings.setProgress(1);
        } else {
            vertWings.setEnabled(true);
            vertWingsText.setVisibility(View.VISIBLE);
            vertWings.setProgress(Integer.valueOf(device.getState().getVerticalSwing()) * 10 / 225 - 1);
            String initFanSpeed = device.getState().getVerticalSwing() + "°";
            vertWingsText.setText(initFanSpeed);
        }
        vertWings.setMax(3);
        vertWings.setKeyProgressIncrement(1);
        vertWings.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = ((progress + 1) * 225 / 10) + "°";
                vertWingsText.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                execAction("setVerticalSwing", getParams(((seekBar.getProgress() + 1) * 225 / 10)));
            }
        });

        CheckBox auto_vertical = dialogView.findViewById(R.id.ac_cb_vertical);
        auto_vertical.setChecked(auto);
        auto_vertical.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                execAction("setVerticalSwing", getParams("auto"));
                vertWings.setEnabled(false);
                vertWingsText.setVisibility(View.INVISIBLE);
                vertWings.setProgress(1);
            } else {
                execAction("setVerticalSwing", getParams(45));
                vertWings.setEnabled(true);
                vertWingsText.setVisibility(View.VISIBLE);
                vertWings.setProgress(1);
                String newText = "45°";
                vertWingsText.setText(newText);
            }
        });
    }

    private void initHorizWings(View dialogView) {
        SeekBar horizWings = dialogView.findViewById(R.id.ac_horiz_wings_bar);
        TextView horizWingsText = dialogView.findViewById(R.id.ac_horiz_wings_text);
        boolean auto = device.getState().getHorizontalSwing().equals("auto");
        if (auto) {
            horizWings.setEnabled(false);
            horizWingsText.setVisibility(View.INVISIBLE);
            horizWings.setProgress(2);
        } else {
            horizWings.setEnabled(true);
            horizWingsText.setVisibility(View.VISIBLE);
            horizWings.setProgress((Integer.valueOf(device.getState().getHorizontalSwing()) + 90) / 45);
            String initFanSpeed = device.getState().getHorizontalSwing() + "°";
            horizWingsText.setText(initFanSpeed);
        }
        horizWings.setMax(4);
        horizWings.setKeyProgressIncrement(1);
        horizWings.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = (progress * 45 - 90) + "°";
                horizWingsText.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                execAction("setHorizontalSwing", getParams((seekBar.getProgress() * 45 - 90)));
            }
        });

        CheckBox auto_horizontal = dialogView.findViewById(R.id.ac_cb_horizontal);
        auto_horizontal.setChecked(auto);
        auto_horizontal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                execAction("setVerticalSwing", getParams("auto"));
                horizWings.setEnabled(false);
                horizWingsText.setVisibility(View.INVISIBLE);
                horizWings.setProgress(1);
            } else {
                execAction("setVerticalSwing", getParams(0));
                horizWings.setEnabled(true);
                horizWingsText.setVisibility(View.VISIBLE);
                horizWings.setProgress(1);
                String newText = "0°";
                horizWingsText.setText(newText);
            }
        });
    }
}
