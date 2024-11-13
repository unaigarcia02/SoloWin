package com.example.myapplication;

import static com.example.myapplication.R.raw.temones;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class BuscaMinasActivity extends BaseActivity {

    private float saldo = BaseActivity.saldo; //utiliza esta variable como saldo, porque se actualiza para todos los juegos
    private MediaPlayer mediaPlayer;
    ImageButton[][] casillas = new ImageButton[5][5];
    private TextView sal;
    private ImageButton infobtn;
    private Button botonComenzar;
    private EditText apuestaInput;
    private Button botFacil,botMedia,botDificil;
    private String dificultadSelec="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_busca_minas);

        sal=findViewById(R.id.saldo);
        sal.setText(String.valueOf(saldo));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupButtons();
        setupMediaPlayer();


    }

    private void setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.temones);
        int duracion = mediaPlayer.getDuration();
        Random random = new Random();
        int inicioAleatorio = random.nextInt(duracion);
        mediaPlayer.seekTo(inicioAleatorio);
        mediaPlayer.start();
    }

    private void setupButtons() {
        //boton informacion
        infobtn = findViewById(R.id.infobtn);
        infobtn.setOnClickListener(v -> mostrarInstrucciones());

        //campo de apuesta
        apuestaInput=findViewById(R.id.betInput);

        //botones de dificultad
        botFacil=findViewById(R.id.botFacil);
        botMedia=findViewById(R.id.botMedia);
        botDificil=findViewById(R.id.botDificil);

    }

    //informacion boton informacion
    private void mostrarInstrucciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instrucciones del Buscaminas");
        builder.setMessage("El objetivo del juego es encontrar todos los diamantes evitando destapar cualquier bomba. Para comenzar, introduce la cantidad que deseas apostar, elige el nivel de dificultad y presiona Comenzar. Buena suerte :)");
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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