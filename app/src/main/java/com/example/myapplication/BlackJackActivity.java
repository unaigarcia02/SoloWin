package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityBlackJackBinding;

public class BlackJackActivity extends AppCompatActivity {

    private ActivityBlackJackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBlackJackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}