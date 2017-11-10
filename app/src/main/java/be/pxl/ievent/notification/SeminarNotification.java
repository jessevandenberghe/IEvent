package be.pxl.ievent.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import be.pxl.ievent.App;
import be.pxl.ievent.R;
import be.pxl.ievent.services.AttendService;


/**
 * Helper class for showing and canceling geo fencing
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class SeminarNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "geoFencing";
    private static final String TAG = SeminarNotification.class.getSimpleName();

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of geo fencing notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static void notify(final Context context,
                              final String exampleString) {
        final Resources res = context.getResources();

        final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);


        final String title = "IEvent";
        final String text = exampleString;

        Intent iAttend = new Intent(App.getContext(), AttendService.class);
        PendingIntent piAttend = PendingIntent.getService(App.getContext(),0,iAttend,PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setVibrate(null)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .setContentTitle(title)
                .setContentText(text)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_MAX)

                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                //.setNumber(number)



                // Example additional actions for this notification. These will
                // only show on devices running Android 4.1 or later, so you
                // should ensure that the activity in this notification's
                // content intent provides access to the same actions in
                // another way.


                .addAction(R.drawable.empty_icon,"Bevestigen",piAttend)

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(false);



        Notification n = builder.build();

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        notify(context, n);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
        Log.i(TAG, "notify: Notification created");
    }

    /**
     * Cancels any notifications of this type previously shown using
     * .
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }

        Log.i(TAG, "notify: Notification canceled");
    }
}
