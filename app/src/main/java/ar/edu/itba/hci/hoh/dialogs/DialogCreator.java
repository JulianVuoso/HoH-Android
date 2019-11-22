package ar.edu.itba.hci.hoh.dialogs;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Routine;

public abstract class DialogCreator {
    private static DataDialog dialog;

    public static void createDialog(Fragment fragment, @NonNull Device device) {
        if (fragment.getContext() == null || device.getType() == null) return;

        switch (device.getType().getName()) {
            case "door":
                dialog = new DoorDialog(fragment, device);
                break;
            case "oven":
                dialog = new OvenDialog(fragment, device);
        }
        if (dialog != null)
            dialog.openDialog();
    }

    public static void createDialog(Fragment fragment, Routine routine) {
        if (fragment.getContext() == null) return;
        dialog = new ExecRoutineDialog(fragment, routine);
        dialog.openDialog();
    }

    public static void closeDialog() {
        DataDialog.cancelRequests();
        if (dialog != null) {
            dialog.closeDialog();
            dialog = null;
        }
    }
}
