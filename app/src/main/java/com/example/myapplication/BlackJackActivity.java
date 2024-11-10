package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityBlackJackBinding;

import java.util.Random;

public class BlackJackActivity extends AppCompatActivity {
    private float saldo = BaseActivity.saldo; //utiliza esta variable como saldo, porque se actualiza para todos los juegos
    private ActivityBlackJackBinding binding;
    private int numeroactual = 0;

    private int dealerpunt= 0;
    private int jugadorpunt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBlackJackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.saldoText.setText(String.valueOf(saldo));
        setupButtons();
    }

    private void setupButtons() {
        binding.pedirButton.setOnClickListener(v -> {
            Toast.makeText(this, "QUE", Toast.LENGTH_SHORT).show();
        });

        binding.plantarseButton.setOnClickListener(v -> {
            Toast.makeText(this, "QUE", Toast.LENGTH_SHORT).show();
        });

        binding.doblarButton.setOnClickListener(v -> {
            Toast.makeText(this, "QUE", Toast.LENGTH_SHORT).show();
        });

        binding.repartirButton.setOnClickListener(v -> {
            repartir();
        });
    }
    public void repartir(){
        binding.pedirButton.setEnabled(true);
        binding.plantarseButton.setEnabled(true);
        binding.doblarButton.setEnabled(true);
        String carta = sacarCarta();
        Toast.makeText(this, carta, Toast.LENGTH_SHORT).show();
        //Esto de aqui abajo usa el nombre y lo convierte en ID para cambiar la imagen, inteligente si me preguntas
        binding.imageView11.setImageResource(getResources().getIdentifier(carta, "drawable", getPackageName()));
        binding.imageView11.setVisibility(View.VISIBLE);
        dealerpunt = actualizarPuntuacion(numeroactual,dealerpunt);
        binding.dealerScore.setText(String.valueOf("Puntuación: "+dealerpunt));

        String carta2 = sacarCarta();
        binding.imageView17.setImageResource(getResources().getIdentifier(carta2, "drawable", getPackageName()));
        binding.imageView17.setVisibility(View.VISIBLE);
        jugadorpunt = actualizarPuntuacion(numeroactual,jugadorpunt);
        binding.playerScore.setText("Puntuación: "+String.valueOf(jugadorpunt));





    }
    public int actualizarPuntuacion(int numero, int puntuacion) {
        int puntos = 0;

        switch (numero) {
            case 1:
                puntos = 11;
                break;
            case 11:
            case 12:
            case 13:
                puntos = 10;
                break;
            default:
                puntos = numero;
                break;
        }

        puntuacion += puntos;

        Toast.makeText(this, "Puntuación actual: " + puntuacion, Toast.LENGTH_SHORT).show();
        return puntos;
    }
    public String sacarCarta(){
        Random random = new Random();
        int palo = random.nextInt(3)+1;
        String palon = "";
        switch (palo){
            case 0:
                palon = "spades";
                break;
            case 1:
                palon = "hearts";
                break;
            case 2:
                palon = "diamonds";
                break;
            case 3:
                palon = "clubs";
                break;
        }
        numeroactual = random.nextInt(13)+1;
        String carta = "";
        switch (numeroactual) {
            case 1:
                carta = "ace";
                break;
            case 11:
                carta = "jack";
                break;
            case 12:
                carta = "queen";
                break;
            case 13:
                carta = "king";
                break;
            default:
                carta = String.format("%02d", numeroactual);
                break;
        }

        return palon+"_"+carta;
    }

    }



