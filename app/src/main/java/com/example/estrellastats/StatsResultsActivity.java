package com.example.estrellastats;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.estrellastats.databinding.ActivityStatsResultsBinding;

public class StatsResultsActivity extends AppCompatActivity {
    private ActivityStatsResultsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Resultados");

        // Recoger filtro y valor desde Intent
        String filtro = getIntent().getStringExtra("filter");  // "Año", "Mes" o "Día"
        String valor = getIntent().getStringExtra("value");    // valor del filtro, ej: "2025", "2025-06", "2025-06-07"
        String clase = getIntent().getStringExtra("class");    // clase seleccionada (ej: "Niños", "Todas", etc.)

        // Construir cláusula WHERE segura
        StringBuilder whereClause = new StringBuilder("1=1"); // base para evitar error SQL

        if (filtro != null && valor != null) {
            switch (filtro) {
                case "Año":
                    // Fecha formato yyyy-MM-dd, filtramos por año con LIKE '2025%'
                    whereClause.append(" AND fecha LIKE '").append(valor).append("%'");
                    break;
                case "Mes":
                    // Fecha formato yyyy-MM-dd, filtramos por mes con LIKE '2025-06%'
                    whereClause.append(" AND fecha LIKE '").append(valor).append("%'");
                    break;
                case "Día":
                    // Fecha exacta
                    whereClause.append(" AND fecha = '").append(valor).append("'");
                    break;
            }
        }

        if (clase != null && !clase.equals("Todas")) {
            whereClause.append(" AND clase = '").append(clase).append("'");
        }

        // Abrir BD y hacer consulta
        SQLiteDatabase db = new StatsDatabaseHelper(this).getReadableDatabase();
        String query = "SELECT SUM(attendance), SUM(onTime), SUM(bibles), SUM(chapters), SUM(visits) " +
                "FROM estadisticas WHERE " + whereClause.toString();

        Cursor cursor = db.rawQuery(query, null);

        int attendance = 0, onTime = 0, bibles = 0, chapters = 0, visits = 0;
        if (cursor.moveToFirst()) {
            attendance = cursor.isNull(0) ? 0 : cursor.getInt(0);
            onTime     = cursor.isNull(1) ? 0 : cursor.getInt(1);
            bibles     = cursor.isNull(2) ? 0 : cursor.getInt(2);
            chapters   = cursor.isNull(3) ? 0 : cursor.getInt(3);
            visits     = cursor.isNull(4) ? 0 : cursor.getInt(4);
        }
        cursor.close();
        db.close();

        // Mostrar resultados en UI
        binding.textResAttendance.setText("Asistencia: " + attendance);
        binding.textResOnTime.setText("Puntualidad: " + onTime);
        binding.textResBibles.setText("Biblias: " + bibles);
        binding.textResChapters.setText("Capítulos leídos: " + chapters);
        binding.textResVisits.setText("Visitas: " + visits);
    }
}
