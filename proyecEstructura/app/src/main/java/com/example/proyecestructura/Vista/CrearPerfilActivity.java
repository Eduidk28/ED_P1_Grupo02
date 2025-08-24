package com.example.ED_P1_Grupo02.Vista;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ED_P1_Grupo02.Controlador.ControladorPerfil;
import com.example.ED_P1_Grupo02.Modelo.Perfil;
import com.example.proyecEstructura.R;


public class CrearPerfilActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo, etContrasena;
    private Button btnGuardar, btnCargar;
    private ControladorPerfil controladorPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        controladorPerfil = new ControladorPerfil(this);
        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String correo = etCorreo.getText().toString();
            String contrasena = etContrasena.getText().toString();

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Perfil nuevoPerfil = new Perfil(nombre, correo, contrasena);
            ControladorPerfil controlador = new ControladorPerfil(this);

            if (controlador.agregarPerfil(nuevoPerfil)) {
                Toast.makeText(this, "Perfil creado", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "El perfil ya existe", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
