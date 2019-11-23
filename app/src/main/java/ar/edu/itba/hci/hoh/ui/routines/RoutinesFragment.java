package ar.edu.itba.hci.hoh.ui.routines;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.RestartListener;
import ar.edu.itba.hci.hoh.elements.Routine;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.GridLayoutAutofitManager;
import ar.edu.itba.hci.hoh.dialogs.DialogCreator;

public class RoutinesFragment extends Fragment {

    private RoutinesViewModel routinesViewModel;

    private RecyclerView rvRoutines;
    private GridLayoutManager gridLayoutManager;
    private RoutinesAdapter adapter;

    private CardView emptyCard;
    private ProgressBar loading;

    private RestartListener restartListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routinesViewModel = ViewModelProviders.of(this).get(RoutinesViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_routines, container, false);

        rvRoutines = root.findViewById(R.id.rv_routines);
        gridLayoutManager = new GridLayoutAutofitManager(this.getContext(), (int) getResources().getDimension(R.dimen.img_card_width), GridLayoutManager.VERTICAL, false);
        rvRoutines.setLayoutManager(gridLayoutManager);
        adapter = new RoutinesAdapter(routine -> DialogCreator.createDialog(this, routine));
        rvRoutines.setAdapter(adapter);
        getRoutineList();
        emptyCard = root.findViewById(R.id.empty_routines_card);
        TextView tvEmptyRoom = emptyCard.findViewById(R.id.card_no_element_text);
        tvEmptyRoom.setText(R.string.empty_routine_list);
        loading = root.findViewById(R.id.routines_loading_bar);

        restartListener = () -> {
            routinesViewModel.reloadRoutines();
            getRoutineList();
        };
        MainActivity.setRestartListener(restartListener);

        return root;
    }

    private void getRoutineList() {
        routinesViewModel.getRoutines().observe(this, routines -> {
            loading.setVisibility(View.GONE);
            if (routines != null && !routines.isEmpty())
                emptyCard.setVisibility(View.GONE);
            else
                emptyCard.setVisibility(View.VISIBLE);
            if (routines != null)
                routines.sort(Routine.getComparator());
            adapter.setRoutines(routines);
            Log.v(MainActivity.LOG_TAG, "ACTUALICE ROUTINES");
        });
    }

    private void executeRoutine(Routine routine) {
        routinesViewModel.execRoutine(routine.getId()).observe(this, result -> {
            if (result != null)
                MyApplication.makeToast(String.format("%s %s!",getResources().getString(R.string.routine_exec_message), routine.getName()));
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        routinesViewModel.cancelRequests();
        DialogCreator.closeDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.removeRestartListener(restartListener);
    }
}