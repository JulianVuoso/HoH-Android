package ar.edu.itba.hci.hoh;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.arch.core.util.Function;
import androidx.core.content.ContextCompat;

import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.elements.DeviceState;
import ar.edu.itba.hci.hoh.elements.Result;
import ar.edu.itba.hci.hoh.repositories.DeviceRepository;
import ar.edu.itba.hci.hoh.repositories.DeviceTypeRepository;
import ar.edu.itba.hci.hoh.repositories.RoomRepository;
import ar.edu.itba.hci.hoh.repositories.RoutineRepository;

public class MyApplication extends Application {
    private static final float toastHorizontalMargin = 0;
    private static final float toastVerticalMargin = (float) 0.08;

    private static final String connectionError = "java.net.ConnectException:";
    private static final String socketError = "java.net.SocketException:";
    private static final String timeoutError = "com.android.volley.TimeoutError";
    private static final String httpTrafficError = "java.io.IOException:";

    private static MyApplication instance;
    private RoomRepository roomRepository;
    private DeviceRepository deviceRepository;
    private DeviceTypeRepository deviceTypeRepository;
    private RoutineRepository routineRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create API instance, no more context needed
        Api.getInstance(this);

        roomRepository = RoomRepository.getInstance(this);
        deviceRepository = DeviceRepository.getInstance(this);
        deviceTypeRepository = DeviceTypeRepository.getInstance(this);
        routineRepository = RoutineRepository.getInstance(this);
        instance = this;
    }

    public synchronized static MyApplication getInstance() {
        return instance;
    }

    public RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    public DeviceTypeRepository getDeviceTypeRepository() {
        return deviceTypeRepository;
    }

    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }

    public static void makeToast(String message) {
        Toast toast = Toast.makeText(instance, message, Toast.LENGTH_SHORT);
        toast.setMargin(toastHorizontalMargin, toastVerticalMargin);
        toast.show();
    }

    public static void makeToast(Error error) {
        if (error == null || error.getDescription() == null || error.getDescription().get(0) == null) return;

        Toast toast;
        String errorMessage = error.getDescription().get(0);
        if (errorMessage.startsWith(connectionError) || errorMessage.startsWith(socketError)) // FAILED TO CONNECT TO URL
            toast = Toast.makeText(instance, String.format("%s %s", instance.getResources().getString(R.string.error_no_connection), Api.getURL()), Toast.LENGTH_SHORT);
        else if (errorMessage.startsWith(timeoutError)) // TIMEOUT ERROR
            toast = Toast.makeText(instance, instance.getResources().getString(R.string.error_timeout), Toast.LENGTH_SHORT);
        else if (errorMessage.startsWith(httpTrafficError)) // HTTP TRAFFIC ERROR
            toast = Toast.makeText(instance, instance.getResources().getString(R.string.error_http_traffic), Toast.LENGTH_SHORT);
        else // UNRECOGNIZED ERROR
            toast = Toast.makeText(instance, errorMessage, Toast.LENGTH_SHORT);
        toast.setMargin(toastHorizontalMargin, toastVerticalMargin);
        toast.show();
    }

    public static int getDrawableFromString(String drawableName) {
        return instance.getResources().getIdentifier(drawableName, "drawable", instance.getPackageName());
    }

    public static float getDeviceCardWidth() {
        return instance.getResources().getDimension(R.dimen.device_card_width);
    }

    public static int getCardBackgroundColor(DeviceState state) {
        if (state == null || state.getStatus() == null) return ContextCompat.getColor(instance, R.color.colorDevCardBackgroundLight);

        switch (state.getStatus()) {
            case "opened":
            case "on":
            case "playing":
                return ContextCompat.getColor(instance, R.color.colorDevCardBackgroundLight);
            case "off":
            case "paused":
            case "closing":
            case "opening":
                return ContextCompat.getColor(instance, R.color.colorDevCardBackgroundMedium);
            case "closed":
                if (state.getLock() != null && state.getLock().equals("unlocked"))
                    return ContextCompat.getColor(instance, R.color.colorDevCardBackgroundMedium);
        }
        // For closed && !unlocked AND for stopped
        return ContextCompat.getColor(instance, R.color.colorDevCardBackgroundDark);
    }

    public static String getDeviceStatusString(DeviceState state) {
        if (state == null || state.getStatus() == null) return null;

        switch (state.getStatus()) {
            case "on":      return instance.getResources().getString(R.string.device_status_on);
            case "off":     return instance.getResources().getString(R.string.device_status_off);
            case "opened":  return instance.getResources().getString(R.string.device_status_open);
            case "opening": return instance.getResources().getString(R.string.device_status_opening);
            case "closing": return instance.getResources().getString(R.string.device_status_closing);
            case "closed":
                        if (state.getLock() != null && state.getLock().equals("locked"))
                            return instance.getResources().getString(R.string.device_status_locked);
                        else
                            return instance.getResources().getString(R.string.device_status_closed);
            case "playing": return instance.getResources().getString(R.string.device_status_playing);
            case "paused":  return instance.getResources().getString(R.string.device_status_paused);
            case "stopped": return instance.getResources().getString(R.string.device_status_stopped);
        }
        return null;
    }

    public static <T> Function<Result<T>, T> getTransformFunction() {
        return result -> {
            Error error = result.getError();
            if (error != null)
                MyApplication.makeToast(error);
            return result.getResult();
        };
    }
}
