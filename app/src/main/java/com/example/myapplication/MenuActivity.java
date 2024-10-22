package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MenuActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);


        Button ruletaBtn = findViewById(R.id.RuletaBTN);
        Button buscaminasBtn=findViewById(R.id.BuscaminasBTN);

        // Configurar el clic en el botón
        ruletaBtn.setOnClickListener(view -> {
            // Crear un intent para navegar a RuletaActivity
            Intent intent = new Intent(MenuActivity.this, RuletaActivity.class);
            startActivity(intent);
        });

        //Click en Busca Minas
        buscaminasBtn.setOnClickListener(view -> {
            Intent intentbuscaminas = new Intent(MenuActivity.this, BuscaMinasActivity.class);
            startActivity(intentbuscaminas);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED; // Evita propagar los insets si ya los usaste
        });
    }
}