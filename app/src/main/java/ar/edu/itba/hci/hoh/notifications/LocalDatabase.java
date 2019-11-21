package ar.edu.itba.hci.hoh.notifications;

import android.content.Context;

import androidx.room.*;

@Database(entities = {Tuple.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    private final static String NAME = "devices_db";
    private static LocalDatabase instance;

    /* If instance does not exist, creates it */
    public static synchronized LocalDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, NAME)
                    .build();
        return instance;
    }

    public abstract TupleDao tupleDao();
}