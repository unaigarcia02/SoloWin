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
        button1.setOnClickListener(v -> handleButtonPress(button1, 1));
        button2.setOnClickListener(v -> handleButtonPress(button2, 3));
        button3.setOnClickListener(v -> handleButtonPress(button3, 5));
    }

    private void handleButtonPress(Button button, int cost) {
        boolean isButtonAlreadyActive =
                (button == button1 && isStartedButton1) ||
                        (button == button2 && isStartedButton2) ||
                        (button == button3 && isStartedButton3);

        if (isButtonAlreadyActive) {
            // Si el botón ya estaba activo, se detienen las vueltas
            stopVueltas();
            resetButtonLabels();
            enableAllButtons();
            resetButtonStates(); // Reinicia los estados de los botones para permitir reinicio
        } else {
            // Iniciar la animación y deshabilitar otros botones
            disableOtherButtons(button);
            startVueltas();
            button.setText("Stop");
            updateButtonStates(button);
        }
    }

    private void updateButtonStates(Button activeButton) {
        // Activar el estado solo para el botón presionado
        isStartedButton1 = (activeButton == button1);
        isStartedButton2 = (activeButton == button2);
        isStartedButton3 = (activeButton == button3);
    }

    private void resetButtonStates() {
        // Reinicia el estado de todos los botones a false
        isStartedButton1 = false;
        isStartedButton2 = false;
        isStartedButton3 = false;
    }

    private void resetButtonLabels() {
        // Reinicia el texto de los botones
        button1.setText("1€");
        button2.setText("3€");
        button3.setText("5€");
    }

    private void disableOtherButtons(Button activeButton) {
        // Deshabilita los botones que no están activos
        if (activeButton != button1) button1.setEnabled(false);
        if (activeButton != button2) button2.setEnabled(false);
        if (activeButton != button3) button3.setEnabled(false);
    }

    private void enableAllButtons() {
        // Vuelve a habilitar todos los botones
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
    }

    private void startVueltas() {
        // Inicia las vueltas para todas las imágenes
        vuelta1 = new Vueltas(img -> runOnUiThread(() -> img1.setImageResource(img)), 200, randomLong(0, 200));
        vuelta2 = new Vueltas(img -> runOnUiThread(() -> img2.setImageResource(img)), 200, randomLong(0, 200));
        vuelta3 = new Vueltas(img -> runOnUiThread(() -> img3.setImageResource(img)), 200, randomLong(0, 200));
        vuelta1.start();
        vuelta2.start();
        vuelta3.start();
    }

    private void stopVueltas() {
        // Detiene todas las vueltas
        if (vuelta1 != null) vuelta1.stopVueltas();
        if (vuelta2 != null) vuelta2.stopVueltas();
        if (vuelta3 != null) vuelta3.stopVueltas();
    }
}