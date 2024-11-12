package com.example.myapplication;

public class Vueltas extends Thread{

    interface VueltasListener {
        void newImage(int img);
    }

    private static int[] imgs = {R.drawable.sym1,R.drawable.sym2, R.drawable.sym3, R.drawable.sym4};
    int currentIndex;
    private VueltasListener vueltasListener;
    private long frameDuration;
    private long startIn;
    private boolean isStarted;

    public Vueltas(VueltasListener vueltasListener, long frameDuration, long startIn){
        this.vueltasListener = vueltasListener;
        this.frameDuration = frameDuration;
        this.startIn = startIn;
        currentIndex = 0;
        isStarted = true;
    }

    public void nextImg(){
        currentIndex++;

        if (currentIndex == imgs.length){
            currentIndex = 0;
        }
    }

    @Override
    public void run() {
        try{
            Thread.sleep(startIn);
        } catch (InterruptedException e) {
        }

        while (isStarted) {
            try {
                Thread.sleep(frameDuration);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            nextImg();

            if (vueltasListener != null) {
                vueltasListener.newImage(imgs[currentIndex]);
            }
        }
    }

    public void stopVueltas(){
        isStarted = false;
    }
}
