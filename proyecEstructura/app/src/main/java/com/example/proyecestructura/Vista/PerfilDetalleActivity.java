package com.example.ED_P1_Grupo02.Vista;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ED_P1_Grupo02.Modelo.Perfil;
import com.example.proyecEstructura.R;


public class PerfilDetalleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_detalle);

        Perfil perfil = (Perfil) getIntent().getSerializableExtra("perfil");

        TextView tvNombre = findViewById(R.id.tvNombreDetalle);
        TextView tvCorreo = findViewById(R.id.tvCorreoDetalle);

        tvNombre.setText(perfil.getNombre());
        tvCorreo.setText(perfil.getCorreo());

    }
}