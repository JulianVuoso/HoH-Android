package ar.edu.itba.hci.hoh.dialogs;

import android.util.Log;
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

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.elements.Device;

class AcDialog extends DeviceDialog {
    private AlertDialog dialog;
    private Button modeCold, modeVent, modeHeat;
    private Switch swAc;
    private SeekBar tempAc, fanSpeed, vertWings, horizWings;
    private TextView tempAcText, fanSpeedText, vertWingsText, horizWingsText;
    private CheckBox auto_speed, auto_vertical, auto_horizontal;

    AcDialog(Fragment fragment, Device device) {
        super(fragment, device);
    }

    AlertDialog openDialog() {
        LayoutInflater inflater = fragment.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_device_ac, null);
        this.dialog = new AlertDialog.Builder(fragment.getContext()).setView(dialogView).create();
//        this.dialog.setOnDismissListener(dialog -> DialogCreator.closeDialog());
        setDialogHeader(dialogView);

        swAc = dialogView.findViewById(R.id.ac_switch);
        swAc.setChecked(device.getState().getStatus().equals("on"));
        swAc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            device.getState().setStatus((isChecked) ? "on" : "off");
            execAction((isChecked) ? "turnOn" : "turnOff");
        });

        initTemperature(dialogView);
        initFanSpeed(dialogView);
        initVertWings(dialogView);
        initHorizWings(dialogView);

        initButtons(dialogView);
        modeCold.setOnClickListener(v -> {
            toggleButton(modeCold, true); toggleButton(modeVent, false); toggleButton(modeHeat, false);
            device.getState().setMode("cold");
            execAction("setMode", getParams("cold"));
        });
        modeVent.setOnClickListener(v -> {
            toggleButton(modeCold, false); toggleButton(modeVent, true); toggleButton(modeHeat, false);
            device.getState().setMode("fan");
            execAction("setMode", getParams("fan"));
        });
        modeHeat.setOnClickListener(v -> {
            toggleButton(modeCold, false); toggleButton(modeVent, false); toggleButton(modeHeat, true);
            device.getState().setMode("heat");
            execAction("setMode", getParams("heat"));
        });

        this.dialog.show();

        return this.dialog;
    }

    void closeDialog() {
        super.cancelTimer();
        dialog.dismiss();
    }

    private void initButtons(View dialogView) {
        modeCold = dialogView.findViewById(R.id.ac_mode_cold);
        modeVent = dialogView.findViewById(R.id.ac_mode_vent);
        modeHeat = dialogView.findViewById(R.id.ac_mode_heat);
        setButtons();
    }

    private void setButtons() {
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
        tempAc = dialogView.findViewById(R.id.ac_temp_bar);
        tempAcText = dialogView.findViewById(R.id.ac_temp_text);
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
                device.getState().setTemperature(seekBar.getProgress() + 18);
                execAction("setTemperature", getParams(seekBar.getProgress() + 18));
            }
        });
    }

    private void initFanSpeed(View dialogView) {
        fanSpeed = dialogView.findViewById(R.id.ac_fan_speed_bar);
        fanSpeedText = dialogView.findViewById(R.id.ac_fan_speed_text);
        auto_speed = dialogView.findViewById(R.id.ac_cb_speed);
        setFanSpeed();
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
                device.getState().setFanSpeed(String.valueOf((seekBar.getProgress() + 1) * 25));
                execAction("setFanSpeed", getParams(String.valueOf((seekBar.getProgress() + 1) * 25)));
            }
        });

        auto_speed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                device.getState().setFanSpeed("auto");
                execAction("setFanSpeed", getParams("auto"));
                fanSpeed.setEnabled(false);
                fanSpeedText.setVisibility(View.INVISIBLE);
                fanSpeed.setProgress(2);
            } else {
                device.getState().setFanSpeed(String.valueOf(50));
                execAction("setFanSpeed", getParams(String.valueOf(50)));
                fanSpeed.setEnabled(true);
                fanSpeedText.setVisibility(View.VISIBLE);
                fanSpeed.setProgress(1);
                String newText = "50%";
                fanSpeedText.setText(newText);
            }
        });
    }

    private void setFanSpeed() {
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
        auto_speed.setChecked(auto);
    }

    private void initVertWings(View dialogView) {
        vertWings = dialogView.findViewById(R.id.ac_vert_wings_bar);
        vertWingsText = dialogView.findViewById(R.id.ac_vert_wings_text);
        auto_vertical = dialogView.findViewById(R.id.ac_cb_vertical);
        setVertWings();
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
                device.getState().setVerticalSwing(String.valueOf((seekBar.getProgress() + 1) * 225 / 10));
                execAction("setVerticalSwing", getParams(String.valueOf((seekBar.getProgress() + 1) * 225 / 10)));
            }
        });

        auto_vertical.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                device.getState().setVerticalSwing("auto");
                execAction("setVerticalSwing", getParams("auto"));
                vertWings.setEnabled(false);
                vertWingsText.setVisibility(View.INVISIBLE);
                vertWings.setProgress(1);
            } else {
                device.getState().setVerticalSwing(String.valueOf(45));
                execAction("setVerticalSwing", getParams(String.valueOf(45)));
                vertWings.setEnabled(true);
                vertWingsText.setVisibility(View.VISIBLE);
                vertWings.setProgress(1);
                String newText = "45°";
                vertWingsText.setText(newText);
            }
        });
    }

    private void setVertWings() {
        boolean auto = device.getState().getVerticalSwing().equals("auto");
        if (auto) {
            vertWings.setEnabled(false);
            vertWingsText.setVisibility(View.INVISIBLE);
            vertWings.setProgress(1);
        } else {
            vertWings.setEnabled(true);
            vertWingsText.setVisibility(View.VISIBLE);
            vertWings.setProgress(Integer.valueOf(device.getState().getVerticalSwing()) * 10 / 220 - 1);
            String initFanSpeed = device.getState().getVerticalSwing() + "°";
            vertWingsText.setText(initFanSpeed);
        }
        auto_vertical.setChecked(auto);
    }

    private void initHorizWings(View dialogView) {
        horizWings = dialogView.findViewById(R.id.ac_horiz_wings_bar);
        horizWingsText = dialogView.findViewById(R.id.ac_horiz_wings_text);
        auto_horizontal = dialogView.findViewById(R.id.ac_cb_horizontal);
        setHorizWings();
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
                device.getState().setHorizontalSwing(String.valueOf(seekBar.getProgress() * 45 - 90));
                execAction("setHorizontalSwing", getParams(String.valueOf(seekBar.getProgress() * 45 - 90)));
            }
        });

        auto_horizontal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                device.getState().setHorizontalSwing("auto");
                execAction("setHorizontalSwing", getParams("auto"));
                horizWings.setEnabled(false);
                horizWingsText.setVisibility(View.INVISIBLE);
                horizWings.setProgress(2);
            } else {
                device.getState().setHorizontalSwing(String.valueOf(0));
                execAction("setHorizontalSwing", getParams(String.valueOf(0)));
                horizWings.setEnabled(true);
                horizWingsText.setVisibility(View.VISIBLE);
                horizWings.setProgress(2);
                String newText = "0°";
                horizWingsText.setText(newText);
            }
        });
    }

    private void setHorizWings() {
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
        auto_horizontal.setChecked(auto);
    }

    void reloadData() {
        Log.e(MainActivity.LOG_TAG, "actualizando");
        swAc.setChecked(device.getState().getStatus().equals("on"));
        setButtons();
        String initAcTemp = device.getState().getTemperature() + "°C";
        tempAcText.setText(initAcTemp);
        tempAc.setProgress(device.getState().getTemperature() - 18);
        setFanSpeed();
        setVertWings();
        setHorizWings();
    }
}
