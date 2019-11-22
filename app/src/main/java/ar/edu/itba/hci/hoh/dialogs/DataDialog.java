package ar.edu.itba.hci.hoh.dialogs;

abstract class DataDialog {
    protected static DialogData dialogData = new DialogData();

    static void cancelRequests() {
        dialogData.cancelRequests();
    }

    abstract void openDialog();
    abstract void closeDialog();
}
