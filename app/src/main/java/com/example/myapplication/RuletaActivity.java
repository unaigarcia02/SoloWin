package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RuletaActivity extends BaseActivity {

    private ImageView rouletteImage;
    private float currentDegree = 0f;
    private float dX, dY;
    private float[][] buttonCoordinates = new float[49][2];

    ImageButton[] botones = new ImageButton[49]; // Crear un array para almacenar los botones

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        rouletteImage = findViewById(R.id.ruleta);
        Button spinButton = findViewById(R.id.spinButton);
        ImageView ficha1 = findViewById(R.id.ficha10);
        setupDraggableFicha(ficha1);
        inicializarBotones();

        spinButton.setOnClickListener(v -> spinRoulette());



    }

    private void spinRoulette() {
        // Generar un ángulo aleatorio para que la ruleta se detenga
        Random random = new Random();
        int randomDegree = random.nextInt(360) + 1080;  // +720 para que gire varias veces

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

        // Debug: Imprimir el ángulo y la sección
        Log.d("Ruleta", "Grados Normalizados: " + currentDegree + ", Sección: " + section);
        Log.d("resultado", resultMapping[resultMapping.length-section]);

        return resultMapping[resultMapping.length-section];
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
                    buttonCoordinates[i][1] = location[1]-30; // Coordenada Y
                }
                // Quitar el listener para evitar llamar esto repetidamente
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDraggableFicha(ImageView ficha) {
        ficha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - motionEvent.getRawX();
                        dY = view.getY() - motionEvent.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(motionEvent.getRawX() + dX)
                                .y(motionEvent.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;

                    case MotionEvent.ACTION_UP:
                        // Obtener las dimensiones de la ficha
                        int fichaWidth = view.getWidth();
                        int fichaHeight = view.getHeight();

                        // Verifica qué botones están debajo de la ficha
                        List<Integer> buttonIndices = getButtonsIndicesAtPosition(motionEvent.getRawX(), motionEvent.getRawY(), fichaWidth, fichaHeight);
                        if (!buttonIndices.isEmpty()) {
                            StringBuilder message = new StringBuilder("Ficha colocada sobre los botones: ");
                            for (Integer index : buttonIndices) {
                                message.append(index).append(" ");
                            }
                            Toast.makeText(RuletaActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RuletaActivity.this, "No se colocó la ficha sobre ningún botón.", Toast.LENGTH_SHORT).show();
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
                int buttonX = buttonLocation[0]+27;
                int buttonY = buttonLocation[1]+30;
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

    private boolean isPointInsideButton(float pointX, float pointY, float buttonX, float buttonY, int buttonWidth, int buttonHeight) {
        return (pointX >= buttonX && pointX <= buttonX + buttonWidth) &&
                (pointY >= buttonY && pointY <= buttonY + buttonHeight);
    }



}

