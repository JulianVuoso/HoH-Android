package ar.edu.itba.hci.hoh.ui.routines;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Routine;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;
import ar.edu.itba.hci.hoh.ui.routines.RoutinesAdapter;
import ar.edu.itba.hci.hoh.ui.routines.RoutinesViewModel;

public class RoutinesFragment extends Fragment {

    private RoutinesViewModel routinesViewModel;

    private RecyclerView rvRoutines;
    private GridLayoutManager gridLayoutManager;
    private RoutinesAdapter adapter;

    private List<Routine> routines = new ArrayList<>();

    private CardView emptyCard;

    private static List<String> requestTag = new ArrayList<>();

    // TODO: GUARDAR LAS PNG DE LAS RUTINAS
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routinesViewModel = ViewModelProviders.of(this).get(RoutinesViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_routines, container, false);

        rvRoutines = root.findViewById(R.id.rv_routines);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        gridLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
        rvRoutines.setLayoutManager(gridLayoutManager);
        adapter = new RoutinesAdapter(routines, new OnItemClickListener<Routine>() {
            @Override
            public void onItemClick(Routine routine) {
                // TODO: OPEN CONFIRMATION DIALOG TO EXECUTE ROUTINE
                executeRoutine(routine, getContext());
            }
        });
        rvRoutines.setAdapter(adapter);

        routines.clear();
        getRoutineList();
        emptyCard = root.findViewById(R.id.empty_routines_card);
        TextView tvEmptyRoom = emptyCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_routine_list);

        return root;
    }

    private void getRoutineList() {
        // TODO: SACAR CONTEXT
        requestTag.add(Api.getInstance(this.getContext()).getRoutines(new Response.Listener<ArrayList<Routine>>() {
            @Override
            public void onResponse(ArrayList<Routine> response) {
                routines.addAll(response);
                if (!routines.isEmpty())
                    emptyCard.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                Log.v(MainActivity.LOG_TAG, "ACTUALICE ROUTINES");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: VER QUE HACER CON ERROR
                Log.e(MainActivity.LOG_TAG, String.format("ERROR AL ACTUALIZAR ROUTINES. El error es %s", error.toString()));
            }
        }));
    }

    public static void executeRoutine(final Routine routine, final Context context) {
        requestTag.add(Api.getInstance(context).execRoutine(routine.getId(), new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                Toast.makeText(context, String.format("Routine %s executed", routine.getName()), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error executing routine", Toast.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO: SACAR CONTEXT
        for (String request : requestTag)
            Api.getInstance(this.getContext()).cancelRequest(request);
    }
}