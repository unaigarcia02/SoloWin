package com.example.myapplication;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Vueltas extends Thread {
    interface VueltasListener {
        void newImage(int img);
    }

    private static int[] imgs = {R.drawable.sym1, R.drawable.sym2, R.drawable.sym3, R.drawable.sym4};
    private static final List<Integer> probs = Arrays.asList(
            R.drawable.sym3, R.drawable.sym3, R.drawable.sym3, R.drawable.sym3, R.drawable.sym3, R.drawable.sym3, R.drawable.sym3, R.drawable.sym3, // Probabilidad alta para cerezas
            R.drawable.sym4, R.drawable.sym4, R.drawable.sym4, R.drawable.sym4, R.drawable.sym4, R.drawable.sym4, // Probabilidad media para campanas
            R.drawable.sym2, R.drawable.sym2,                  // Probabilidad baja para bar
            R.drawable.sym1                                    // Probabilidad muy baja para 7
    );

    private VueltasListener vueltasListener;
    private long frameDuration;
    private boolean isStarted;
    private int currentImage; // Imagen actual seleccionada
    private static final Random RANDOM = new Random();

    public Vueltas(VueltasListener vueltasListener, long frameDuration) {
        this.vueltasListener = vueltasListener;
        this.frameDuration = frameDuration;
        this.isStarted = true;
    }

    public int getCurrentImage() {
        return currentImage;
    }

    @Override
    public void run() {
        while (isStarted) {
            try {
                // Añadimos aleatoriedad al frameDuration
                long randomFrameDuration = frameDuration + RANDOM.nextInt(100) - 50; // valor entre frameDuration - 50 y frameDuration + 50
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Selección aleatoria de símbolo usando la lista de probabilidad
            int randomIndex = RANDOM.nextInt(probs.size());
            currentImage = probs.get(randomIndex); // Almacenar el símbolo actual

            if (vueltasListener != null) {
                vueltasListener.newImage(currentImage);
            }
        }
    }

    public void stopVueltas() {
        isStarted = false;
    }
}
