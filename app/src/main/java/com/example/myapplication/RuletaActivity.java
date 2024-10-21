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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RuletaActivity extends BaseActivity {

    private ImageView rouletteImage;
    private float currentDegree = 0f;
    private float dX, dY;
    private boolean isOriginalFicha;
    private float saldo;
    private float[][] buttonCoordinates = new float[49][2];
    ImageButton[] botones = new ImageButton[49]; // Crear un array para almacenar los botones
    private Map<ImageView, Integer> fichaCostMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);


        rouletteImage = findViewById(R.id.ruleta);
        Button spinButton = findViewById(R.id.spinButton);

        ImageView ficha10 = findViewById(R.id.ficha10);
        ImageView ficha20 = findViewById(R.id.ficha20);
        ImageView ficha50 = findViewById(R.id.ficha50);
        ImageView ficha100 = findViewById(R.id.ficha100);
        ImageView ficha500 = findViewById(R.id.ficha500);


        setupDraggableFicha(ficha10, 10);
        setupDraggableFicha(ficha20, 20);
        setupDraggableFicha(ficha50, 50);
        setupDraggableFicha(ficha100, 100);
        setupDraggableFicha(ficha500, 500);
        inicializarBotones();
        spinButton.setOnClickListener(v -> spinRoulette());

        TextView saldoTextView = findViewById(R.id.Saldo);
        saldo = Integer.parseInt(saldoTextView.getText().toString());


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
                        // Obtener los botones en los que se ha soltado la ficha
                        List<Integer> buttonIndicesOnRelease = getButtonsIndicesAtPosition(motionEvent.getRawX(), motionEvent.getRawY(), fichaWidth, fichaHeight);

                        if (!buttonIndicesOnRelease.isEmpty()) {
                            // Verificar si el saldo es suficiente antes de descontar
                            if (saldo >= costo) {
                                // Comprobar si esta es la primera vez que se suelta la ficha
                                if (!(boolean) view.getTag()) {
                                    // Si es la primera vez, descontamos el saldo
                                    saldo -= costo;
                                    actualizarSaldo();  // Actualiza el saldo en la interfaz de usuario
                                    view.setTag(true);  // Marcamos la ficha como ya soltada
                                    StringBuilder message = new StringBuilder("Ficha colocada sobre los botones: ");
                                    for (Integer index : buttonIndicesOnRelease) {
                                        message.append(index).append(" ");
                                    }
                                    Toast.makeText(RuletaActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    StringBuilder message2 = new StringBuilder("Ficha colocada sobre los botones: ");
                                    for (Integer index : buttonIndicesOnRelease) {
                                        message2.append(index).append(" ");
                                    }
                                    Toast.makeText(RuletaActivity.this, message2.toString(), Toast.LENGTH_SHORT).show();                                                                    }
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
}

