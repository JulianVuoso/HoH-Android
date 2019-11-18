package ar.edu.itba.hci.hoh.ui.routines;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.elements.Routine;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.ui.GridLayoutAutofitManager;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class RoutinesFragment extends Fragment {

    private RoutinesViewModel routinesViewModel;

    private RecyclerView rvRoutines;
    private GridLayoutManager gridLayoutManager;
    private RoutinesAdapter adapter;

    private List<Routine> routines = new ArrayList<>();

    private CardView emptyCard;

    private static List<String> requestTag = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routinesViewModel = ViewModelProviders.of(this).get(RoutinesViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_routines, container, false);

        rvRoutines = root.findViewById(R.id.rv_routines);
        gridLayoutManager = new GridLayoutAutofitManager(this.getContext(), (int) getResources().getDimension(R.dimen.img_card_width), GridLayoutManager.VERTICAL, false);
        rvRoutines.setLayoutManager(gridLayoutManager);
        adapter = new RoutinesAdapter(routine -> {
            // TODO: OPEN CONFIRMATION DIALOG TO EXECUTE ROUTINE
            executeRoutine(routine);
        });
        rvRoutines.setAdapter(adapter);
        getRoutineList();
        emptyCard = root.findViewById(R.id.empty_routines_card);
        TextView tvEmptyRoom = emptyCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_routine_list);

        return root;
    }

    private void getRoutineList() {
        routinesViewModel.getRoutines().observe(this, routines -> {
            if (routines != null && ! routines.isEmpty())
                emptyCard.setVisibility(View.GONE);
            adapter.setRoutines(routines);
            Log.v(MainActivity.LOG_TAG, "ACTUALICE ROUTINES");
        });
    }

    // TODO: CAMBIAR FETCHEO DE RESULTADOS
    private void executeRoutine(Routine routine) {
        routinesViewModel.execRoutine(routine.getId()).observe(this, result -> {
            if (result != null)
                MyApplication.makeToast(String.format("Routine %s executed", routine.getName()));
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO: SACAR CONTEXT
        for (String request : requestTag)
            Api.getInstance(this.getContext()).cancelRequest(request);
    }

    // TODO: VER DONDE PUEDO RECARGAR VISTAS AL VOLVER DE CONFIG
//    @Override
//    public void onResume() {
//        super.onResume();
//        emptyCard.setVisibility(View.VISIBLE);
//        routinesViewModel.reloadRoutines();
//        getRoutineList();
//    }
}