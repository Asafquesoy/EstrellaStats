package com.example.estrellastats;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.estrellastats.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonEnter.setOnClickListener(v -> {
            startActivity(new Intent(this, EnterDataActivity.class));
        });
        binding.buttonStats.setOnClickListener(v -> {
            startActivity(new Intent(this, StatsOptionsActivity.class));
        });
    }
}