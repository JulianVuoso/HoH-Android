package ar.edu.itba.hci.hoh.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.elements.Routine;

public class Api {
    private final static String LOG_TAG = "ar.edu.itba.hci.hoh.api.Api";
    private static Api instance;
    private static RequestQueue requestQueue;
    // Use IP 10.0.2.2 instead of 127.0.0.1 when running Android emulator in the
    // same computer that runs the API.
    private static String URL = "http://10.0.2.2:8080/api/";

    public static String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        Api.URL = URL;
    }

    private Api(Context context) {
        requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
    }

    public static synchronized Api getInstance(Context context) {
        if (instance == null) {
            instance = new Api(context);
        }
        return instance;
    }

    /** ROOM **/
    /* Create a new room */
    public String addRoom(Room room, Response.Listener<Room> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Room> request =
                new GsonRequest<>(Request.Method.POST, url, room, "result", new TypeToken<Room>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Update an existing room */
    public String modifyRoom(Room room, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + room.getId();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Boolean> request =
                new GsonRequest<>(Request.Method.PUT, url, room, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Delete an existing room */
    public String deleteRoom(String id, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + id;
        GsonRequest<Object, Boolean> request =
                new GsonRequest<>(Request.Method.DELETE, url, null, "result", new TypeToken<Boolean>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Retrieve a specific room */
    public String getRoom(String id, Response.Listener<Room> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + id;
        GsonRequest<Object, Room> request =
                new GsonRequest<>(Request.Method.GET, url, null, "result", new TypeToken<Room>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Retrieve all rooms */
    public String getRooms(Response.Listener<ArrayList<Room>> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/";
        GsonRequest<Object, ArrayList<Room>> request =
                new GsonRequest<>(Request.Method.GET, url, null, "result", new TypeToken<ArrayList<Room>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    /* Retrieve devices from a specific room */
    public String getDevicesFromRoom(String id, Response.Listener<ArrayList<Device>> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + id + "/devices";
        GsonRequest<Object, ArrayList<Device>> request =
                new GsonRequest<>(Request.Method.GET, url, null, "result", new TypeToken<ArrayList<Device>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /** DEVICE **/
    /* Create a new room */
    public String addDevice(Device device, Response.Listener<Device> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Device, Device> request =
                new GsonRequest<>(Request.Method.POST, url, device, "result", new TypeToken<Device>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Update an existing device */
    public String modifyDevice(Device device, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + device.getId();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Device, Boolean> request =
                new GsonRequest<>(Request.Method.PUT, url, device, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Delete an existing device */
    public String deleteDevice(String id, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + id;
        GsonRequest<Object, Boolean> request =
                new GsonRequest<>(Request.Method.DELETE, url, null, "result", new TypeToken<Boolean>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Retrieve a specific device */
    public String getDevice(String id, Response.Listener<Device> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + id;
        GsonRequest<Object, Device> request =
                new GsonRequest<>(Request.Method.GET, url, null, "result", new TypeToken<Device>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Retrieve all devices */
    public String getDevices(Response.Listener<ArrayList<Device>> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/";
        GsonRequest<Object, ArrayList<Device>> request =
                new GsonRequest<>(Request.Method.GET, url, null, "devices", new TypeToken<ArrayList<Device>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    /* Retrieve devices from a specific deviceType */
    public String getDevicesFromType(String id, Response.Listener<ArrayList<Device>> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + "devicetypes/" + id;
        GsonRequest<Object, ArrayList<Device>> request =
                new GsonRequest<>(Request.Method.GET, url, null, "result", new TypeToken<ArrayList<Device>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    // TODO: ME DEVUELVE STRINGS O BOOLEANS SEGUN LA ACCION. COMO UNIFICO?
    /* Execute an action in a specific device with params {[]} */
    public String execAction(String id, String action, String[] param, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + id + "/" + action;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        GsonRequest<String[], Boolean> request =
                new GsonRequest<>(Request.Method.PUT, url, param, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Execute an action in a specific device with NO params {} */
    public String execAction(String id, String action, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + id + "/" + action;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        // TODO: CHECK SI ES {} o []
        GsonRequest<String[], Boolean> request =
                new GsonRequest<>(Request.Method.PUT, url, new String[0], "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /** DEVICE TYPE **/
    /* Retrieve a specific deviceType */
    public String getDeviceType(String id, Response.Listener<DeviceType> listener, Response.ErrorListener errorListener) {
        String url = URL + "devicetypes/" + id;
        GsonRequest<Object, DeviceType> request =
                new GsonRequest<>(Request.Method.GET, url, null, "result", new TypeToken<DeviceType>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Retrieve all deviceTypes */
    public String getDeviceTypes(Response.Listener<ArrayList<DeviceType>> listener, Response.ErrorListener errorListener) {
        String url = URL + "devicetypes/";
        GsonRequest<Object, ArrayList<DeviceType>> request =
                new GsonRequest<>(Request.Method.GET, url, null, "result", new TypeToken<ArrayList<DeviceType>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    /** ROUTINES **/
    /* Retrieve a specific routine */
    public String getRoutine(String id, Response.Listener<Routine> listener, Response.ErrorListener errorListener) {
        String url = URL + "routines/" + id;
        GsonRequest<Object, Routine> request =
                new GsonRequest<>(Request.Method.GET, url, null, "routine", new TypeToken<Routine>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    /* Retrieve all routines */
    public String getRoutines(Response.Listener<ArrayList<Routine>> listener, Response.ErrorListener errorListener) {
        String url = URL + "routines/";
        GsonRequest<Object, ArrayList<Routine>> request =
                new GsonRequest<>(Request.Method.GET, url, null, "result", new TypeToken<ArrayList<Routine>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    // TODO: ME DEVUELVE UN VECTOR DE STRINGS Y BOOLEANS. COMO UNIFICO?
    /* Execute a specific routine */
    public String execRoutine(String id, Response.Listener<ArrayList<Object>> listener, Response.ErrorListener errorListener) {
        String url = URL + "routines/" + id + "/execute";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        // TODO: CHECK SI ES {} o []
        GsonRequest<String, ArrayList<Object>> request =
                new GsonRequest<>(Request.Method.PUT, url, "[]", "result", new TypeToken<ArrayList<Object>>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public void cancelRequest(String uuid) {
        if ((uuid != null) && (requestQueue != null)) {
            requestQueue.cancelAll(uuid);
        }
    }

    public Error handleError(VolleyError error) {
        Error response = null;
        boolean handled = false;

        NetworkResponse networkResponse = error.networkResponse;
        if ((networkResponse != null) && (error.networkResponse.data != null)) {
            try {
                String json = new String(
                        error.networkResponse.data,
                        HttpHeaderParser.parseCharset(networkResponse.headers));

                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.getJSONObject("error").toString();

                Gson gson = new Gson();
                response = gson.fromJson(json, Error.class);
                handled = true;
            } catch (JSONException | UnsupportedEncodingException e) {
            }
        }

        if (!handled) {
            Log.e(LOG_TAG, error.toString());

            ArrayList<String> description = new ArrayList<>(Arrays.asList(error.getMessage()));
            response = new Error(6, description);
        }

        return response;
    }
}

