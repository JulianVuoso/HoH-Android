package ar.edu.itba.hci.hoh.notifications;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;

import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.elements.Device;

import static ar.edu.itba.hci.hoh.notifications.NotificationCreator.createNotification;
import static ar.edu.itba.hci.hoh.notifications.NotificationCreator.showNotification;

public class NotificationWorker extends Worker {

    public final static String TAG = "ar.edu.itba.hoh.notificationworker";
    public final static String NAME = TAG;

    private Context context;
    private LocalDatabase db;
    private static int notId = 0;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
        this.db = LocalDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Api.getInstance(context).getDevices(response -> {
            ArrayList<Device> newDevices =  new ArrayList<>();
            ArrayList<Device> changedDevices =  new ArrayList<>();
            ArrayList<Tuple> deletedTuples;
            ArrayList<String> currentIds = new ArrayList<>();
            boolean flag = DatabaseHandler.getTotalCount(db) > 0;

            Tuple tuple;
            if (response == null) return;
            for (Device device: response) {
                tuple = DatabaseHandler.getById(db, device.getId());

                if (tuple == null) {
                    /* Add to new devices */
                    newDevices.add(device);

                    /* Update database */
                    DatabaseHandler.insert(db, new Tuple(device.getId(), device.getState().toString(), device.getName()));
                } else if (!tuple.getState().equals(device.getState().toString())) {
                    /* Add to changed device list */
                    changedDevices.add(device);

                    /* Update database */
                    DatabaseHandler.insert(db, new Tuple(device.getId(), device.getState().toString(), device.getName()));
                }
                currentIds.add(device.getId());
            }
            deletedTuples = (ArrayList<Tuple>) DatabaseHandler.getMissing(db, currentIds.toArray(new String[0]));

            /* Creates notifications for new and changed devices only if not beginning */
            if (flag) {
                notifyNewDevices(newDevices);
                notifyChangedDevices(changedDevices);
                notifyDeletedDevices(deletedTuples);
            }

        }, error -> {

        });

        return Result.success();
    }

    private void notifyNewDevices(ArrayList<Device> devices) {
        String content;
        Notification notification;

        for (Device device: devices) {
            content = device.getName() + " " + context.getResources().getString(R.string.new_notification) + " " + device.getRoom().getName() + "\n";
            notification = createNotification(context, R.string.new_notification_title, content, device.getRoom().getId());
            showNotification(context, notId++, notification);
        }
    }

    private void notifyChangedDevices(ArrayList<Device> devices) {
        String content;
        Notification notification;

        for (Device device: devices) {
            content = device.getName() + " " + context.getResources().getString(R.string.changed_notification) + "\n";
            notification = createNotification(context, R.string.changed_notification_title, content, device.getRoom().getId());
            showNotification(context, notId++, notification);

        }
    }

    private void notifyDeletedDevices(ArrayList<Tuple> devices) {
        if (devices == null) return;

        String content;
        Notification notification;

        for (Tuple device: devices) {
            DatabaseHandler.delete(db, device);
            content = device.getName() + " " + context.getResources().getString(R.string.deleted_notification) + "\n";
            notification = createNotification(context, R.string.deleted_notification_title, content, null);
            showNotification(context, notId++, notification);
        }
    }
}