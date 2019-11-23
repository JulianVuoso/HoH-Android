package ar.edu.itba.hci.hoh.dialogs;


import androidx.appcompat.app.AlertDialog;

abstract class DataDialog {
    protected static DialogData dialogData = new DialogData();

    static void cancelRequests() {
        dialogData.cancelRequests();
    }

    abstract AlertDialog openDialog();
    abstract void closeDialog();
}
