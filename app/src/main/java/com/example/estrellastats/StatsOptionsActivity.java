package com.example.estrellastats;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.estrellastats.databinding.ActivityStatsOptionsBinding;

public class StatsOptionsActivity extends AppCompatActivity {
    private ActivityStatsOptionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Ver estadísticas");

        binding.buttonYear.setOnClickListener(v -> {
            startActivity(new Intent(this, StatsYearActivity.class));
        });
        binding.buttonDate.setOnClickListener(v -> {
            startActivity(new Intent(this, StatsDateActivity.class));
        });
    }
}