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

        //no se introduce nada en el EditText
        if (apuestaStr.isEmpty()){
            mostrarAlerta("Error","Introduce una cantidad para apostar.");
            return;
        }

        float apuesta=Float.parseFloat(apuestaStr);

        //verificar que la apuesta no es mayor al saldo
        if (apuesta>saldo){
            mostrarAlerta("Saldo insuficiente","No tiene saldo suficiente para realizar esta apuesta");
            return;
        }

        //verificar que se ha seleccionado una dificultad
        if(dificultadSelec.isEmpty()){
            mostrarAlerta("Error","Por favor, selecciona una dificultad");
            return;
        }

        //iniciar el juego
        //saldo=saldo-apuesta;
        //sal.setText(String.valueOf(saldo));
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