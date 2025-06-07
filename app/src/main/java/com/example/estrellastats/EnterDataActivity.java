package com.example.estrellastats;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.estrellastats.databinding.MenuInicioBinding;
import java.util.Calendar;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

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

        String[] tipos = {"Niños", "Intermedios", "Jóvenes", "Nuevos creyentes", "Adultos"};
        binding.spinnerClassType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos));

        binding.editTextDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (DatePicker dp, int yy, int mm, int dd) -> {
                binding.editTextDate.setText(String.format("%02d/%02d/%04d", dd, mm+1, yy));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.buttonSave.setOnClickListener(v -> {
            // guardar lógica...
            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
        });
    }
}