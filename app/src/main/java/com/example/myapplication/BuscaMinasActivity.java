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
    private float apuesta;
    private MediaPlayer mediaPlayer;
    ImageButton[][] casillas = new ImageButton[5][5];
    private TextView sal;
    private ImageButton infobtn;
    private Button botonComenzar;
    private EditText apuestaInput;
    private Button botFacil,botMedia,botDificil;
    private String dificultadSelec="";
    private int totalDiamantes;
    private int diamantesEncontrados;
    private boolean[][] tieneBomba;
    private boolean[][] tieneDiamante;


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

        // Inicialización de casillas una vez en onCreate()
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                String buttonID = "casilla" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                casillas[i][j] = findViewById(resID);
            }
        }

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
        //configurar para solo seleccionar una
        View.OnClickListener dificultadListener=v -> {
            resetearDificultades();

            if (v.getId()==R.id.botFacil) {
                dificultadSelec = "Facil";
                botFacil.setSelected(true);
            }else if(v.getId()==R.id.botMedia){
                dificultadSelec="Media";
                botMedia.setSelected(true);
            }else if(v.getId()==R.id.botDificil) {
                dificultadSelec = "Dificil";
                botDificil.setSelected(true);
            }
        };
        botFacil.setOnClickListener(dificultadListener);
        botMedia.setOnClickListener(dificultadListener);
        botDificil.setOnClickListener(dificultadListener);

        //boton comenzar
        botonComenzar=findViewById(R.id.botonComenzar);
        botonComenzar.setOnClickListener(v->iniciarJuego());
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

    private void resetearDificultades(){
        botFacil.setSelected(false);
        botMedia.setSelected(false);
        botDificil.setSelected(false);
    }

    private void iniciarJuego(){
        String apuestaStr=apuestaInput.getText().toString();
        apuesta=Float.parseFloat(apuestaStr);

        //no se introduce nada en el EditText
        if (apuestaStr.isEmpty()){
            mostrarAlerta("Error","Introduce una cantidad para apostar.");
            return;
        }

        //verificar que la apuesta no es mayor al saldo
        else if (apuesta>saldo){
            mostrarAlerta("Saldo insuficiente","No tiene saldo suficiente para realizar esta apuesta");
            return;
        }

        //verificar que se ha seleccionado una dificultad
        else if(dificultadSelec.isEmpty()){
            mostrarAlerta("Error","Por favor, selecciona una dificultad");
            return;
        }

        //iniciar el juego
        else{
            saldo=saldo-apuesta;
            sal.setText(String.valueOf(saldo));
            jugar();
        }
    }

    private void jugar() {
        // Inicializar variables
        diamantesEncontrados = 0;
        tieneBomba = new boolean[5][5];
        tieneDiamante = new boolean[5][5];

        // Configurar la cantidad de bombas y diamantes según la dificultad
        int numBombas = 0;
        if (dificultadSelec.equals("Facil")) {
            numBombas = 2;
            totalDiamantes = 5;
        } else if (dificultadSelec.equals("Media")) {
            numBombas = 4;
            totalDiamantes = 10;
        } else if (dificultadSelec.equals("Dificil")) {
            numBombas = 8;
            totalDiamantes = 15;
        }

        // Colocar bombas aleatoriamente
        Random random = new Random();
        for (int i = 0; i < numBombas; i++) {
            int fila, columna;
            do {
                fila = random.nextInt(5);
                columna = random.nextInt(5);
            } while (tieneBomba[fila][columna]); // Evitar colocar dos bombas en la misma celda
            tieneBomba[fila][columna] = true;
        }
        // Colocar diamantes aleatoriamente
        for (int i = 0; i < totalDiamantes; i++) {
            int fila, columna;
            do {
                fila = random.nextInt(5);
                columna = random.nextInt(5);
            } while (tieneBomba[fila][columna] || tieneDiamante[fila][columna]); // Evitar superposiciones
            tieneDiamante[fila][columna] = true;
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                casillas[i][j].setEnabled(true);
                casillas[i][j].setAlpha(1.0f); // Restablecer transparencia
                casillas[i][j].setImageResource(R.drawable.azulejo2);
            }
        }

        // Asignar comportamiento a cada casilla
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int finalI = i;
                int finalJ = j;
                casillas[i][j] = findViewById(getResources().getIdentifier("casilla" + i + j, "id", getPackageName()));
                casillas[i][j].setOnClickListener(v -> revelarCasilla(finalI, finalJ));
            }
        }
    }

    private void revelarCasilla(int fila, int columna) {
        if (tieneBomba[fila][columna]) {
            casillas[fila][columna].setBackground(null);
            casillas[fila][columna].setImageResource(R.drawable.bomba); // Asignar imagen de bomba
            mostrarAlerta("Has perdido", "Has encontrado una bomba. Mejor suerte la próxima vez.");
            finalizarJuego(false);
        } else if (tieneDiamante[fila][columna]) {
            casillas[fila][columna].setBackground(null);
            casillas[fila][columna].setImageResource(R.drawable.diamante); // Asignar imagen de diamante
            diamantesEncontrados++;
            if (diamantesEncontrados == totalDiamantes) {
                mostrarAlerta("¡Felicidades!", "¡Has encontrado todos los diamantes y has ganado el juego!");
                finalizarJuego(true);
            }
        } else {
            casillas[fila][columna].setAlpha(0f);//hacer la casilla transparente
        }
    }

    private void finalizarJuego(boolean victoria) {
        // Deshabilitar todas las casillas después de ganar o perder
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                casillas[i][j].setEnabled(false);
            }
        }
        // Recompensar al jugador si gana
        if (victoria) {
            float multiplicador = 1.0f;
            // Configurar multiplicador según la dificultad seleccionada
            switch (dificultadSelec) {
                case "Facil":
                    multiplicador = 2.0f;
                    break;
                case "Media":
                    multiplicador = 3.0f;
                    break;
                case "Dificil":
                    multiplicador = 5.0f;
                    break;
            }
            saldo += apuesta * multiplicador;
            sal.setText(String.valueOf(saldo));
        }

        new android.os.Handler().postDelayed(this::resetearTablero, 2000);
    }

    private void resetearTablero(){
        // Restablecer el tablero y permitir nueva apuesta y selección de dificultad
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                casillas[i][j].setEnabled(false);
                casillas[i][j].setAlpha(1.0f);
                casillas[i][j].setImageResource(R.drawable.azulejo2); // Hacer la casilla visible de nuevo
            }
        }
        // Restablecer valores del juego
        dificultadSelec = "";
        apuesta = 0;
        apuestaInput.setText(""); // Limpiar el campo de apuesta
        resetearDificultades(); // Quitar la selección de dificultad
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
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