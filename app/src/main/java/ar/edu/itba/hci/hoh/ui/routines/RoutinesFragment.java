package ar.edu.itba.hci.hoh.ui.routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ar.edu.itba.hci.hoh.R;

public class RoutinesFragment extends Fragment {

    private RoutinesViewModel routinesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routinesViewModel =
                ViewModelProviders.of(this).get(RoutinesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rooms, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        routinesViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}