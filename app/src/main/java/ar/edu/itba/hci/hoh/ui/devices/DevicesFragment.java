package ar.edu.itba.hci.hoh.ui.devices;

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

public class DevicesFragment extends Fragment {

    private DevicesViewModel devicesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel =
                ViewModelProviders.of(this).get(DevicesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        devicesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}