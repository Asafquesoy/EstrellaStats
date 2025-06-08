package com.example.estrellastats;

import android.app.DatePickerDialog;
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

        // Configurar spinner de clases
        String[] tipos = {"Niños", "Intermedios", "Jóvenes", "Nuevos creyentes", "Adultos"};
        binding.spinnerClassType.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        );

        // DatePicker para fecha
        binding.editTextDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (DatePicker dp, int yy, int mm, int dd) -> {
                binding.editTextDate.setText(
                        String.format("%04d-%02d-%02d", yy, mm + 1, dd)  // Formato yyyy-MM-dd
                );
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        // Botón guardar datos
        binding.buttonSave.setOnClickListener(v -> {
            StatsDatabaseHelper dbHelper = new StatsDatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String fecha = binding.editTextDate.getText().toString();
            String clase = binding.spinnerClassType.getSelectedItem().toString();
            int attendance = Integer.parseInt(binding.editTextAttendance.getText().toString());
            int onTime = Integer.parseInt(binding.editTextOnTime.getText().toString());
            int bibles = Integer.parseInt(binding.editTextBibles.getText().toString());
            int chapters = Integer.parseInt(binding.editTextChapters.getText().toString());
            int visits = Integer.parseInt(binding.editTextVisits.getText().toString());

            db.execSQL("INSERT INTO estadisticas (fecha, clase, attendance, onTime, bibles, chapters, visits) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{fecha, clase, attendance, onTime, bibles, chapters, visits});

            Toast.makeText(this, "Datos guardados en SQLite", Toast.LENGTH_SHORT).show();

            binding.editTextAttendance.setText("");
            binding.editTextOnTime.setText("");
            binding.editTextBibles.setText("");
            binding.editTextChapters.setText("");
            binding.editTextVisits.setText("");
        });

    }
}
