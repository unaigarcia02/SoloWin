package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.myapplication.databinding.ActivitySlotBinding;

import java.util.Random;


public class SlotActivity extends BaseActivity {
    private TextView msg;
    private ImageView img1,img2,img3;
    private Vueltas vuelta1, vuelta2, vuelta3;
    private Button btn;
    private boolean isStarted;
    private float saldo = BaseActivity.saldo; //utiliza esta variable como saldo, porque se actualiza para todos los juegos
    private ActivitySlotBinding binding;

    public static final Random RANDOM = new Random();

    public static long randomLong(long lower, long upper){
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_slot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img1);
        img3 = (ImageView) findViewById(R.id.img1);
        msg = (TextView) findViewById(R.id.Saldo);
        setupButtons();
    }

    private void setupButtons() {
        binding.button1.setOnClickListener(v -> {
            if(isStarted){
                vuelta1.stopVueltas();
                vuelta2.stopVueltas();
                vuelta3.stopVueltas();

                if(vuelta1.currentIndex == vuelta2.currentIndex && vuelta2.currentIndex == vuelta3.currentIndex){
                    msg.setText("You win the big prize");
                } else if (vuelta1.currentIndex == vuelta2.currentIndex || vuelta2.currentIndex == vuelta3.currentIndex || vuelta1.currentIndex == vuelta3.currentIndex) {
                    msg.setText("Little Prize");
                } else{
                    msg.setText("You lose");
                }

                btn.setText("Start");
                isStarted=false;
            } else {
                vuelta1=new Vueltas(new Vueltas.VueltasListener() {
                    @Override
                    public void newImage(final int img) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img1.setImageResource(img);
                            }
                        });
                    }
                }, 200, randomLong(0,200));
                vuelta1.start();
                vuelta2=new Vueltas(new Vueltas.VueltasListener() {
                    @Override
                    public void newImage(final int img) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img2.setImageResource(img);
                            }
                        });
                    }
                }, 200, randomLong(0,200));
                vuelta2.start();
                vuelta3=new Vueltas(new Vueltas.VueltasListener() {
                    @Override
                    public void newImage(final int img) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img3.setImageResource(img);
                            }
                        });
                    }
                }, 200, randomLong(0,200));
                vuelta3.start();
            }
            btn.setText("Stop");
            msg.setText("");
            isStarted=true;
        });

        binding.button3.setOnClickListener(v -> {
            if(isStarted){
                vuelta1.stopVueltas();
                vuelta2.stopVueltas();
                vuelta3.stopVueltas();

                if(vuelta1.currentIndex == vuelta2.currentIndex && vuelta2.currentIndex == vuelta3.currentIndex){
                    msg.setText("You win the big prize");
                } else if (vuelta1.currentIndex == vuelta2.currentIndex || vuelta2.currentIndex == vuelta3.currentIndex || vuelta1.currentIndex == vuelta3.currentIndex) {
                    msg.setText("Little Prize");
                } else{
                    msg.setText("You lose");
                }

                btn.setText("Start");
                isStarted=false;
            } else {
                vuelta1=new Vueltas(new Vueltas.VueltasListener() {
                    @Override
                    public void newImage(final int img) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img1.setImageResource(img);
                            }
                        });
                    }
                }, 200, randomLong(0,200));
                vuelta1.start();
                vuelta2=new Vueltas(new Vueltas.VueltasListener() {
                    @Override
                    public void newImage(final int img) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img2.setImageResource(img);
                            }
                        });
                    }
                }, 200, randomLong(0,200));
                vuelta2.start();
                vuelta3=new Vueltas(new Vueltas.VueltasListener() {
                    @Override
                    public void newImage(final int img) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img3.setImageResource(img);
                            }
                        });
                    }
                }, 200, randomLong(0,200));
                vuelta3.start();
            }
            btn.setText("Stop");
            msg.setText("");
            isStarted=true;
        });

        binding.button5.setOnClickListener(v -> {
            if(isStarted){
                vuelta1.stopVueltas();
                vuelta2.stopVueltas();
                vuelta3.stopVueltas();

                if(vuelta1.currentIndex == vuelta2.currentIndex && vuelta2.currentIndex == vuelta3.currentIndex){
                    msg.setText("You win the big prize");
                } else if (vuelta1.currentIndex == vuelta2.currentIndex || vuelta2.currentIndex == vuelta3.currentIndex || vuelta1.currentIndex == vuelta3.currentIndex) {
                    msg.setText("Little Prize");
                } else{
                    msg.setText("You lose");
                }

                btn.setText("Start");
                isStarted=false;
            } else {
                vuelta1=new Vueltas(new Vueltas.VueltasListener() {
                    @Override
                    public void newImage(final int img) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img1.setImageResource(img);
                            }
                        });
                    }
                }, 200, randomLong(0,200));
                vuelta1.start();
                vuelta2=new Vueltas(new Vueltas.VueltasListener() {
                    @Override
                    public void newImage(final int img) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img2.setImageResource(img);
                            }
                        });
                    }
                }, 200, randomLong(0,200));
                vuelta2.start();
                vuelta3=new Vueltas(new Vueltas.VueltasListener() {
                    @Override
                    public void newImage(final int img) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img3.setImageResource(img);
                            }
                        });
                    }
                }, 200, randomLong(0,200));
                vuelta3.start();
            }
            btn.setText("Stop");
            msg.setText("");
            isStarted=true;
        });

    }

}