package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityBlackJackBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackJackActivity extends AppCompatActivity {
    private float saldo = BaseActivity.saldo; //utiliza esta variable como saldo, porque se actualiza para todos los juegos
    private ActivityBlackJackBinding binding;
    private int numeroactual = 0;

    private int dealerpunt= 0;
    private int jugadorpunt=0;
    private double apuesta = 0;

    private ImageView[] pcs;
    private ImageView[] dcs;

    private List<String> usadas = new ArrayList<>();
    //al cambiar de foco vuelve a tener pantalla completa
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBlackJackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.saldoText.setText(String.valueOf(saldo));
        setupButtons();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );
          pcs = new ImageView[] {
                binding.pc1,
                binding.pc2,
                binding.pc3,
                binding.pc4,
                binding.pc5,
                binding.pc6
        };
        dcs = new ImageView[]  {
                binding.dc1,
                binding.dc2,
                binding.dc3,
                binding.dc4,
                binding.dc5,
                //binding.dc6
        };
    }

    private void setupButtons() {
        binding.pedirButton.setOnClickListener(v -> {
            pedir();
        });

        binding.plantarseButton.setOnClickListener(v -> {
            //plantarse();
        });

        binding.doblarButton.setOnClickListener(v -> {
            Toast.makeText(this, "QUE", Toast.LENGTH_SHORT).show();
        });

        binding.repartirButton.setOnClickListener(v -> {
            try {
                apuesta = Double.parseDouble(binding.betInput.getText().toString());
                if (apuesta <= saldo) {
                    saldo = (float) (saldo - apuesta);
                    binding.saldoText.setText(String.valueOf(saldo));
                    binding.apostado.setText("Apostado: "+apuesta);
                    repartir();
                } else {
                    Toast.makeText(this, "No tienes suficiente saldo", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor, introduce una cantidad válida", Toast.LENGTH_SHORT).show();
            }
        });
        binding.imageButton.setOnClickListener(v -> {
            mostrarInstrucciones();
        });

    }
    private void mostrarInstrucciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instrucciones de Blackjack");
        builder.setMessage("El objetivo del Blackjack es acercarse lo más posible a 21 puntos sin pasarse. Cada carta tiene un valor numérico y puedes pedir más cartas o plantarte en cualquier momento. Ganas si tu puntuación es mayor que la del dealer sin pasarte de 21. Buena suerte :)");
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void repartir(){
        binding.pedirButton.setEnabled(true);
        binding.plantarseButton.setEnabled(true);
        binding.doblarButton.setEnabled(true);
        String carta = sacarCarta();
        Toast.makeText(this, carta, Toast.LENGTH_SHORT).show();
        //Esto de aqui abajo usa el nombre y lo convierte en ID para cambiar la imagen, inteligente si me preguntas
        binding.dc1.setImageResource(getResources().getIdentifier(carta, "drawable", getPackageName()));
        binding.dc1.setVisibility(View.VISIBLE);
        dealerpunt = actualizarPuntuacion(numeroactual,dealerpunt);
        binding.dealerScore.setText(String.valueOf("Puntuación conocida: "+dealerpunt));
        binding.dc2.setVisibility(View.VISIBLE);

        String carta2 = sacarCarta();
        binding.pc1.setImageResource(getResources().getIdentifier(carta2, "drawable", getPackageName()));
        binding.pc1.setVisibility(View.VISIBLE);
        jugadorpunt = actualizarPuntuacion(numeroactual,jugadorpunt);
        String carta3 = sacarCarta();
        binding.pc2.setImageResource(getResources().getIdentifier(carta3, "drawable", getPackageName()));
        binding.pc2.setVisibility(View.VISIBLE);
        jugadorpunt = actualizarPuntuacion(numeroactual,jugadorpunt);
        binding.playerScore.setText("Puntuación: "+String.valueOf(jugadorpunt));


    }
    private int actualizarPuntuacion(int numero, int puntuacion) {
        int puntos = 0;

        switch (numero) {
            case 1:
                puntos = 1;
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
        if(jugadorpunt > 21 || dealerpunt > 21)
        {
            Toast.makeText(this, "Has perdido", Toast.LENGTH_SHORT).show();
            jugadorpunt = 0;
            dealerpunt =0;
            binding.playerScore.setText("Puntuación: "+String.valueOf(jugadorpunt));
            binding.dealerScore.setText("Puntuación: "+String.valueOf(dealerpunt));


            for (ImageView pc : pcs) {
                pc.setVisibility(View.INVISIBLE);
            }
            for (ImageView dc : dcs) {
                dc.setVisibility(View.INVISIBLE);
            }
            binding.pedirButton.setEnabled(false);
            binding.plantarseButton.setEnabled(false);
            binding.doblarButton.setEnabled(false);

            //Toast.makeText(this, String.valueOf(jugadorpunt), Toast.LENGTH_SHORT).show();
            return 0;

        }

        return puntuacion;
    }

    private String sacarCarta(){
        Random random = new Random();
        int palo = random.nextInt(4);
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

        if(usadas.contains(palon+"_"+carta)){
            Toast.makeText(this,"Se ha eliminado"+palon+"_"+carta, Toast.LENGTH_SHORT).show();
            return sacarCarta();
        }
        usadas.add(palon+"_"+carta);
        return palon+"_"+carta;
    }

    private void pedir(){
        for (ImageView pc : pcs) {
            if(pc.getVisibility() != View.VISIBLE) {
                String carta = sacarCarta();
                pc.setImageResource(getResources().getIdentifier(carta, "drawable", getPackageName()));
                pc.setVisibility(View.VISIBLE);
                jugadorpunt = actualizarPuntuacion(numeroactual,jugadorpunt);
                binding.playerScore.setText("Puntuación: "+String.valueOf(jugadorpunt));
                break;
            }
        }
    }
    }




