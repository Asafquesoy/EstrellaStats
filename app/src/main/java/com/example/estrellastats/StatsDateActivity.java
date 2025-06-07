package com.example.estrellastats;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.estrellastats.databinding.ActivityStatsDateBinding;
import java.util.Calendar;

public class StatsDateActivity extends AppCompatActivity {
    private ActivityStatsDateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Estadísticas por fecha");

        // DatePicker para elegir fecha de consulta
        binding.editTextQueryDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (dp, yy, mm, dd) -> {
                String formatted = String.format("%02d/%02d/%04d", dd, mm+1, yy);
                binding.editTextQueryDate.setText(formatted);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.buttonFetch.setOnClickListener(v -> {
            String date = binding.editTextQueryDate.getText().toString();
            // Aquí debes consultar tu base de datos por esa fecha
            // Ejemplo ficticio:
            int attendance = 30;
            int onTime = 26;

            binding.textDateAttendance.setText("Asistencia: " + attendance);
            binding.textDateOnTime.setText("Personas a tiempo: " + onTime);
        });
    }
}