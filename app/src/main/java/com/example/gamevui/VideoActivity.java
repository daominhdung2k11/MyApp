package com.example.gamevui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);

        // Bắt đầu service phát video dưới nền
        startVideoService();

        // Phát video chính
        playVideo();
    }

    private void playVideo() {
        try {
            // Lấy video từ raw folder
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.skibidiyesyes);
            videoView.setVideoURI(videoUri);
            
            // Tắt media controller để không thể dừng video
            videoView.setMediaController(null);

            videoView.setOnPreparedListener(mp -> {
                mediaPlayer = mp;
                mediaPlayer.setLooping(true); // Lặp lại video
                videoView.start();
            });

            videoView.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(VideoActivity.this, "Lỗi phát video", Toast.LENGTH_SHORT).show();
                return true;
            });

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startVideoService() {
        Intent serviceIntent = new Intent(this, VideoService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    @Override
    public void onBackPressed() {
        // Không cho thoát app bằng nút back
        Toast.makeText(this, "Không thể thoát!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Video vẫn tiếp tục chạy dưới nền
    }

    @Override
    protected void onDestroy() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
        super.onDestroy();
    }
}
