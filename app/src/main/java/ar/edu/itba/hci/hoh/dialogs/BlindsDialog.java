package ar.edu.itba.hci.hoh.dialogs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.elements.Device;

class BlindsDialog extends DeviceDialog {
    private AlertDialog dialog;
    private Button openBtn, closeBtn;
    private ProgressBar pB;
    private TextView statusText;

    BlindsDialog(Fragment fragment, Device device) {
        super(fragment, device);
    }

    AlertDialog openDialog() {
        LayoutInflater inflater = fragment.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_device_blinds, null);
        this.dialog = new AlertDialog.Builder(fragment.getContext()).setView(dialogView).create();
//        this.dialog.setOnDismissListener(dialog -> DialogCreator.closeDialog());
        setDialogHeader(dialogView);

        openBtn = dialogView.findViewById(R.id.window_open);
        closeBtn = dialogView.findViewById(R.id.window_close);
        statusText = dialogView.findViewById(R.id.window_status_text);
        statusText.setVisibility(View.INVISIBLE);
        setButtons();

        pB = dialogView.findViewById(R.id.window_bar);
        pB.setMax(100);
        pB.setProgressTintList(ContextCompat.getColorStateList(fragment.getContext(), R.color.colorPrimary));
        pB.setProgress(device.getState().getLevel());

        openBtn.setOnClickListener(v -> {
            device.getState().setStatus("opening");
            setButtons();
            execAction("open");
        });

        closeBtn.setOnClickListener(v -> {
            device.getState().setStatus("closing");
            setButtons();
            execAction("close");
        });

        this.dialog.show();
        return this.dialog;
    }

    void closeDialog() {
        super.cancelTimer();
        dialog.dismiss();
    }

    private void setButtons() {
        String status = device.getState().getStatus();
        switch (status) {
            case "opened":
                openBtn.setEnabled(true);
                closeBtn.setEnabled(true);
                toggleButton(openBtn, true);
                toggleButton(closeBtn, false);
                statusText.setVisibility(View.INVISIBLE);
                break;
            case "closed":
                openBtn.setEnabled(true);
                closeBtn.setEnabled(true);
                toggleButton(openBtn, false);
                toggleButton(closeBtn, true);
                statusText.setVisibility(View.INVISIBLE);
                break;
            case "opening":
                toggleButton(closeBtn, false);
                closeBtn.setEnabled(true);
                openBtn.setEnabled(false);
                statusText.setText(fragment.getResources().getString(R.string.device_status_opening));
                statusText.setVisibility(View.VISIBLE);
                break;
            default:
                toggleButton(openBtn, false);
                openBtn.setEnabled(true);
                closeBtn.setEnabled(false);
                statusText.setText(fragment.getResources().getString(R.string.device_status_closing));
                statusText.setVisibility(View.VISIBLE);
                break;
        }

    }

    void reloadData() {
        Log.e(MainActivity.LOG_TAG, "actualizando");
//        setButtons();
        int newlevel = device.getState().getLevel();
        int currentLevel = pB.getProgress();
        pB.setProgress(newlevel);
        if (newlevel != currentLevel) {
            if (newlevel == 0) {
                device.getState().setStatus("opened");
                setButtons();
            } else if (newlevel == 100) {
                device.getState().setStatus("closed");
                setButtons();
            }
        }
    }
}
