package ar.edu.itba.hci.hoh.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Timer;
import java.util.TimerTask;

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.notifications.DatabaseHandler;
import ar.edu.itba.hci.hoh.notifications.LocalDatabase;
import ar.edu.itba.hci.hoh.notifications.Tuple;

abstract class DeviceDialog extends DataDialog {
    protected Fragment fragment;
    protected Device device;
    private Timer timer;

    DeviceDialog(Fragment fragment, Device dev) {
        this.fragment = fragment;
        this.device = dev;
        String type = dev.getType().getName();
        if (type.equals("speaker") || type.equals("blinds")) {
            this.timer = new Timer();
            this.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Cant use observe in background thread
                    Api.getInstance(fragment.getContext()).getDevice(device.getId(), response -> {
                        device = response;
                        reloadData();
                    }, error -> Log.e(MainActivity.LOG_TAG, "Error reloading"));
                }
            }, 0, 1000);
        }
    }

    protected void setDialogHeader(View dialogView) {
        TextView devName = dialogView.findViewById(R.id.dialog_dev_name);
        if (devName != null) devName.setText(device.getName());

        TextView devRoom = dialogView.findViewById(R.id.dialog_dev_room);
        if (devRoom != null) devRoom.setText(device.getRoom().getName());

        ImageView devImg = dialogView.findViewById(R.id.dialog_dev_img);
        if (devImg != null) {
            devImg.setImageResource(DeviceType.getDeviceTypeDrawable(device.getType()));
            devImg.setContentDescription(device.getType().getName());
        }

        ImageButton closeDialog = dialogView.findViewById(R.id.dialog_dev_close);
        if (closeDialog != null) closeDialog.setOnClickListener(v -> DialogCreator.closeDialog());

        ImageButton devFav = dialogView.findViewById(R.id.dialog_dev_fav);
        if (devFav != null) {
            setFavoriteIcon(devFav);
            devFav.setOnClickListener(v -> {
                device.getMeta().setFavorite(!device.getMeta().isFavorite());
                Device apiDevice = new Device(device.getId(), device.getName(), device.getMeta());
                dialogData.modifyDevice(apiDevice).observe(fragment, result -> {
                    if (result != null) {
                        setFavoriteIcon(devFav);
                    }
                });
            });

        }
    }

    abstract void reloadData();

    protected void cancelTimer() {
        Log.e(MainActivity.LOG_TAG, "cancelando timer????");
        if (timer != null) {
            Log.e(MainActivity.LOG_TAG, "cancelando timer");
            timer.cancel();
        }
    }

    private void setFavoriteIcon(ImageButton button) {
        if (device.getMeta().isFavorite()) {
            button.setImageResource(R.drawable.ic_star_black_24dp);
            button.setContentDescription(fragment.getResources().getString(R.string.dialog_favorite_description));
        } else {
            button.setImageResource(R.drawable.ic_star_border_black_24dp);
            button.setContentDescription(fragment.getResources().getString(R.string.dialog_not_favorite_description));
        }
    }

    protected void toggleButton(Button button, boolean state) {
        if (state)
            button.setBackgroundTintList(ContextCompat.getColorStateList(fragment.getContext(), R.color.colorPrimary));
        else
            button.setBackgroundTintList(ContextCompat.getColorStateList(fragment.getContext(), R.color.colorOffButton));
        button.setClickable(!state);
    }

    protected void execAction(String action) {
        dialogData.execAction(device.getId(), action).observe(fragment, getDefaultObserver());
    }

    protected void execAction(String action, String[] param) {
        dialogData.execAction(device.getId(), action, param).observe(fragment, getDefaultObserver());
    }

    protected void execAction(String action, Integer[] param) {
        dialogData.execAction(device.getId(), action, param).observe(fragment, getDefaultObserver());
    }

    protected void execAction(String action, Observer<Boolean> observer) {
        dialogData.execAction(device.getId(), action).observe(fragment, observer);
    }

    protected void execAction(String action, String[] param, Observer<String> observer) {
        dialogData.execAction(device.getId(), action, param).observe(fragment, observer);
    }

    protected void updateLocalDatabase() {
        LocalDatabase db = LocalDatabase.getInstance(fragment.getContext());
        DatabaseHandler.insert(db, new Tuple(device.getId(), device.getState().toString(), device.getName()));
    }

    protected String[] getParams(String param) {
        String[] ret = new String[1];
        ret[0] = param;
        return ret;
    }

    protected Integer[] getParams(int param) {
        Integer[] ret = new Integer[1];
        ret[0] = param;
        return ret;
    }

    private <T> Observer<T> getDefaultObserver() {
        return result -> {
            if (result != null) {
                dialogData.getDevice(device.getId()).observe(fragment, newDevice -> {
                    device = newDevice;
                    reloadData();
                    updateLocalDatabase();
                });
            }
        };
    }
}
