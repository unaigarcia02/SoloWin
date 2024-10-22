package com.example.myapplication;

import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;


public class MainActivity extends BaseActivity  {
    private VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        video=(VideoView) findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.casin;
        video.setVideoURI(Uri.parse(path));
        video.setOnCompletionListener(mediaPlayer -> video.start());

        video.start();

        Button buttonJugar = findViewById(R.id.buttonJgr);

        // Asigna un listener para navegar a la segunda actividad
        buttonJugar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aplicar el efecto de desenfoque al presionar el botón
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    View rootView = findViewById(R.id.main);  // Usar el layout raíz
                    RenderEffect blurEffect = RenderEffect.createBlurEffect(15f, 15f, Shader.TileMode.CLAMP);
                    rootView.setRenderEffect(blurEffect);
                }

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}