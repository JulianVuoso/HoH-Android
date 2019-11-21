package ar.edu.itba.hci.hoh.notifications;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    public final static String TAG = "ar.edu.itba.workmanager.myworker";
    public final static String NAME = TAG;

    private int test;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        test = 0;
    }

    @NonNull
    @Override
    public Result doWork() {
        test++;
        Log.i("Worker Tester", String.format("Te value of test is %d", test));
        return Result.success();
    }
}