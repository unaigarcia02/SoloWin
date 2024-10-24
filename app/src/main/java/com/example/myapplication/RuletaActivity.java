package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class RuletaActivity extends BaseActivity {

    private ImageView rouletteImage;
    private float currentDegree = 0f;
    private float dX, dY;
    private ImageView kuala;
    private MediaPlayer mediaPlayer;
    private boolean original1, original2, original3, original4, original5;
    private float saldo;
    private Map<String, Integer> apuestasPorBoton = new HashMap<>();  // Mapa para almacenar las apuestas por botón
    private float[][] buttonCoordinates = new float[49][2];
    ImageButton[] botones = new ImageButton[49]; // Crear un array para almacenar los botones
    private Map<ImageView, Integer> fichasEnLaMesa = new HashMap<>(); // Lista para almacenar las fichas en la mesa
    ImageView ficha10;
    ImageView ficha20;
    ImageView ficha50;
    ImageView ficha100;
    ImageView ficha500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        kuala = findViewById(R.id.kuala);
        mediaPlayer = MediaPlayer.create(this, R.raw.boom);

        rouletteImage = findViewById(R.id.ruleta);
        Button spinButton = findViewById(R.id.spinButton);

        ficha10 = findViewById(R.id.ficha10);
        ficha20 = findViewById(R.id.ficha20);
        ficha50 = findViewById(R.id.ficha50);
        ficha100 = findViewById(R.id.ficha100);
        ficha500 = findViewById(R.id.ficha500);


        setupDraggableFicha(ficha10, 10);
        setupDraggableFicha(ficha20, 20);
        setupDraggableFicha(ficha50, 50);
        setupDraggableFicha(ficha100, 100);
        setupDraggableFicha(ficha500, 500);

        original1 = true;
        original2 = true;
        original3 = true;
        original4 = true;
        original5 = true;

        inicializarBotones();
        spinButton.setOnClickListener(v -> spinRoulette());

        TextView saldoTextView = findViewById(R.id.Saldo);
        saldo = Integer.parseInt(saldoTextView.getText().toString());


    }

    private void spinRoulette() {
        // Mostrar el resumen de apuestas antes de iniciar el giro
        mostrarResumenApuestas();

        // Generar un ángulo aleatorio para que la ruleta se detenga
        Random random = new Random();
        int randomDegree =  random.nextInt(360) + 1080;  // +1080 para que gire varias veces

        // Crear una animación de rotación
        RotateAnimation rotateAnimation = new RotateAnimation(
                currentDegree, currentDegree + randomDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        rotateAnimation.setDuration(3000);  // Duración del giro en milisegundos
        rotateAnimation.setFillAfter(true);  // Mantener la posición final después de la animación

        // Listener para cuando la animación se completa
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Opcional: hacer algo al iniciar el giro
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Obtener el resultado cuando la ruleta se detenga
                String result = getRouletteResult(currentDegree);
                Toast.makeText(RuletaActivity.this, "Resultado: " + result, Toast.LENGTH_LONG).show();

                // Realizar las apuestas basadas en el resultado
                realizarApuestas(result);
                eliminarFichasDeLaMesa();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // No se usa en este caso
            }
        });

        // Ejecutar la animación
        rouletteImage.startAnimation(rotateAnimation);

        // Actualizar el ángulo actual
        currentDegree  = ((currentDegree + randomDegree)+5) % 360;
    }
    private void eliminarFichasDeLaMesa() {
        RelativeLayout layout = findViewById(R.id.fichas_container);

        for (ImageView ficha : fichasEnLaMesa.keySet()) {
            if(ficha.equals(ficha10)){
                if(original1){
                    ViewGroup parent = (ViewGroup) ficha10.getParent();
                    parent.removeView(ficha10);
                    original1 = false;
                    layout.removeView(ficha);
                }else{
                    layout.removeView(ficha);
                }
            }
            if(ficha.equals(ficha20)){
                if(original2){
                    ViewGroup parent = (ViewGroup) ficha20.getParent();
                    parent.removeView(ficha20);
                    original2 = false;
                    layout.removeView(ficha);
                }else{
                    layout.removeView(ficha);
                }
            }
            if(ficha.equals(ficha50)){
                if(original3){
                    ViewGroup parent = (ViewGroup) ficha50.getParent();
                    parent.removeView(ficha50);
                    original3 = false;
                    layout.removeView(ficha);
                }else{
                    layout.removeView(ficha);
                }
            }
            if(ficha.equals(ficha100)){
                if(original4){
                    ViewGroup parent = (ViewGroup) ficha100.getParent();
                    parent.removeView(ficha100);
                    original4 = false;
                    layout.removeView(ficha);
                }else{
                    layout.removeView(ficha);
                }
            }
            if(ficha.equals(ficha500)){
                if(original5){
                    ViewGroup parent = (ViewGroup) ficha500.getParent();
                    parent.removeView(ficha500);
                    original5 = false;
                    layout.removeView(ficha);
                }else{
                    layout.removeView(ficha);
                }
            }
            layout.removeView(ficha);  // Eliminar la ficha del layout
        }
        fichasEnLaMesa.clear();  // Limpiar la lista después de eliminar las fichas
        Toast.makeText(RuletaActivity.this, "Todas las fichas eliminadas de la mesa", Toast.LENGTH_SHORT).show();
    }

    private void realizarApuestas(String resultado) {
        int numeroGanador = Integer.parseInt(resultado);
        Map<Integer, List<List<ImageButton>>> fichaApuestas = new HashMap<>(); // HashMap para almacenar el costo y listas de listas de botones
        apuestasPorBoton.clear();

        for (ImageView ficha : fichasEnLaMesa.keySet()) {
            List<ImageButton> buttonsUnderFicha = getButtonsUnderFicha(ficha); // Obtener botones debajo de la ficha
            if (!buttonsUnderFicha.isEmpty()) {
                int costo = costoFicha(ficha); // Obtener el costo de la ficha

                // Si no existe una entrada para este costo, inicializa una nueva lista
                fichaApuestas.putIfAbsent(costo, new ArrayList<>());

                // Añadir la lista de botones actual a la lista correspondiente
                fichaApuestas.get(costo).add(buttonsUnderFicha);

                // Procesar las apuestas para los botones sobre los que está la ficha
                for (ImageButton button : buttonsUnderFicha) {
                    String buttonText = (String) button.getContentDescription(); // Obtener el texto del botón
                    int apuestaActual = apuestasPorBoton.getOrDefault(buttonText, 0);
                    apuestasPorBoton.put(buttonText, apuestaActual + costo); // Asumir que 'costo' es el costo de la ficha
                }
            }
        }
        for (Map.Entry<Integer, List<List<ImageButton>>> entry : fichaApuestas.entrySet()) {
            int costo = entry.getKey();
            List<List<ImageButton>> listasBotones = entry.getValue();

            // Procesar y imprimir cada lista de botones
            for (List<ImageButton> lista : listasBotones) {
                // Imprimir costo en Logcat

                // Verifica la cantidad de botones en la lista
                if (lista.size() == 1) {

                    for (ImageButton button : lista) {
                        Log.d("Apuestas", "ESTA EN 11111111: " + costo);
                        Log.d("Apuestas", "ESTA EN 11111111: " + button.getContentDescription());// Imprimir el contenido de descripción del botón
                        if(button.getContentDescription().equals("2 to 1 (1)")){
                            if (numeroGanador == 3  || numeroGanador == 6 || numeroGanador == 9 || numeroGanador == 12 ||
                                    numeroGanador == 15  || numeroGanador == 18 || numeroGanador == 21 || numeroGanador == 24 ||
                                    numeroGanador == 27  || numeroGanador == 30 || numeroGanador == 33 || numeroGanador == 36)
                            {
                                saldo = saldo + (((2 * costo) + costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("2 to 1 (2)")){
                            if (numeroGanador == 2  || numeroGanador == 5 || numeroGanador == 8 || numeroGanador == 11 ||
                                    numeroGanador == 14  || numeroGanador == 17 || numeroGanador == 20 || numeroGanador == 23 ||
                                    numeroGanador == 26  || numeroGanador == 29 || numeroGanador == 32 || numeroGanador == 35)
                            {
                                saldo = saldo + (((2 * costo) + costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("2 to 1 (3)")){
                            if (numeroGanador == 1  || numeroGanador == 4 || numeroGanador == 7 || numeroGanador == 10 ||
                                    numeroGanador == 13  || numeroGanador == 15 || numeroGanador == 19 || numeroGanador == 22 ||
                                    numeroGanador == 20  || numeroGanador == 28 || numeroGanador == 31 || numeroGanador == 34)
                            {
                                saldo = saldo + (((2 * costo) + costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("1 st 12")){
                            if (numeroGanador == 1  || numeroGanador == 2 || numeroGanador == 3 || numeroGanador == 4 ||
                                    numeroGanador == 5  || numeroGanador == 6 || numeroGanador == 7 || numeroGanador == 8 ||
                                    numeroGanador == 9  || numeroGanador == 10 || numeroGanador == 11 || numeroGanador == 12)
                            {
                                saldo = saldo + (((2 * costo) + costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("2 nd 12")){
                            if (numeroGanador == 13  || numeroGanador == 14 || numeroGanador == 15 || numeroGanador == 16 ||
                                    numeroGanador == 17  || numeroGanador == 18 || numeroGanador == 19 || numeroGanador == 20 ||
                                    numeroGanador == 21  || numeroGanador == 22 || numeroGanador == 23 || numeroGanador == 24)
                            {
                                saldo = saldo + (((2 * costo) + costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("3 rd 12")){
                            if (numeroGanador == 25  || numeroGanador == 26 || numeroGanador == 27 || numeroGanador == 28 ||
                                    numeroGanador == 29  || numeroGanador == 30 || numeroGanador == 31 || numeroGanador == 32 ||
                                    numeroGanador == 33  || numeroGanador == 34 || numeroGanador == 35 || numeroGanador == 36)
                            {
                                saldo = saldo + (((2 * costo) + costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("1 to 18")){
                            if (numeroGanador >= 1 && numeroGanador <= 18)
                            {
                                saldo = saldo + ((2 * costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("19 to 36")){
                            if (numeroGanador >= 19 && numeroGanador <= 36)
                            {
                                saldo = saldo + ((2 * costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("even")){
                            if (numeroGanador % 2 == 0)
                            {
                                saldo = saldo + ((2 * costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("odd")){
                            if (numeroGanador % 2 != 0)
                            {
                                saldo = saldo + ((2 * costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("red")){
                            if (numeroGanador % 2 == 0)
                            {
                                saldo = saldo + ((2 * costo));
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("black")){
                            if (numeroGanador % 2 != 0)
                            {
                                saldo = saldo + ((2 * costo));
                                actualizarSaldo();
                            }
                        }else if(Integer.parseInt(button.getContentDescription().toString()) == numeroGanador){
                            saldo = saldo + (((35 * costo) + costo));
                            actualizarSaldo();
                        }

                    }

                }if (lista.size() == 2) {

                    for (ImageButton button : lista) {
                        Log.d("Apuestas", "ESTA EN 22222222: " + costo);
                        Log.d("Apuestas", "ESTA EN 222222222: " + button.getContentDescription());// Imprimir el contenido de descripción del botón
                        if(button.getContentDescription().equals("2 to 1 (1)")){
                            if (numeroGanador == 3  || numeroGanador == 6 || numeroGanador == 9 || numeroGanador == 12 ||
                                    numeroGanador == 15  || numeroGanador == 18 || numeroGanador == 21 || numeroGanador == 24 ||
                                    numeroGanador == 27  || numeroGanador == 30 || numeroGanador == 33 || numeroGanador == 36)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("2 to 1 (2)")){
                            if (numeroGanador == 2  || numeroGanador == 5 || numeroGanador == 8 || numeroGanador == 11 ||
                                    numeroGanador == 14  || numeroGanador == 17 || numeroGanador == 20 || numeroGanador == 23 ||
                                    numeroGanador == 26  || numeroGanador == 29 || numeroGanador == 32 || numeroGanador == 35)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("2 to 1 (3)")){
                            if (numeroGanador == 1  || numeroGanador == 4 || numeroGanador == 7 || numeroGanador == 10 ||
                                    numeroGanador == 13  || numeroGanador == 15 || numeroGanador == 19 || numeroGanador == 22 ||
                                    numeroGanador == 20  || numeroGanador == 28 || numeroGanador == 31 || numeroGanador == 34)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("1 st 12")){
                            if (numeroGanador == 1  || numeroGanador == 2 || numeroGanador == 3 || numeroGanador == 4 ||
                                    numeroGanador == 5  || numeroGanador == 6 || numeroGanador == 7 || numeroGanador == 8 ||
                                    numeroGanador == 9  || numeroGanador == 10 || numeroGanador == 11 || numeroGanador == 12)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("2 nd 12")){
                            if (numeroGanador == 13  || numeroGanador == 14 || numeroGanador == 15 || numeroGanador == 16 ||
                                    numeroGanador == 17  || numeroGanador == 18 || numeroGanador == 19 || numeroGanador == 20 ||
                                    numeroGanador == 21  || numeroGanador == 22 || numeroGanador == 23 || numeroGanador == 24)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("3 rd 12")){
                            if (numeroGanador == 25  || numeroGanador == 26 || numeroGanador == 27 || numeroGanador == 28 ||
                                    numeroGanador == 29  || numeroGanador == 30 || numeroGanador == 31 || numeroGanador == 32 ||
                                    numeroGanador == 33  || numeroGanador == 34 || numeroGanador == 35 || numeroGanador == 36)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("1 to 18")){
                            if (numeroGanador >= 1 && numeroGanador <= 18)
                            {
                                saldo = saldo + ((float) (2 * costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("19 to 36")){
                            if (numeroGanador >= 19 && numeroGanador <= 36)
                            {
                                saldo = saldo + ((float) (2 * costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("even")){
                            if (numeroGanador % 2 == 0)
                            {
                                saldo = saldo + ((float) (2 * costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("odd")){
                            if (numeroGanador % 2 != 0)
                            {
                                saldo = saldo + ((float) (2 * costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("red")){
                            if (numeroGanador % 2 == 0)
                            {
                                saldo = saldo + ((float) (2 * costo) /2);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("black")){
                            if (numeroGanador % 2 != 0)
                            {
                                saldo = saldo + ((float) (2 * costo) /2);
                                actualizarSaldo();
                            }
                        }else if(Integer.parseInt(button.getContentDescription().toString()) == numeroGanador){
                            saldo = saldo + (((35 * costo) + costo)/2);
                            actualizarSaldo();
                        }

                    }

                } else if (lista.size() == 3) {

                    Log.d("Apuestas", "ESTA EN 33333333: " + costo);
                    kuala.setVisibility(View.VISIBLE);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                    saldo = saldo + costo;
                    actualizarSaldo();
                    // Espera un tiempo antes de desvanecer (fade out)
                    kuala.postDelayed(this::fadeOutImage, 10);
                    Toast.makeText(RuletaActivity.this, "ESA APUESTA ESTA PROHIBIDA", Toast.LENGTH_SHORT).show();

                } else if (lista.size() == 4) {

                    for (ImageButton button : lista) {
                        Log.d("Apuestas", "ESTA EN 22222222: " + costo);
                        Log.d("Apuestas", "ESTA EN 222222222: " + button.getContentDescription());// Imprimir el contenido de descripción del botón
                        if(button.getContentDescription().equals("2 to 1 (1)")){
                            if (numeroGanador == 3  || numeroGanador == 6 || numeroGanador == 9 || numeroGanador == 12 ||
                                    numeroGanador == 15  || numeroGanador == 18 || numeroGanador == 21 || numeroGanador == 24 ||
                                    numeroGanador == 27  || numeroGanador == 30 || numeroGanador == 33 || numeroGanador == 36)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("2 to 1 (2)")){
                            if (numeroGanador == 2  || numeroGanador == 5 || numeroGanador == 8 || numeroGanador == 11 ||
                                    numeroGanador == 14  || numeroGanador == 17 || numeroGanador == 20 || numeroGanador == 23 ||
                                    numeroGanador == 26  || numeroGanador == 29 || numeroGanador == 32 || numeroGanador == 35)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("2 to 1 (3)")){
                            if (numeroGanador == 1  || numeroGanador == 4 || numeroGanador == 7 || numeroGanador == 10 ||
                                    numeroGanador == 13  || numeroGanador == 15 || numeroGanador == 19 || numeroGanador == 22 ||
                                    numeroGanador == 20  || numeroGanador == 28 || numeroGanador == 31 || numeroGanador == 34)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("1 st 12")){
                            if (numeroGanador == 1  || numeroGanador == 2 || numeroGanador == 3 || numeroGanador == 4 ||
                                    numeroGanador == 5  || numeroGanador == 6 || numeroGanador == 7 || numeroGanador == 8 ||
                                    numeroGanador == 9  || numeroGanador == 10 || numeroGanador == 11 || numeroGanador == 12)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("2 nd 12")){
                            if (numeroGanador == 13  || numeroGanador == 14 || numeroGanador == 15 || numeroGanador == 16 ||
                                    numeroGanador == 17  || numeroGanador == 18 || numeroGanador == 19 || numeroGanador == 20 ||
                                    numeroGanador == 21  || numeroGanador == 22 || numeroGanador == 23 || numeroGanador == 24)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("3 rd 12")){
                            if (numeroGanador == 25  || numeroGanador == 26 || numeroGanador == 27 || numeroGanador == 28 ||
                                    numeroGanador == 29  || numeroGanador == 30 || numeroGanador == 31 || numeroGanador == 32 ||
                                    numeroGanador == 33  || numeroGanador == 34 || numeroGanador == 35 || numeroGanador == 36)
                            {
                                saldo = saldo + ((float) ((2 * costo) + costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("1 to 18")){
                            if (numeroGanador >= 1 && numeroGanador <= 18)
                            {
                                saldo = saldo + ((float) (2 * costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("19 to 36")){
                            if (numeroGanador >= 19 && numeroGanador <= 36)
                            {
                                saldo = saldo + ((float) (2 * costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("even")){
                            if (numeroGanador % 2 == 0)
                            {
                                saldo = saldo + ((float) (2 * costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("odd")){
                            if (numeroGanador % 2 != 0)
                            {
                                saldo = saldo + ((float) (2 * costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("red")){
                            if (numeroGanador % 2 == 0)
                            {
                                saldo = saldo + ((float) (2 * costo) /4);
                                actualizarSaldo();
                            }
                        }
                        else if(button.getContentDescription().equals("black")){
                            if (numeroGanador % 2 != 0)
                            {
                                saldo = saldo + ((float) (2 * costo) /4);
                                actualizarSaldo();
                            }
                        }else if(Integer.parseInt(button.getContentDescription().toString()) == numeroGanador){
                            saldo = saldo + (((35 * costo) + costo)/4);
                            actualizarSaldo();
                        }

                    }

                }

            }
        }

    }

    // Método auxiliar para obtener el costo de la ficha
    private int costoFicha(ImageView ficha) {
        // Verifica si la ficha está en el HashMap
        if (fichasEnLaMesa.containsKey(ficha)) {
            // Devuelve el costo asociado a la ficha
            return fichasEnLaMesa.get(ficha);
        } else {
            // Si la ficha no está en el HashMap, devuelve 0 o el valor que prefieras
            return 0;
        }
    }

    private void fadeOutImage() {
        // Animación de desvanecimiento (fade out)
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(1000); // 500 milisegundos
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // No hacer nada
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                kuala.setVisibility(View.GONE); // Oculta la imagen después del desvanecimiento
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // No hacer nada
            }
        });
        kuala.startAnimation(fadeOut);
    }

    private String getRouletteResult(float degree) {
        float degreeNormalized = degree % 360;  // Asegurarse de que el ángulo esté dentro de 0-360

        // Calcular el tamaño de cada sección
        // 37 secciones para la ruleta (0-36)
        int totalSections = 37;
        float sectionAngle = 360f / totalSections;  // Ángulo por sección: 360 / 37 ≈ 9.73°

        // Determinar en qué sección cae la ruleta
        int section = (int) (degreeNormalized / sectionAngle);


        // Mapeo de cada sección a un número de la ruleta (0-36)
        String[] resultMapping = {
                "0", "32", "15", "19", "4", "21", "2", "25", "17", "34",
                "6", "27", "13", "36", "11", "30", "8", "23", "10", "5",
                "24", "16", "33", "1", "20", "14", "31", "9", "22", "18",
                "29", "7", "28", "12", "35", "3", "26"
        };

        // Asegurarse de que el índice de sección esté en el rango correcto
        if (section < 0 || section >= resultMapping.length) {
            return "Resultado no válido";  // Manejo de error si la sección está fuera de rango
        }
        if(section == 0){
            section = 37;
        }

        // Debug: Imprimir el ángulo y la sección
        Log.d("Ruleta", "Grados Normalizados: " + degreeNormalized + ", Sección: " + section);
        Log.d("resultado", resultMapping[resultMapping.length - section]);

        return resultMapping[resultMapping.length - section]; // Retorna el resultado basado en el índice ajustado
    }


    //SECCION PARA GESTIOAR LOS BOTONES DE LA RULETA

    private void inicializarBotones() {
        // Iterar y asignar botones al array

        for (int i = 0; i <= 48; i++) {
            int resID = getResources().getIdentifier("num_" + i, "id", getPackageName());
            botones[i] = findViewById(resID);
        }



        // Asegúrate de que las coordenadas se obtengan después del layout
        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i <= 48; i++) {
                    // Guardar las coordenadas del botón
                    int[] location = new int[2];
                    botones[i].getLocationOnScreen(location);
                    buttonCoordinates[i][0] = location[0]; // Coordenada X
                    buttonCoordinates[i][1] = location[1]; // Coordenada Y
                }
                // Quitar el listener para evitar llamar esto repetidamente
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDraggableFicha(ImageView ficha, int costo) {
        // Inicializamos el "tag" de la ficha para saber si ya ha sido soltada
        ficha.setTag(false);  // `false` indica que la ficha es nueva y no ha sido soltada

        ficha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Solo inicializamos el arrastre de la ficha aquí, sin modificar el saldo
                        List<Integer> buttonIndices = getButtonsIndicesAtPosition(motionEvent.getRawX(), motionEvent.getRawY(), view.getWidth(), view.getHeight());
                        if (!buttonIndices.isEmpty()) {
                            // Solo arrastrar la ficha existente
                            dX = view.getX()+30 - motionEvent.getRawX();
                            dY = view.getY()+30 - motionEvent.getRawY();
                        } else {
                            // Crear una nueva ficha sin descontar saldo
                            ImageView newFicha = createNewFicha(view, costo);
                            setupDraggableFicha(newFicha, costo); // Asociar el costo de la nueva ficha
                            dX = newFicha.getX()+30 - motionEvent.getRawX();
                            dY = newFicha.getY()+30 - motionEvent.getRawY();
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // Permitir que la ficha se arrastre sin costo
                        view.animate()
                                .x(motionEvent.getRawX()+30 + dX)
                                .y(motionEvent.getRawY()+30 + dY)
                                .setDuration(0)
                                .start();
                        break;

                    case MotionEvent.ACTION_UP:
                        // Obtener las dimensiones de la ficha
                        int fichaWidth = view.getWidth();
                        int fichaHeight = view.getHeight();

                        // Obtener los textos de los botones en los que se ha soltado la ficha
                        List<String> buttonTextsOnRelease = getButtonsTextsAtPosition(motionEvent.getRawX(), motionEvent.getRawY(), fichaWidth, fichaHeight);

                        if (!buttonTextsOnRelease.isEmpty()) {
                            // Verificar si el saldo es suficiente antes de descontar
                            if (saldo >= costo) {
                                // Comprobar si esta es la primera vez que se suelta la ficha
                                if (!(boolean) view.getTag()) {
                                    // Si es la primera vez, descontamos el saldo
                                    saldo -= costo;
                                    actualizarSaldo();  // Actualiza el saldo en la interfaz de usuario
                                    view.setTag(true);
                                    // Marcamos la ficha como ya soltada
                                    fichasEnLaMesa.put((ImageView) view, costo);

                                    // Actualizar el mapa de apuestas por botón
                                    for (String buttonText : buttonTextsOnRelease) {
                                        int apuestaActual = apuestasPorBoton.getOrDefault(buttonText, 0);
                                        apuestasPorBoton.put(buttonText, apuestaActual + costo);  // Sumar el costo de la ficha a las apuestas actuales
                                    }

                                    StringBuilder message = new StringBuilder("Ficha colocada sobre los botones: ");
                                    for (String buttonText : buttonTextsOnRelease) {
                                        message.append(buttonText).append(" ");
                                    }
                                    Toast.makeText(RuletaActivity.this, message.toString(), Toast.LENGTH_SHORT).show();

                                } else {
                                    // Solo actualizar el mapa si la ficha ya ha sido soltada antes
                                    for (String buttonText : buttonTextsOnRelease) {
                                        int apuestaActual = apuestasPorBoton.getOrDefault(buttonText, 0);
                                        apuestasPorBoton.put(buttonText, apuestaActual + costo);
                                    }

                                    StringBuilder message2 = new StringBuilder("Ficha colocada sobre los botones: ");
                                    for (String buttonText : buttonTextsOnRelease) {
                                        message2.append(buttonText).append(" ");
                                    }
                                    Toast.makeText(RuletaActivity.this, message2.toString(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Mostrar mensaje si no hay saldo suficiente
                                Toast.makeText(RuletaActivity.this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
                                view.setVisibility(View.GONE);  // Eliminar la ficha si no hay saldo
                            }
                        } else {
                            // Si la ficha no se suelta sobre un botón válido, la eliminamos
                            view.setVisibility(View.GONE);
                            Toast.makeText(RuletaActivity.this, "Ficha no colocada sobre ningún botón. Ficha desaparece.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
    }


    private List<Integer> getButtonsIndicesAtPosition(float x, float y, int fichaWidth, int fichaHeight) {
        List<Integer> buttonIndices = new ArrayList<>(); // Lista para almacenar los índices de los botones

        for (int i = 0; i <= 48; i++) {
            // Obtener el botón correspondiente
            View button = findViewById(getResources().getIdentifier("num_" + i, "id", getPackageName()));
            if (button != null) {
                // Obtener las coordenadas y tamaño del botón
                int[] buttonLocation = new int[2];
                button.getLocationOnScreen(buttonLocation);
                int buttonX = buttonLocation[0];
                int buttonY = buttonLocation[1];
                int buttonWidth = button.getWidth();
                int buttonHeight = button.getHeight();

                // Comprobar si la ficha intersecta con el botón
                if (isRectanglesIntersecting(buttonX, buttonY, buttonWidth, buttonHeight, x, y, fichaWidth, fichaHeight)) {
                    buttonIndices.add(i); // Agregar el índice del botón a la lista
                }
            }
        }


        // Para depuración: Imprimir los índices de los botones detectados
        Log.d("RuletaActivity", "Botones detectados: " + buttonIndices.toString());

        return buttonIndices; // Retorna la lista de botones detectados
    }

    private boolean isRectanglesIntersecting(int buttonX, int buttonY, int buttonWidth, int buttonHeight, float fichaX, float fichaY, int fichaWidth, int fichaHeight) {
        // Comprobar si hay intersección entre el botón y la ficha
        return !(buttonX > fichaX + fichaWidth ||
                buttonX + buttonWidth < fichaX ||
                buttonY > fichaY + fichaHeight ||
                buttonY + buttonHeight < fichaY);
    }



    private ImageView createNewFicha(View originalFicha, int costo) {
        RelativeLayout layout = findViewById(R.id.fichas_container);
        // Crear una nueva ficha
        ImageView newFicha = new ImageView(RuletaActivity.this);
        // Obtener el recurso de imagen de la ficha original
        newFicha.setImageDrawable(((ImageView) originalFicha).getDrawable());
        // Mantener el tamaño original de la ficha
        newFicha.setTag(costo);


        int originalWidth = originalFicha.getWidth();
        int originalHeight = originalFicha.getHeight();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(originalWidth, originalHeight);
        newFicha.setLayoutParams(params);

        // Obtener la posición de la ficha original en la pantalla
        int[] locationOriginal = new int[2];
        originalFicha.getLocationOnScreen(locationOriginal);

        int[] locationLayout = new int[2];
        layout.getLocationOnScreen(locationLayout);

        int relativeX = locationOriginal[0] - locationLayout[0];
        int relativeY = locationOriginal[1] - locationLayout[1];

        // Posicionar la nueva ficha en la misma posición que la ficha original
        newFicha.setX(relativeX);
        newFicha.setY(relativeY);

        // Añadir la nueva ficha al contenedor
        layout.addView(newFicha);

        return newFicha;
    }

    private void actualizarSaldo() {
        TextView saldoTextView = findViewById(R.id.Saldo);
        saldoTextView.setText(String.valueOf(saldo));
    }

    private List<String> getButtonsTextsAtPosition(float x, float y, int fichaWidth, int fichaHeight) {
        List<String> buttonTexts = new ArrayList<>(); // Lista para almacenar los textos de los botones

        for (int i = 0; i <= 48; i++) {
            // Obtener el botón correspondiente
            ImageButton button = findViewById(getResources().getIdentifier("num_" + i, "id", getPackageName()));
            if (button != null) {
                // Obtener las coordenadas y tamaño del botón
                int[] buttonLocation = new int[2];
                button.getLocationOnScreen(buttonLocation);
                int buttonX = buttonLocation[0];
                int buttonY = buttonLocation[1];
                int buttonWidth = button.getWidth();
                int buttonHeight = button.getHeight();

                // Comprobar si la ficha intersecta con el botón
                if (isRectanglesIntersecting(buttonX, buttonY, buttonWidth, buttonHeight, x, y, fichaWidth, fichaHeight)) {
                    // Obtener el texto del botón si es un ImageButton con texto asignado
                    String buttonText = (String) button.getContentDescription(); // Usar el atributo "contentDescription" o un método similar
                    if (buttonText != null) {
                        buttonTexts.add(buttonText); // Agregar el texto del botón a la lista
                    }
                }
            }
        }

        // Para depuración: Imprimir los textos de los botones detectados
        Log.d("RuletaActivity", "Botones detectados: " + buttonTexts.toString());

        return buttonTexts; // Retorna la lista de textos de los botones detectados
    }

    private void mostrarResumenApuestas() {
        StringBuilder resumen = new StringBuilder("");

        for (Map.Entry<String, Integer> entry : apuestasPorBoton.entrySet()) {
            String boton = entry.getKey();
            int totalApostado = entry.getValue();
            resumen.append("").append(boton).append(": ").append(totalApostado).append(" de");
        }

        // Mostrar el resumen en la consola o en un Toast
        Log.d("RuletaActivity", resumen.toString());
        Toast.makeText(RuletaActivity.this, resumen.toString(), Toast.LENGTH_LONG).show();
    }

    private List<ImageButton> getButtonsUnderFicha(ImageView ficha) {
        List<ImageButton> buttonsUnderFicha = new ArrayList<>(); // Lista para almacenar los botones sobre los que está la ficha

        // Obtener las coordenadas de la ficha
        int[] fichaLocation = new int[2];
        ficha.getLocationOnScreen(fichaLocation);
        float fichaX = fichaLocation[0];
        float fichaY = fichaLocation[1];
        int fichaWidth = ficha.getWidth();
        int fichaHeight = ficha.getHeight();

        // Iterar sobre todos los botones
        for (int i = 0; i <= 48; i++) {
            // Obtener el botón correspondiente
            ImageButton button = botones[i];
            if (button != null) {
                // Obtener las coordenadas y tamaño del botón
                int[] buttonLocation = new int[2];
                button.getLocationOnScreen(buttonLocation);
                float buttonX = buttonLocation[0];
                float buttonY = buttonLocation[1];
                float buttonWidth = button.getWidth();
                float buttonHeight = button.getHeight();

                // Comprobar si la ficha intersecta con el botón
                if (isRectanglesIntersecting((int) buttonX, (int) buttonY, (int) buttonWidth, (int) buttonHeight, fichaX, fichaY, fichaWidth, fichaHeight)) {
                    buttonsUnderFicha.add(button); // Agregar el botón a la lista
                }
            }
        }


        return buttonsUnderFicha; // Retorna la lista de botones detectados
    }

    protected void onDestroy() {
        super.onDestroy();
        // Libera el MediaPlayer cuando ya no se necesite
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}

