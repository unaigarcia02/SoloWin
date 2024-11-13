package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuActivity extends BaseActivity  {
    private ImageView fondo1, fondo2;
    private TextView msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        msg = findViewById(R.id.Saldo);
        fondo1 = findViewById(R.id.imageView);
        fondo2 = findViewById(R.id.imageView7);

        ImageButton ruletaBtn = findViewById(R.id.RuletaBTN);
        ImageButton buscaminasBtn = findViewById(R.id.BuscaminasBTN);
        ImageButton slotBtn = findViewById(R.id.SlotBTN);
        ImageButton mrblackjackBtn = findViewById(R.id.mrblackBTN);

        // Configurar el clic en el botón
        ruletaBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, RuletaActivity.class);
            startActivity(intent);
        });

        buscaminasBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, BuscaMinasActivity.class);
            startActivity(intent);
        });

        slotBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, SlotActivity.class);
            startActivity(intent);
        });
        mrblackjackBtn.setOnClickListener(View -> {
            Intent hallo = new Intent(MenuActivity.this, BlackJackActivity.class);
            startActivity(hallo);
        });



        // Usar ViewTreeObserver para asegurarse de que las vistas estén listas
        fondo1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fondo1.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Iniciar la animación para fondo1
                startAnimation(fondo1);
                // Iniciar la animación para fondo2
                startAnimation(fondo2);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED; // Evita propagar los insets si ya los usaste
        });
    }

    private void startAnimation(ImageView fondo) {
        TranslateAnimation animacion = new TranslateAnimation(
                0,                  // X inicial
                0,                  // X final
                0,                  // Y inicial
                -fondo.getHeight()  // Y final (altura de la imagen)
        );
        animacion.setDuration(4000);     // Duración de la animación en milisegundos
        animacion.setRepeatCount(Animation.INFINITE);  // Repetir infinitamente
        animacion.setRepeatMode(Animation.RESTART);    // Reinicia la animación
        // Ejecuta la animación
        animacion.setInterpolator(new LinearInterpolator());
        fondo.startAnimation(animacion);
    }
    protected void onResume() {
        super.onResume();
        msg.setText(String.valueOf(BaseActivity.saldo));
    }
}
