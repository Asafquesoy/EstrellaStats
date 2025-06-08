package com.example.estrellastats;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.estrellastats.databinding.ActivityDeleteDataBinding;

import java.util.Calendar;

public class DeleteDataActivity extends AppCompatActivity {
    private ActivityDeleteDataBinding binding;
    private StatsDatabaseHelper dbHelper;

    private String[] clases = {"Niños", "Intermedios", "Jóvenes", "Nuevos creyentes", "Adultos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Eliminar datos");

        dbHelper = new StatsDatabaseHelper(this);
        binding.editTextDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (DatePicker dp, int yy, int mm, int dd) -> {
                // Formato yyyy-MM-dd
                binding.editTextDate.setText(String.format("%04d-%02d-%02d", yy, mm + 1, dd));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        // Configurar spinner de clases
        binding.spinnerClass.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, clases));

        // Botón buscar datos existentes
        binding.buttonSearch.setOnClickListener(v -> {
            String fecha = binding.editTextDate.getText().toString().trim();
            String clase = binding.spinnerClass.getSelectedItem().toString();

            if (fecha.isEmpty()) {
                Toast.makeText(this, "Ingrese la fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            mostrarDatos(fecha, clase);
        });

        // Botón eliminar datos
        binding.buttonDelete.setOnClickListener(v -> {
            String fecha = binding.editTextDate.getText().toString().trim();
            String clase = binding.spinnerClass.getSelectedItem().toString();

            if (fecha.isEmpty()) {
                Toast.makeText(this, "Ingrese la fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            eliminarDatos(fecha, clase);
        });
    }

    private void mostrarDatos(String fecha, String clase) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT attendance, onTime, bibles, chapters, visits FROM estadisticas WHERE fecha = ? AND clase = ?";
        Cursor cursor = db.rawQuery(query, new String[]{fecha, clase});

        if (cursor.moveToFirst()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Asistencia: ").append(cursor.getInt(0)).append("\n");
            sb.append("Puntualidad: ").append(cursor.getInt(1)).append("\n");
            sb.append("Biblias: ").append(cursor.getInt(2)).append("\n");
            sb.append("Capítulos leídos: ").append(cursor.getInt(3)).append("\n");
            sb.append("Visitas: ").append(cursor.getInt(4));
            binding.textViewData.setText(sb.toString());
        } else {
            binding.textViewData.setText("No se encontraron datos para esa fecha y clase.");
        }
        cursor.close();
        db.close();
    }

    private void eliminarDatos(String fecha, String clase) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("estadisticas", "fecha = ? AND clase = ?", new String[]{fecha, clase});
        db.close();

        if (rows > 0) {
            Toast.makeText(this, "Datos eliminados correctamente", Toast.LENGTH_SHORT).show();
            binding.textViewData.setText("");
            binding.editTextDate.setText("");
        } else {
            Toast.makeText(this, "No se encontraron datos para eliminar", Toast.LENGTH_SHORT).show();
        }
    }
}
