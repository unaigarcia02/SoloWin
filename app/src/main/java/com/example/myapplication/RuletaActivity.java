package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.Random;

public class RuletaActivity extends BaseActivity {

    private ImageView rouletteImage;
    private Button spinButton;
    private float currentDegree = 0f;
    private final int totalSections = 37;  // 37 secciones para la ruleta (0-36)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        rouletteImage = findViewById(R.id.ruleta);
        spinButton = findViewById(R.id.spinButton);

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
}