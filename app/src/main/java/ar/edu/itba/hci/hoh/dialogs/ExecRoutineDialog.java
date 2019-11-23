package ar.edu.itba.hci.hoh.dialogs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.elements.Routine;

class ExecRoutineDialog extends DataDialog {
    private Fragment fragment;
    private Routine routine;
    private AlertDialog dialog;

    ExecRoutineDialog(Fragment fragment, Routine routine) {
        this.fragment = fragment;
        this.routine = routine;
    }

    AlertDialog openDialog() {
        LayoutInflater inflater = fragment.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_exec_routine, null);
        this.dialog = new AlertDialog.Builder(fragment.getContext()).setView(dialogView).create();

        TextView dialogMessage = dialogView.findViewById(R.id.dialog_exec_routine_message);
        dialogMessage.setText(String.format("%s %s?", dialogView.getResources().getString(R.string.routine_exec_dialog_message), routine.getName()));
        Button cancelButton = dialogView.findViewById(R.id.dialog_exec_routine_cancel);
        if (cancelButton != null) cancelButton.setOnClickListener(v -> dialog.dismiss());
        Button runButton = dialogView.findViewById(R.id.dialog_exec_routine_run);
        if (runButton != null) runButton.setOnClickListener(v -> {
            executeRoutine();
            dialog.dismiss();
        });

        this.dialog.show();
        return this.dialog;
    }

    void closeDialog() {
        dialog.dismiss();
    }

    private void executeRoutine() {
        dialogData.execRoutine(routine.getId()).observe(fragment, result -> {
            if (result != null)
                MyApplication.makeToast(String.format("%s %s!", fragment.getResources().getString(R.string.routine_exec_message), routine.getName()));
        });
    }
}