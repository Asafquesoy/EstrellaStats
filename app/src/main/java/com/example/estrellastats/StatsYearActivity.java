package com.example.estrellastats;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.estrellastats.databinding.ActivityStatsYearBinding;

public class StatsYearActivity extends AppCompatActivity {
    private ActivityStatsYearBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsYearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Estadísticas del año");

        // Aquí cargarías datos de tu base de datos para el año actual
        // Ejemplo ficticio:
        int totalAttendance = 1200;
        int totalOnTime = 950;

        binding.textTotalAttendance.setText("Asistencia total: " + totalAttendance);
        binding.textTotalOnTime.setText("Personas a tiempo total: " + totalOnTime);
    }
}