package com.example.myapplication;

import static com.example.myapplication.R.raw.temones;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class BuscaMinasActivity extends BaseActivity {

    private float saldo = BaseActivity.saldo; //utiliza esta variable como saldo, porque se actualiza para todos los juegos
    private MediaPlayer mediaPlayer;
    Button[][] botones = new Button[5][5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_busca_minas);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.temones);
        int duracion = mediaPlayer.getDuration();
        Random random = new Random();
        int inicioAleatorio = random.nextInt(duracion);
        mediaPlayer.seekTo(inicioAleatorio);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar memoria
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}