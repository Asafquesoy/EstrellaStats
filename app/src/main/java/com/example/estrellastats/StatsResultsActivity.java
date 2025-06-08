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

        // Obtener datos del intent
        String filtro = getIntent().getStringExtra("filter");  // "Año", "Mes", "Día"
        String valor = getIntent().getStringExtra("value");    // Valor del filtro (ej: "2025", "06", "2025-06-07")
        String clase = getIntent().getStringExtra("class");    // Clase seleccionada

        // Mostrar clase
        if (clase != null) {
            if (clase.equals("Todas")) {
                binding.textViewClass.setText("Clase: Todas las clases");
            } else {
                binding.textViewClass.setText("Clase: " + clase);
            }
        }

        // Mostrar fecha de filtro
        if (filtro != null && valor != null) {
            String textoFecha = "Fecha: ";
            switch (filtro) {
                case "Año":
                    textoFecha += "Año " + valor;
                    break;
                case "Mes":
                    String mesNombre = numeroAMesNombre(valor);
                    textoFecha += mesNombre != null ? mesNombre + " de " + getIntent().getStringExtra("year") : "Mes " + valor;
                    break;
                case "Día":
                    textoFecha += valor;
                    break;
            }
            binding.textViewDate.setText(textoFecha);
        }

        // Construir cláusula WHERE
        StringBuilder whereClause = new StringBuilder("1=1");

        if (filtro != null && valor != null) {
            switch (filtro) {
                case "Año":
                    whereClause.append(" AND fecha LIKE '").append(valor).append("%'");
                    break;
                case "Mes":
                    String mesNum = valor;
                    if (mesNum.length() == 1) mesNum = "0" + mesNum;
                    whereClause.append(" AND substr(fecha, 6, 2) = '").append(mesNum).append("'");
                    String year = getIntent().getStringExtra("year");
                    if (year != null) {
                        whereClause.append(" AND substr(fecha, 1, 4) = '").append(year).append("'");
                    }
                    break;
                case "Día":
                    whereClause.append(" AND fecha = '").append(valor).append("'");
                    break;
            }
        }

        if (clase != null && !clase.equals("Todas")) {
            whereClause.append(" AND clase = '").append(clase).append("'");
        }

        // Ejecutar consulta
        SQLiteDatabase db = new StatsDatabaseHelper(this).getReadableDatabase();
        String query = "SELECT SUM(attendance), SUM(onTime), SUM(bibles), SUM(chapters), SUM(visits) " +
                "FROM estadisticas WHERE " + whereClause;

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

        // Mostrar resultados
        binding.textResAttendance.setText("Asistencia: " + attendance);
        binding.textResOnTime.setText("Puntualidad: " + onTime);
        binding.textResBibles.setText("Biblias: " + bibles);
        binding.textResChapters.setText("Capítulos leídos: " + chapters);
        binding.textResVisits.setText("Visitas: " + visits);
    }

    // Convertir número de mes a nombre (para mostrar)
    private String numeroAMesNombre(String numero) {
        switch (numero) {
            case "01": return "Enero";
            case "02": return "Febrero";
            case "03": return "Marzo";
            case "04": return "Abril";
            case "05": return "Mayo";
            case "06": return "Junio";
            case "07": return "Julio";
            case "08": return "Agosto";
            case "09": return "Septiembre";
            case "10": return "Octubre";
            case "11": return "Noviembre";
            case "12": return "Diciembre";
            default: return null;
        }
    }
}
