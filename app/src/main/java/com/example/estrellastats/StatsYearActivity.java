package com.example.estrellastats;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import com.example.estrellastats.databinding.ActivityStatsYearBinding;

public class StatsYearActivity extends AppCompatActivity {
    private ActivityStatsYearBinding binding;
    private String[] clases = {"Todas", "Niños", "Intermedios", "Jóvenes", "Nuevos creyentes", "Adultos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsYearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Estadísticas del año");

        // Spinner de clases
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                clases
        );
        binding.spinnerClass.setAdapter(adapter);

        binding.spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                cargarDatosAnuales(clases[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void cargarDatosAnuales(String clase) {
        // Ejemplo: fetch de BD por mes para la clase
        int[] attendanceMonths = fetchAttendanceByMonth(clase); // longitud 12
        int[] onTimeMonths = fetchOnTimeByMonth(clase);
        // ... otros datos: biblesMonths, chaptersMonths, visitsMonths, etc.

        // Mostrar acumulados (sin gráficos)
        binding.textTotalAttendance.setText("Asistencia total: " + sum(attendanceMonths));
        binding.textTotalOnTime.setText("Puntualidad total: " + sum(onTimeMonths));
        // ... setText para otros acumulados según tus necesidades
    }

    // Métodos de ejemplo para obtener datos y sumar
    private int[] fetchAttendanceByMonth(String clase) {
        // TODO: implementar fetch real
        return new int[12];
    }

    private int[] fetchOnTimeByMonth(String clase) {
        // TODO: implementar fetch real
        return new int[12];
    }

    private int sum(int[] arr) {
        int s = 0;
        for (int v : arr) {
            s += v;
        }
        return s;
    }
}
