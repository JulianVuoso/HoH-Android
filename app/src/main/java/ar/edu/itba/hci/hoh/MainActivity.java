package ar.edu.itba.hci.hoh;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ar.edu.itba.hci.hoh.elements.Category;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.notifications.DatabaseHandler;
import ar.edu.itba.hci.hoh.notifications.LocalDatabase;
import ar.edu.itba.hci.hoh.notifications.NotificationWorker;
import ar.edu.itba.hci.hoh.ui.devices.DevicesFragment;
import ar.edu.itba.hci.hoh.ui.room.RoomFragment;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "ar.edu.itba.hci.hoh";

    private static MainActivityData mainActivityData;
    private static MainActivity instance;

    public static List<Category> categories = new ArrayList<>();

    private static List<RestartListener> restartListeners = new ArrayList<>();

    private static boolean notifications;
    private static int notificationsTime = 15;

    public static int getNotificationsTime() {
        return notificationsTime;
    }

    public static void setNotificationsTime(int notificationsTime) {
        MainActivity.notificationsTime = notificationsTime;
    }

    public static boolean isNotifications() {
        return notifications;
    }

    public static void setNotifications(boolean notifications) {
        MainActivity.notifications = notifications;
        LocalDatabase db = LocalDatabase.getInstance(instance.getApplicationContext());

        if (notifications) {
            PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(NotificationWorker.class, notificationsTime, TimeUnit.MINUTES)
                    .addTag(NotificationWorker.TAG)
                    .build();
            WorkManager.getInstance().enqueueUniquePeriodicWork(NotificationWorker.NAME, ExistingPeriodicWorkPolicy.KEEP, request);
//
//            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class)
//                    .setInitialDelay(60, TimeUnit.SECONDS)
//                    .addTag(NotificationWorker.TAG)
//                    .build();
//            WorkManager.getInstance().enqueueUniqueWork(NotificationWorker.NAME, ExistingWorkPolicy.KEEP, request);
        } else {
            WorkManager.getInstance().cancelUniqueWork(NotificationWorker.NAME);
            DatabaseHandler.deleteAll(db);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                MyApplication.makeToast(this.getResources().getString(R.string.search_button_message));
                break;
            case R.id.kebab_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.kebab_about_us:
                aboutUsDialog(item.getActionView());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // TODO: REVISAR MENSAJE I/Choreographer: Skipped 32 frames!  The application may be doing too much work on its main thread.
    // TODO: CORERGIR SVG DE HORNO Y BLINDS PORQUE LES FALTA MARGEN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;
        mainActivityData = new MainActivityData();

        if (categories.size() == 0) getCategoryList();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_rooms, R.id.navigation_devices, R.id.navigation_home, R.id.navigation_routines)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp();
    }

    private static void getCategoryList() {
        mainActivityData.getDeviceTypes().observe(instance, deviceTypes -> {
            if (deviceTypes != null) {
                Category lights = new Category(instance.getResources().getString(R.string.category_lights), R.drawable.ic_light_black_60dp);
                // TODO: EN EL INFORME, PONER QUE DOORS & WINDOWS ERA MUY LARGO Y QUE DEBERIAMOS CAMBIARLO TAMBIEN EN LA WEB
                Category openings = new Category(instance.getResources().getString(R.string.category_doors_blinds), R.drawable.ic_door_black_60dp);
                Category ac = new Category(instance.getResources().getString(R.string.category_ac), R.drawable.ic_ac_60dp);
                Category appliances = new Category(instance.getResources().getString(R.string.category_appliances), R.drawable.ic_fridge_black_60dp);
                Category entertainment = new Category(instance.getResources().getString(R.string.category_entertainment), R.drawable.ic_entertainment_black_60dp);

                for (DeviceType type : deviceTypes) {
                    switch (type.getName()) {
                        case "lamp":    lights.addType(type);
                            break;
                        case "door":
                        case "blinds":  openings.addType(type);
                            break;
                        case "ac":      ac.addType(type);
                            break;
                        case "refrigerator":
                        case "oven":    appliances.addType(type);
                            break;
                        case "speaker": entertainment.addType(type);
                            break;
                    }
                }
                categories.clear();
                categories.add(lights);
                categories.add(openings);
                categories.add(ac);
                categories.add(appliances);
                categories.add(entertainment);
                // Por las dudas, le aviso a los dos lugares donde usan categories que se actualizo la lista
                DevicesFragment.notifyAdapter();
                RoomFragment.notifyAdapter();
                Log.v(MainActivity.LOG_TAG, "ACTUALICE CATEGORIAS");
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mainActivityData.cancelRequests();
    }

    public static void reloadCategories() {
        if (categories.size() == 0) {
            mainActivityData.reloadTypes();
            getCategoryList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (categories.size() == 0) {
            mainActivityData.reloadTypes();
            getCategoryList();
        }
    }

    public static void setRestartListener(RestartListener listener) {
        restartListeners.add(listener);
    }

    public static void removeRestartListener(RestartListener listener) {
        restartListeners.remove(listener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (restartListeners != null) {
            Log.e(LOG_TAG, "Le mando al listener en MainActivity");
            for (RestartListener listener : restartListeners)
                listener.onRestartActivity();
        }
    }

    private void aboutUsDialog(View view) {
        View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_about_us, (ViewGroup) view);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        ImageButton closeDialogButton = dialogView.findViewById(R.id.close_dialog_about_us);
        if (closeDialogButton != null) closeDialogButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
