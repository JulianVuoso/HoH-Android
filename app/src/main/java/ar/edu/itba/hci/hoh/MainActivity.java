package ar.edu.itba.hci.hoh;

import android.content.Context;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Category;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.api.Error;
import ar.edu.itba.hci.hoh.ui.devices.DevicesFragment;
import ar.edu.itba.hci.hoh.ui.room.RoomFragment;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    public static final String LOG_TAG = "ar.edu.itba.hci.hoh";

    public static List<Category> categories = new ArrayList<>();
    private static String requestTag;

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
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: CHECK QUE ESTE GARANTIZADO QUE SE EJECUTA ESTA PRIMERO
        // CREATE AN API INSTANCE WITH A CONTEXT, NO MORE CONTEXT NEEDED. FILL CATEGORY LIST
        Api.getInstance(this.getApplicationContext());
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

        // For UP navigation add backstack listener
        getSupportFragmentManager().addOnBackStackChangedListener(this);

    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    private void shouldDisplayHomeUp() {
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(canGoBack);
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return Navigation.findNavController(this, R.id.main_fragment).navigateUp() || super.onSupportNavigateUp();
        getSupportFragmentManager().popBackStack();
        return true;
    }

    public void handleError(VolleyError error) {
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
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException e) {
            }
        }

        if (handled) {
            String text = getResources().getString(R.string.error_message);
            if (response != null)
                text += " " + response.getDescription().get(0);

            Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
        }
        else
            Log.e(LOG_TAG, error.toString());
    }

    private static void getCategoryList() {
        requestTag = Api.getInstance(null).getDeviceTypes(new Response.Listener<ArrayList<DeviceType>>() {
            @Override
            public void onResponse(ArrayList<DeviceType> response) {
                Category lights = new Category("Lights", R.drawable.ic_light_black_60dp);
                Category openings = new Category("Doors & Blinds", R.drawable.ic_door_black_60dp);
                Category ac = new Category("Air Conditioning", R.drawable.ic_door_black_60dp); // TODO: CHANGE CATEGORY PIC
                Category appliances = new Category("Appliances", R.drawable.ic_fridge_black_60dp);
                Category entertainment = new Category("Entertainment", R.drawable.ic_entertainment_black_60dp);

                for (DeviceType type : response) {
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                MainActivity.handleError(error);
                // TODO: VER QUE HACER CON ERROR
                Log.e(MainActivity.LOG_TAG, String.format("ERROR AL ACTUALIZAR CATEGORIAS. El error es %s", error.toString()));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Api.getInstance(null).cancelRequest(requestTag);
    }

    public static int getDrawableFromString(Context context, String imgName) {
        return context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
    }
}
