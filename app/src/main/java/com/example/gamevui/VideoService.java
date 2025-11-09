package com.example.gamevui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class VideoService extends Service {

    private MediaPlayer mediaPlayer;
    private static final String CHANNEL_ID = "VideoChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Tạo notification channel
        createNotificationChannel();

        // Phát video dưới nền
        playBackgroundVideo();

        // Tạo notification để giữ service chạy
        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification);

        return START_STICKY; // Service sẽ được khởi động lại nếu bị kill
    }

    private void playBackgroundVideo() {
        try {
            mediaPlayer = new MediaPlayer();
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.skibidiyesyes);
            mediaPlayer.setDataSource(this, videoUri);
            mediaPlayer.setLooping(true); // Lặp lại video
            
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
            });
            
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Video Playback",
                    NotificationManager.IMPORTANCE_LOW);
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, VideoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Game Vui")
                .setContentText("Đang chạy...")
                .setSmallIcon(R.drawable.avatar)
                .setContentIntent(pendingIntent)
                .setOngoing(true) // Không thể swipe để xóa
                .build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
