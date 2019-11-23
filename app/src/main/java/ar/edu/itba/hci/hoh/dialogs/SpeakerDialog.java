package ar.edu.itba.hci.hoh.dialogs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.DeviceState;

class SpeakerDialog extends DeviceDialog {
    private AlertDialog dialog;
    private TextView time, song, artist, mState;
    private ImageView playPause;
    private SeekBar volume;
    private TextView volumeTxt;

    private static final String[] genres = {"pop", "rock", "dance", "country", "classical"};

    SpeakerDialog(Fragment fragment, Device device) {
        super(fragment, device);
    }

    AlertDialog openDialog() {
        LayoutInflater inflater = fragment.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_device_speaker, null);
        this.dialog = new AlertDialog.Builder(fragment.getContext()).setView(dialogView).create();
//        this.dialog.setOnDismissListener(dialog -> DialogCreator.closeDialog());
        setDialogHeader(dialogView);

        time = dialogView.findViewById(R.id.time);
        song = dialogView.findViewById(R.id.song);
        artist = dialogView.findViewById(R.id.artist);
        mState = dialogView.findViewById(R.id.music_state);
        playPause = dialogView.findViewById(R.id.pause_play);

        setPlayPause();

        ImageView stop = dialogView.findViewById(R.id.stop);
        stop.setOnClickListener(v -> {
            device.getState().setStatus("stopped");
            setPlayPause();
            execAction("stop");
        });
        ImageView prev = dialogView.findViewById(R.id.prev);
        prev.setOnClickListener(v -> execAction("previousSong"));
        ImageView next = dialogView.findViewById(R.id.next);
        next.setOnClickListener(v -> execAction("nextSong"));

        playPause.setOnClickListener(v -> {
            String status = device.getState().getStatus();
            if (status.equals("playing")) {
                device.getState().setStatus("paused");
                execAction("pause");
            } else {
                device.getState().setStatus("playing");
                if (status.equals("stopped"))
                    execAction("play");
                else
                    execAction("resume");
            }
            setPlayPause();
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(fragment.getContext(), android.R.layout.simple_spinner_item, genres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner genre = dialogView.findViewById(R.id.genre_selector);
        genre.setAdapter(adapter);
        genre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                execAction("setGenre", getParams(genres[position]));
                Log.e(MainActivity.LOG_TAG, String.format("Selected item: %s", genres[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        setTextViews();

        volume = dialogView.findViewById(R.id.speaker_volume_bar);
        volumeTxt = dialogView.findViewById(R.id.speaker_volume_text);
        volume.setMax(10);
        setVolume();
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeTxt.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                execAction("setVolume", getParams(seekBar.getProgress()));
            }
        });

        this.dialog.show();

        return this.dialog;
    }

    private void setPlayPause() {
        String state = device.getState().getStatus();
        if (state.equals("playing")) {
            playPause.setImageResource(R.drawable.ic_pause_circle_filled_black_80dp);
            mState.setText(fragment.getResources().getString(R.string.device_status_playing));
        } else {
            playPause.setImageResource(R.drawable.ic_play_circle_filled_black_80dp);
            if (state.equals("stopped"))
                mState.setText(fragment.getResources().getString(R.string.device_status_stopped));
            else
                mState.setText(fragment.getResources().getString(R.string.device_status_paused));
        }
    }

    private void setTextViews() {
        String status = device.getState().getStatus();
        if (status.equals("playing") || status.equals("paused")) {
            DeviceState.Song currentSong = device.getState().getSong();
            Log.e(MainActivity.LOG_TAG, String.format("%s %s", currentSong.getProgress(), status));
            time.setText(currentSong.getProgress());
            song.setText(currentSong.getTitle());
            artist.setText(currentSong.getArtist());
        } else {
            time.setText("-");
            song.setText("-");
            artist.setText("-");
        }
    }

    private void setVolume() {
        int currentVol = device.getState().getVolume();
        volumeTxt.setText(String.valueOf(currentVol));
        volume.setProgress(currentVol);
    }

    void closeDialog() {
        super.cancelTimer();
        dialog.dismiss();
    }

    void reloadData() {
        Log.e(MainActivity.LOG_TAG, "actualizando");
//        setPlayPause();
        setTextViews();
//        setVolume();
    }
}
