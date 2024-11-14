package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends BaseActivity {
    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        video = findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.casin;
        video.setVideoURI(Uri.parse(path));
        video.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.setVolume(0f, 0f); // Silencia el audio
            video.start(); // Comienza a reproducir el video
        });


        video.setOnCompletionListener(mediaPlayer -> video.start()); // Loop del video

        Button buttonJugar = findViewById(R.id.buttonJgr);
        buttonJugar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (video != null) {
            video.start(); // Reanudar el video cuando la actividad se vuelve visible
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (video != null) {
            video.pause(); // Pausar el video cuando la actividad está en segundo plano
        }
    }
}
