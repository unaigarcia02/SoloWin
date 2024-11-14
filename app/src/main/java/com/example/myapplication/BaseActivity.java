package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends AppCompatActivity {
    protected static float saldo = 200f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        musica();
        hideSystemUI();
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    public void musica(){
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.musicaperrona);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public static void ponermusica(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.musicaperrona);
            mediaPlayer.setLooping(true);
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }


    public static void pararmusica() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release(); // Libera recursos y pone a null
            mediaPlayer = null; // Para poder crear uno nuevo al volver a llamar a ponermusica
        }
    }
}
