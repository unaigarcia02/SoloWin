package com.example.myapplication;

import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;


public class SlotActivity extends BaseActivity {
    private ImageView img1, img2, img3;
    private Vueltas vuelta1, vuelta2, vuelta3;
    private boolean isStarted = false; // Controla si alguna vuelta está activa
    private Button button1, button2, button3;
    private ImageButton button4;
    private Map<Integer, String> MapaSym;
    private TextView msg;
    private float saldo = BaseActivity.saldo; // Ejemplo de saldo inicial
    private MediaPlayer musica, musica2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot);
        msg = findViewById(R.id.Saldo);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button3);
        button3 = findViewById(R.id.button5);
        button4 = findViewById(R.id.imageButton);
        msg.setText(String.valueOf(saldo));
        musica = MediaPlayer.create(this, R.raw.temones);
        musica2 = MediaPlayer.create(this, R.raw.vueltas);
        musica.setLooping(true);
        musica2.setLooping(true);
        musica.start();

        // Inicializar MapaSym correctamente en onCreate
        MapaSym = new HashMap<>();
        MapaSym.put(R.drawable.sym1, "7");
        MapaSym.put(R.drawable.sym2, "bar");
        MapaSym.put(R.drawable.sym3, "cereza");
        MapaSym.put(R.drawable.sym4, "campana");

        setupButtons();

        BaseActivity.pararmusica();//deja esto, es para que se pare la musica del menu cuando se entra aqui
    }

    private void setupButtons() {
        button1.setOnClickListener(v -> {
            try {
                handleButtonPress(button1, 1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        button2.setOnClickListener(v -> {
            try {
                handleButtonPress(button2, 3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        button3.setOnClickListener(v -> {
            try {
                handleButtonPress(button3, 5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        button4.setOnClickListener(v -> {
            mostrarInstrucciones();
        });
    }

    private void handleButtonPress(Button button, int mult) throws InterruptedException {
        if (isStarted) {
            // Si las vueltas están activas, se detienen
            stopVueltas();
            sleep(500);
            musica2.pause();
            // Obtener los símbolos resultantes
            int symbol1 = vuelta1.getCurrentImage();
            int symbol2 = vuelta2.getCurrentImage();
            int symbol3 = vuelta3.getCurrentImage();

            // Calcular el premio con los símbolos obtenidos
            calcularPremio(symbol1, symbol2, symbol3, mult);

            resetButtonLabels();
            enableAllButtons();
            isStarted = false; // Reiniciar el estado de vueltas
        } else {
            // Iniciar las vueltas y deshabilitar otros botones
            if ((button==button1 && saldo>1) || (button==button2 && saldo>3) || (button==button3 && saldo>5)) {
            disableOtherButtons(button);
            startVueltas();
            button.setText("Stop");
            musica2.start();
            // Descontar el saldo según el botón pulsado
            if (button==button1){saldo --;  BaseActivity.saldo=saldo;}
            else if (button==button2) {saldo=saldo-3;   BaseActivity.saldo=saldo;}
            else if (button==button3) {saldo=saldo-5;   BaseActivity.saldo=saldo;}
            msg.setText(String.valueOf(saldo));
            isStarted = true;}
            else{
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void calcularPremio(int symbol1, int symbol2, int symbol3, int mult) {
        String nombreSimbolo = MapaSym.get(symbol1);
        String nombreSimbolo2 = MapaSym.get(symbol2);
        String nombreSimbolo3 = MapaSym.get(symbol3);

        if (nombreSimbolo==nombreSimbolo2 && nombreSimbolo2==nombreSimbolo3){
            if (nombreSimbolo=="cereza"){
                Toast.makeText(this, "Has ganado:" + mult*2 + "€", Toast.LENGTH_SHORT).show();
                saldo=saldo+mult*2;
                BaseActivity.saldo=saldo;
                msg.setText(String.valueOf(saldo));
            }
            if (nombreSimbolo=="campana"){
                Toast.makeText(this, "Has ganado:" + mult*4 + "€", Toast.LENGTH_SHORT).show();
                saldo=saldo+mult*4;
                BaseActivity.saldo=saldo;
                msg.setText(String.valueOf(saldo));
            }
            if (nombreSimbolo=="bar"){
                Toast.makeText(this, "Has ganado:" + mult*8 + "€", Toast.LENGTH_SHORT).show();
                saldo=saldo+mult*8;
                BaseActivity.saldo=saldo;
                msg.setText(String.valueOf(saldo));
            }
            if (nombreSimbolo=="7") {
                Toast.makeText(this, "Has ganado:" + mult*16 + "€", Toast.LENGTH_SHORT).show();
                saldo = saldo+mult*16;
                BaseActivity.saldo=saldo;
                msg.setText(String.valueOf(saldo));
            }
        }
        else{
            Toast.makeText(this, "No has ganado, INTENTALO OTRA VEZ", Toast.LENGTH_SHORT).show();
        }
    }

    private void startVueltas() {
        vuelta1 = new Vueltas(img -> runOnUiThread(() -> img1.setImageResource(img)), 200);
        vuelta2 = new Vueltas(img -> runOnUiThread(() -> img2.setImageResource(img)), 200);
        vuelta3 = new Vueltas(img -> runOnUiThread(() -> img3.setImageResource(img)), 200);
        vuelta1.start();
        vuelta2.start();
        vuelta3.start();
    }

    private void stopVueltas() {
        if (vuelta1 != null) vuelta1.stopVueltas();
        if (vuelta2 != null) vuelta2.stopVueltas();
        if (vuelta3 != null) vuelta3.stopVueltas();
    }

    private void resetButtonLabels() {
        button1.setText("1€");
        button2.setText("3€");
        button3.setText("5€");
    }

    private void disableOtherButtons(Button activeButton) {
        if (activeButton != button1) button1.setEnabled(false);
        if (activeButton != button2) button2.setEnabled(false);
        if (activeButton != button3) button3.setEnabled(false);
    }

    private void enableAllButtons() {
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (musica != null) {
            musica.release(); // Libera el MediaPlayer cuando se destruye la Activity
            musica = null;
        }
        if (musica2 != null) {
            musica2.release(); // Libera el MediaPlayer cuando se destruye la Activity
            musica2 = null;
        }
    }
    private void mostrarInstrucciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instrucciones de la Slot Machine");
        builder.setMessage("Para jugar debes tener un saldo mayor del que quieres apostar. Para apostar haces click en cualquiera de los 3 botones. La slot comenzará a funcionar y habrá que darle de nuevo al botón de la apuesta para que pare de girar. Buena suerte :)");
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
