package com.gp.health.utils.notification_service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gp.health.R;
import com.gp.health.data.prefs.AppPreferencesHelper;
import com.gp.health.ui.chat.ChatActivity;
import com.gp.health.ui.main.MainActivity;
import com.gp.health.ui.property_details.PropertyDetailsActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final String CHANNEL_1_ID = "channel1";
    private static final String CHANNEL_2_ID = "channel2";
    private int itemId = -1;
    private int senderId = -1;
    private int receiverId = -1;

    @SuppressLint("LogNotTimber")
    @Override
    public void onNewToken(@NonNull String refreshedToken) {
        super.onNewToken(refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        //token.sendRefreshedToken(refreshedToken);
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                JSONObject object = new JSONObject(remoteMessage.getData());
                String title = object.getString("title");
                String body = object.getString("body");
                int type = Integer.parseInt(object.getString("type"));
                if (object.has("item"))
                    itemId = Integer.parseInt(object.getString("item"));
                if (object.has("user_reciever"))
                    receiverId = Integer.parseInt(object.getString("user_reciever"));
                if (object.has("user_sender"))
                    senderId = Integer.parseInt(object.getString("user_sender"));

                if (AppPreferencesHelper.getInstance().isUserLogged()) { //handle till token works
                    if (senderId != AppPreferencesHelper.getInstance().getCurrentUserId())
                        sendNotification(title, body, itemId, type, receiverId, senderId);
                }


//                sendNotification(title, body, postId, type, receiverId, senderId);
//                sendNotification(title, body, orderId, type);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String title, String body, int itemId, int type, int receiverId, int senderId) {
        int mId = (int) System.currentTimeMillis();
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) setupChannels();

        if (type == 5) {
            intent = new Intent(this, ChatActivity.class)
                    .putExtra("receiver_id", senderId);
        } else if (type == 7) {
            intent = new Intent(this, PropertyDetailsActivity.class)
                    .putExtra("itemId", itemId);
        } else
            intent = new Intent(this, MainActivity.class);

        intent.putExtra("itemId", itemId);

        intent.setAction("actionstring" + System.currentTimeMillis());
        intent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, mId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = null;
        if (AppPreferencesHelper.getInstance().getNotificationsSound() == 1)
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(mId, notification.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
        channel1.setDescription("This is channel 1");

        NotificationChannel channel2 = new NotificationChannel(
                CHANNEL_2_ID, "Channel 2", NotificationManager.IMPORTANCE_LOW);
        channel2.setDescription("This is channel 2");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel1);
        notificationManager.createNotificationChannel(channel2);
    }

}
