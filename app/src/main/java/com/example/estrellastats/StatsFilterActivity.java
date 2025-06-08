package com.example.estrellastats;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.estrellastats.databinding.ActivityStatsFilterBinding;

import java.util.Calendar;

public class StatsFilterActivity extends AppCompatActivity {
    private ActivityStatsFilterBinding binding;
    private StatsDatabaseHelper dbHelper;

    private final String[] ranges = {"Año", "Mes", "Día"};
    private final String[] months = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    private final String[] classes = {"Todas","Niños","Intermedios","Jóvenes","Nuevos creyentes","Adultos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        binding = ActivityStatsFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Filtrar estadísticas");

        dbHelper = new StatsDatabaseHelper(this);

        // Configurar spinners
        binding.spinnerRange.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ranges));
        binding.spinnerMonth.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, months));
        binding.spinnerClass.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, classes));

        // Mostrar/ocultar campos según rango
        binding.spinnerRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String sel = ranges[pos];
                binding.labelYear.setVisibility(sel.equals("Año") || sel.equals("Mes") ? View.VISIBLE : View.GONE);
                binding.editTextYear.setVisibility(sel.equals("Año") || sel.equals("Mes") ? View.VISIBLE : View.GONE);
                binding.labelMonth.setVisibility(sel.equals("Mes") ? View.VISIBLE : View.GONE);
                binding.spinnerMonth.setVisibility(sel.equals("Mes") ? View.VISIBLE : View.GONE);
                binding.labelDate.setVisibility(sel.equals("Día") ? View.VISIBLE : View.GONE);
                binding.editTextDate.setVisibility(sel.equals("Día") ? View.VISIBLE : View.GONE);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // DatePicker
        binding.editTextDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (dp, yy, mm, dd) -> {
                binding.editTextDate.setText(String.format("%04d-%02d-%02d", yy, mm+1, dd));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Botón aplicar filtro
        binding.buttonApplyFilter.setOnClickListener(v -> {
            String range = binding.spinnerRange.getSelectedItem().toString();
            String year = binding.editTextYear.getText().toString();
            int monthIndex = binding.spinnerMonth.getSelectedItemPosition() + 1;
            String month = (monthIndex < 10 ? "0" : "") + monthIndex;
            String date = binding.editTextDate.getText().toString();
            String clase = binding.spinnerClass.getSelectedItem().toString();

            // Obtener estadísticas
            int[] results = getStats(range, year, month, date, clase);

            // Lanzar resultados
            Intent intent = new Intent(this, StatsResultsActivity.class);
            intent.putExtra("attendance", results[0]);
            intent.putExtra("onTime", results[1]);
            intent.putExtra("bibles", results[2]);
            intent.putExtra("chapters", results[3]);
            intent.putExtra("visits", results[4]);

            intent.putExtra("filter", range);
            intent.putExtra("value", range.equals("Día") ? date : (range.equals("Mes") ? month : year));
            intent.putExtra("year", year); // importante para filtrar mes correctamente
            intent.putExtra("class", clase);

            startActivity(intent);
        });
    }

    private int[] getStats(String range, String year, String month, String date, String clase) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String where = "";
        String[] args = new String[]{};

        switch (range) {
            case "Año":
                where = "substr(fecha, 1, 4) = ?";
                args = new String[]{year};
                break;
            case "Mes":
                where = "substr(fecha, 1, 7) = ?";
                args = new String[]{year + "-" + month};
                break;
            case "Día":
                where = "fecha = ?";
                args = new String[]{date};
                break;
        }

        if (!clase.equals("Todas")) {
            if (!where.isEmpty()) {
                where += " AND clase = ?";
                args = append(args, clase);
            } else {
                where = "clase = ?";
                args = new String[]{clase};
            }
        }

        String query = "SELECT SUM(attendance), SUM(onTime), SUM(bibles), SUM(chapters), SUM(visits) FROM estadisticas";
        if (!where.isEmpty()) {
            query += " WHERE " + where;
        }

        int[] result = new int[5];
        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < 5; i++) {
                result[i] = cursor.isNull(i) ? 0 : cursor.getInt(i);
            }
        }
        cursor.close();
        db.close();
        return result;
    }

    private String[] append(String[] original, String value) {
        String[] result = new String[original.length + 1];
        System.arraycopy(original, 0, result, 0, original.length);
        result[original.length] = value;
        return result;
    }
}
