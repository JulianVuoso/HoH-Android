package ar.edu.itba.hci.hoh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.elements.Category;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.ui.devices.DevicesFragment;
import ar.edu.itba.hci.hoh.ui.room.RoomFragment;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "ar.edu.itba.hci.hoh";

    private static MainActivityData mainActivityData;
    private static MainActivity instance;

    public static List<Category> categories = new ArrayList<>();
    private static String requestTag;

    private static boolean notifications;

    public static boolean isNotifications() {
        return notifications;
    }

    public static void setNotifications(boolean notifications) {
        MainActivity.notifications = notifications;
        if (notifications) {
            // TODO: ENABLE WORK MANAGER
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
                Toast.makeText(this, "Search Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.kebab_settings:
//                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.kebab_help:
                Toast.makeText(this, "Help Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.kebab_website:
                Toast.makeText(this, "Website Selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // TODO: REVISAR MENSAJE I/Choreographer: Skipped 32 frames!  The application may be doing too much work on its main thread.

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
                Category lights = new Category("Lights", R.drawable.ic_light_black_60dp);
                Category openings = new Category("Doors & Blinds", R.drawable.ic_door_black_60dp);
                Category ac = new Category("Air Conditioning", R.drawable.ic_door_black_60dp); // TODO: CHANGE CATEGORY PIC
                Category appliances = new Category("Appliances", R.drawable.ic_fridge_black_60dp);
                Category entertainment = new Category("Entertainment", R.drawable.ic_entertainment_black_60dp);

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
        Api.getInstance(null).cancelRequest(requestTag);
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
}
