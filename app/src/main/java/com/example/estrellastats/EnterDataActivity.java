package com.example.estrellastats;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.estrellastats.databinding.MenuInicioBinding;

import java.util.Calendar;

public class EnterDataActivity extends AppCompatActivity {
    private MenuInicioBinding binding;
    private StatsDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, b.bottom);
            return insets;
        });

        binding = MenuInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Ingresar datos");

        dbHelper = new StatsDatabaseHelper(this);

        // Configurar spinner de clases
        String[] tipos = {"Niños", "Intermedios", "Jóvenes", "Nuevos creyentes", "Adultos"};
        binding.spinnerClassType.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        );

        // DatePicker para fecha (guardamos fecha en formato yyyy-MM-dd para facilitar filtros)
        binding.editTextDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (DatePicker dp, int yy, int mm, int dd) -> {
                // Formato yyyy-MM-dd
                binding.editTextDate.setText(String.format("%04d-%02d-%02d", yy, mm + 1, dd));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        // Botón guardar datos con validación
        binding.buttonSave.setOnClickListener(v -> {
            String fecha = binding.editTextDate.getText().toString();
            String clase = binding.spinnerClassType.getSelectedItem().toString();

            // Validar que los campos no estén vacíos
            if (fecha.isEmpty() || clase.isEmpty()
                    || binding.editTextAttendance.getText().toString().isEmpty()
                    || binding.editTextOnTime.getText().toString().isEmpty()
                    || binding.editTextBibles.getText().toString().isEmpty()
                    || binding.editTextChapters.getText().toString().isEmpty()
                    || binding.editTextVisits.getText().toString().isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar si ya hay datos para esa fecha y clase
            if (datosYaExisten(fecha, clase)) {
                Toast.makeText(this, "Ya existen datos para esta clase y fecha", Toast.LENGTH_LONG).show();
                return;
            }

            int attendance = Integer.parseInt(binding.editTextAttendance.getText().toString());
            int onTime = Integer.parseInt(binding.editTextOnTime.getText().toString());
            int bibles = Integer.parseInt(binding.editTextBibles.getText().toString());
            int chapters = Integer.parseInt(binding.editTextChapters.getText().toString());
            int visits = Integer.parseInt(binding.editTextVisits.getText().toString());

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO estadisticas (fecha, clase, attendance, onTime, bibles, chapters, visits) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{fecha, clase, attendance, onTime, bibles, chapters, visits});
            db.close();

            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();

            // Limpiar campos después de guardar
            binding.editTextDate.setText("");
            binding.editTextAttendance.setText("");
            binding.editTextOnTime.setText("");
            binding.editTextBibles.setText("");
            binding.editTextChapters.setText("");
            binding.editTextVisits.setText("");
        });
    }

    // Método que verifica si ya existen datos para la fecha y clase indicadas
    private boolean datosYaExisten(String fecha, String clase) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM estadisticas WHERE fecha = ? AND clase = ?";
        Cursor cursor = db.rawQuery(query, new String[]{fecha, clase});
        boolean existe = false;
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            existe = count > 0;
        }
        cursor.close();
        db.close();
        return existe;
    }
}
