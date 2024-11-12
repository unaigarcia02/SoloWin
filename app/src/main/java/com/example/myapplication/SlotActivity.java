package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;


public class SlotActivity extends BaseActivity {
    private ImageView img1, img2, img3;
    private Vueltas vuelta1, vuelta2, vuelta3;
    private boolean isStartedButton1 = false, isStartedButton2 = false, isStartedButton3 = false;
    private float saldo = BaseActivity.saldo; //utiliza esta variable como saldo, porque se actualiza para todos los juegos
    private Button button1, button2, button3;

    public static final Random RANDOM = new Random();

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_slot);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button3);
        button3 = findViewById(R.id.button5);

        setupButtons();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void setupButtons() {
        button1.setOnClickListener(v -> {
            if (isStartedButton1) {
                // Detener vueltas del botón 1
                stopVueltas();
                button1.setText("1€");
                isStartedButton1 = false;
                checkResult();
            } else {
                // Resetear otros botones
                resetButtons(button2, button3);

                // Iniciar vueltas del botón 1
                startVueltas();
                button1.setText("Stop");
                isStartedButton1 = true;
            }
        });

        button2.setOnClickListener(v -> {
            if (isStartedButton2) {
                // Detener vueltas del botón 2
                stopVueltas();
                button2.setText("3€");
                isStartedButton2 = false;
                checkResult();
            } else {
                // Resetear otros botones
                resetButtons(button1, button3);

                // Iniciar vueltas del botón 2
                startVueltas();
                button2.setText("Stop");
                isStartedButton2 = true;
            }
        });

        button3.setOnClickListener(v -> {
            if (isStartedButton3) {
                // Detener vueltas del botón 3
                stopVueltas();
                button3.setText("5€");
                isStartedButton3 = false;
                checkResult();
            } else {
                // Resetear otros botones
                resetButtons(button1, button2);

                // Iniciar vueltas del botón 3
                startVueltas();
                button3.setText("Stop");
                isStartedButton3 = true;
            }
        });
    }

    private void resetButtons(Button... buttons) {
        // Detiene cualquier otra vuelta en progreso y resetea el texto
        for (Button button : buttons) {
            button.setText(button.getId() == R.id.button1 ? "1€" : button.getId() == R.id.button3 ? "3€" : "5€");
        }
        // Resetea los estados de cada botón
        isStartedButton1 = isStartedButton2 = isStartedButton3 = false;
    }

    private void startVueltas() {
        // Inicia las vueltas de los 3 elementos
        vuelta1 = new Vueltas(img -> runOnUiThread(() -> img1.setImageResource(img)), 200, randomLong(0, 200));
        vuelta2 = new Vueltas(img -> runOnUiThread(() -> img2.setImageResource(img)), 200, randomLong(0, 200));
        vuelta3 = new Vueltas(img -> runOnUiThread(() -> img3.setImageResource(img)), 200, randomLong(0, 200));
        vuelta1.start();
        vuelta2.start();
        vuelta3.start();
    }

    private void stopVueltas() {
        // Detiene las vueltas
        if (vuelta1 != null) vuelta1.stopVueltas();
        if (vuelta2 != null) vuelta2.stopVueltas();
        if (vuelta3 != null) vuelta3.stopVueltas();
    }

    private void checkResult() {
        // Verifica el resultado y muestra el mensaje
        if (vuelta1.currentIndex == vuelta2.currentIndex && vuelta2.currentIndex == vuelta3.currentIndex) {
            Toast.makeText(this, "3 iguales", Toast.LENGTH_SHORT).show();
        } else if (vuelta1.currentIndex == vuelta2.currentIndex || vuelta2.currentIndex == vuelta3.currentIndex || vuelta1.currentIndex == vuelta3.currentIndex) {
            Toast.makeText(this, "2 iguales", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ninguna igual", Toast.LENGTH_SHORT).show();
        }
    }
}