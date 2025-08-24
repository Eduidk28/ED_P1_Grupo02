package com.example.ED_P1_Grupo02.Vista;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ED_P1_Grupo02.Controlador.ControladorPerfil;
import com.example.ED_P1_Grupo02.Modelo.Perfil;
import com.example.proyecEstructura.R;


public class PasswordDialog extends Dialog {
    private final Perfil perfil;
    private final OnPasswordActionsListener listener;
    private final ControladorPerfil controladorPerfil;
    private EditText etPassword;

    public interface OnPasswordActionsListener {
        void onPasswordCorrect(Perfil perfil);
        void onPerfilEliminado(Perfil perfil);
    }

    public PasswordDialog(@NonNull Context context, Perfil perfil, OnPasswordActionsListener listener) {
        super(context);
        this.perfil = perfil;
        this.listener = listener;
        this.controladorPerfil = new ControladorPerfil(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_password);

        etPassword = findViewById(R.id.etPassword);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        btnConfirm.setOnClickListener(v -> verifyPassword());
        btnEliminar.setOnClickListener(v -> mostrarConfirmacionEliminacion());
    }

    private void verifyPassword() {
        String enteredPassword = etPassword.getText().toString().trim();

        if (enteredPassword.isEmpty()) {
            etPassword.setError("Ingrese la contraseña");
            return;
        }

        if (controladorPerfil.verifyPassword(enteredPassword, perfil.getContrasena())) {
            Intent intent = new Intent(getContext(), VisualizarGrafoActivity.class);
            intent.putExtra("perfil", perfil);
            getContext().startActivity(intent);

            listener.onPasswordCorrect(perfil);
            dismiss();
        } else {
            etPassword.setError("Contraseña incorrecta");
            Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarConfirmacionEliminacion() {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Perfil")
                .setMessage("¿Estás seguro de que deseas eliminar el perfil " + perfil.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    if (controladorPerfil.eliminarPerfil(perfil)) {
                        listener.onPerfilEliminado(perfil);
                        Toast.makeText(getContext(), "Perfil eliminado", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Error al eliminar el perfil", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
