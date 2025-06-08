package com.example.estrellastats;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import com.example.estrellastats.databinding.ActivityStatsMonthBinding;

public class StatsMonthActivity extends AppCompatActivity {
    private ActivityStatsMonthBinding binding;
    private String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    private String[] clases = {"Todas","Niños","Intermedios","Jóvenes","Nuevos creyentes","Adultos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsMonthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Estadísticas por mes");

        // Configurar spinners
        ArrayAdapter<String> adapterMes = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, meses);
        ArrayAdapter<String> adapterClase = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, clases);
        binding.spinnerMonth.setAdapter(adapterMes);
        binding.spinnerClass.setAdapter(adapterClase);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mes = meses[binding.spinnerMonth.getSelectedItemPosition()];
                String clase = clases[binding.spinnerClass.getSelectedItemPosition()];
                cargarDatosMensuales(mes, clase);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        binding.spinnerMonth.setOnItemSelectedListener(listener);
        binding.spinnerClass.setOnItemSelectedListener(listener);
    }

    private void cargarDatosMensuales(String mes, String clase) {
        // Fetch de BD por semana (longitud 4 o 5)
        int[] weeklyAttendance = fetchAttendanceByWeek(mes, clase);
        int[] weeklyOnTime     = fetchOnTimeByWeek(mes, clase);
        int[] weeklyBibles     = fetchBiblesByWeek(mes, clase);
        int[] weeklyChapters   = fetchChaptersByWeek(mes, clase);
        int[] weeklyVisits     = fetchVisitsByWeek(mes, clase);

        // Mostrar totales mensuales
        binding.textWeeklyAttendance.setText("Asistencia total (mes): " + sum(weeklyAttendance));
        binding.textWeeklyOnTime.setText("Puntualidad total (mes): "  + sum(weeklyOnTime));
        binding.textWeeklyBibles.setText("Biblias total (mes): "       + sum(weeklyBibles));
        binding.textWeeklyChapters.setText("Capítulos total (mes): "     + sum(weeklyChapters));
        binding.textWeeklyVisits.setText("Visitas total (mes): "        + sum(weeklyVisits));
    }

    // Métodos fetch (implementa tu lógica de BD)
    private int[] fetchAttendanceByWeek(String mes, String clase) { return new int[5]; }
    private int[] fetchOnTimeByWeek(String mes, String clase)     { return new int[5]; }
    private int[] fetchBiblesByWeek(String mes, String clase)     { return new int[5]; }
    private int[] fetchChaptersByWeek(String mes, String clase)   { return new int[5]; }
    private int[] fetchVisitsByWeek(String mes, String clase)     { return new int[5]; }

    private int sum(int[] arr) {
        int total = 0;
        for (int v : arr) total += v;
        return total;
    }
}