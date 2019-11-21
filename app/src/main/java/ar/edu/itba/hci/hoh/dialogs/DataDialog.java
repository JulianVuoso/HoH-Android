package ar.edu.itba.hci.hoh.dialogs;

abstract class DataDialog {
    protected static DialogData dialogData = new DialogData();

    static void cancelRequests() {
        dialogData.cancelRequests();
    }
}
