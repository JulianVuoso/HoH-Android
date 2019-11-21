package ar.edu.itba.hci.hoh.dialogs;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import ar.edu.itba.hci.hoh.elements.Routine;

public abstract class DialogCreator {
    private static AlertDialog dialog;

    public static void createDialog(Fragment fragment, Routine routine) {
        dialog = ExecRoutineDialog.openDialog(fragment, routine);
    }

    public static void closeDialog() {
        DataDialog.cancelRequests();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
