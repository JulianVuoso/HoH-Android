package ar.edu.itba.hci.hoh.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.room.RoomFragment;

public class NotificationCreator {
    public static final String CHANNEL_ID = "ar.edu.itba.hci.hoh.NOTIFICATION_CHANNEL";
    private static final String INTENT_KEY = "ROOOM";

    public static void showNotification(Context context, int messageId, Notification notification) {

        /* Create notification manager */
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        /* Register the channel with the system (required by Android 8.0 - Oreo) */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Set channel */
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getResources().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            /* Create channel */
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        /* Send notification */
        assert manager != null;
        manager.notify(messageId, notification);
    }

    public static Notification createNotification(Context context, int title, String content) {
        /* Set notification channel id (required by Android 8.0 - Oreo) */

        Intent intent = new Intent(context.getApplicationContext(), RoomFragment.class);
        intent.putExtra(INTENT_KEY, "hello");

        // Create pending intent, mention the Activity which needs to be
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setContentTitle(context.getResources().getText(title))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return builder.build();
    }
}
