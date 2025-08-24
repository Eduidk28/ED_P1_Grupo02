package com.example.ED_P1_Grupo02;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecEstructura.R;
import com.example.ED_P1_Grupo02.Controlador.ControladorPerfil;
import com.example.ED_P1_Grupo02.Modelo.Perfil;
import com.example.ED_P1_Grupo02.Vista.CrearPerfilActivity;
import com.example.ED_P1_Grupo02.Vista.PerfilAdapter;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PerfilAdapter adapter;
    private ControladorPerfil controladorPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controladorPerfil = new ControladorPerfil(this);
        setupRecyclerView();
        setupButton();
    }

    private void setupRecyclerView() {
        RecyclerView rvPerfiles = findViewById(R.id.rvPerfiles);
        rvPerfiles.setLayoutManager(new LinearLayoutManager(this));

        List<Perfil> perfiles = controladorPerfil.obtenerPerfiles();
        Log.d("MainActivity", "Perfiles cargados: " + perfiles.size());
        adapter = new PerfilAdapter(perfiles, this);
        rvPerfiles.setAdapter(adapter);
    }

    private void setupButton() {
        findViewById(R.id.btnIrPerfil).setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearPerfilActivity.class);
            startActivityForResult(intent, 1001);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {

            List<Perfil> perfilesActualizados = controladorPerfil.obtenerPerfiles();
            adapter.actualizarLista(perfilesActualizados);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        List<Perfil> perfilesActualizados = controladorPerfil.obtenerPerfiles();
        adapter.actualizarLista(perfilesActualizados);
    }

}