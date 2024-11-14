package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Build;
import android.view.WindowManager;
import android.view.DisplayCutout;

public class BaseActivity extends AppCompatActivity {


    protected static float saldo = 200f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Solicitar el uso completo de la pantalla, pero respetar el área del notch/cutout
            getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

            // Verificar los insets y ajustar el diseño
            findViewById(android.R.id.content).setOnApplyWindowInsetsListener((view, insets) -> {
                DisplayCutout cutout = insets.getDisplayCutout();
                if (cutout != null) {
                    // Ajustar el layout evitando el área del notch
                    view.setPadding(cutout.getSafeInsetLeft(), cutout.getSafeInsetTop(),
                            cutout.getSafeInsetRight(), cutout.getSafeInsetBottom());
                }
                return insets.consumeSystemWindowInsets();
            });
        }
        hideSystemUI();  // Llamar al método para ocultar las barras del sistema
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }
}

