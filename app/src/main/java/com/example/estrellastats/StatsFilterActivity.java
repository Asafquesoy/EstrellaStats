package com.example.estrellastats;

import android.app.DatePickerDialog;
import android.content.Intent;
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

    private String[] ranges = {"Año", "Mes", "Día"};
    private String[] months = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    private String[] classes = {"Todas","Niños","Intermedios","Jóvenes","Nuevos creyentes","Adultos"};

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

        // DatePicker para seleccionar fecha en rango Día
        binding.editTextDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (dp, yy, mm, dd) -> {
                // Guardamos fecha en formato yyyy-MM-dd para la BD
                binding.editTextDate.setText(String.format("%04d-%02d-%02d", yy, mm+1, dd));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Botón aplicar filtro
        binding.buttonApplyFilter.setOnClickListener(v -> {
            String range = binding.spinnerRange.getSelectedItem().toString();
            String year = binding.editTextYear.getText().toString().trim();
            int monthIndex = binding.spinnerMonth.getSelectedItemPosition() + 1;
            String month = (monthIndex < 10 ? "0" : "") + monthIndex;
            String date = binding.editTextDate.getText().toString().trim();
            String clase = binding.spinnerClass.getSelectedItem().toString();

            // Validar año si es necesario
            if ((range.equals("Año") || range.equals("Mes")) && year.isEmpty()) {
                binding.editTextYear.setError("Ingrese un año");
                binding.editTextYear.requestFocus();
                return;
            }
            if (range.equals("Día") && date.isEmpty()) {
                binding.editTextDate.setError("Seleccione una fecha");
                binding.editTextDate.requestFocus();
                return;
            }

            // Construir valor de filtro para enviar a StatsResultsActivity
            String filterValue = "";
            switch (range) {
                case "Año":
                    filterValue = year;           // Ej: "2025"
                    break;
                case "Mes":
                    filterValue = year + "-" + month;  // Ej: "2025-06"
                    break;
                case "Día":
                    filterValue = date;           // Ej: "2025-06-07"
                    break;
            }

            Intent intent = new Intent(this, StatsResultsActivity.class);
            intent.putExtra("filter", range);
            intent.putExtra("value", filterValue);
            intent.putExtra("class", clase);
            startActivity(intent);
        });
    }
}
