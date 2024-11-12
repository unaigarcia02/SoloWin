package com.example.myapplication;

import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
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
    private Map<Integer, String> MapaSym;
    private TextView msg;
    private float saldo = BaseActivity.saldo; // Ejemplo de saldo inicial

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

        // Inicializar MapaSym correctamente en onCreate
        MapaSym = new HashMap<>();
        MapaSym.put(R.drawable.sym1, "7");
        MapaSym.put(R.drawable.sym2, "bar");
        MapaSym.put(R.drawable.sym3, "cereza");
        MapaSym.put(R.drawable.sym4, "campana");

        setupButtons();
    }

    private void setupButtons() {
        button1.setOnClickListener(v -> handleButtonPress(button1, 1));
        button2.setOnClickListener(v -> handleButtonPress(button2, 3));
        button3.setOnClickListener(v -> handleButtonPress(button3, 5));
    }

    private void handleButtonPress(Button button, int mult) {
        if (isStarted) {
            // Si las vueltas están activas, se detienen
            stopVueltas();

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
            disableOtherButtons(button);
            startVueltas();
            button.setText("Stop");
            // Descontar el saldo según el botón pulsado
            if (button==button1){saldo --;}
            else if (button==button2) {saldo=saldo-3;}
            else if (button==button3) {saldo=saldo-5;}
            msg.setText(String.valueOf(saldo));
            isStarted = true; // Indicar que las vueltas están activas
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
            }
            if (nombreSimbolo=="campana"){
                Toast.makeText(this, "Has ganado:" + mult*4 + "€", Toast.LENGTH_SHORT).show();
                saldo=saldo+mult*4;
            }
            if (nombreSimbolo=="bar"){
                Toast.makeText(this, "Has ganado:" + mult*8 + "€", Toast.LENGTH_SHORT).show();
                saldo=saldo+mult*8;
            }
            if (nombreSimbolo=="7") {
                Toast.makeText(this, "Has ganado:" + mult*16 + "€", Toast.LENGTH_SHORT).show();
                saldo = saldo+mult*16;
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
}