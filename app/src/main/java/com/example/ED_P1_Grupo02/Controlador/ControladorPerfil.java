package com.example.ED_P1_Grupo02.Controlador;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.example.ED_P1_Grupo02.Modelo.Perfil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

public class ControladorPerfil {
    private final Context context;
    private ArrayList<Perfil> listaPerfiles;

    public ControladorPerfil(Context context) {
        this.context = context;
        this.listaPerfiles = cargarPerfiles();
    }


    public boolean agregarPerfil(Perfil perfil) {

        Log.d("Controlador", "Contraseña a guardar: " + perfil.getContrasena());

        if (perfilExiste(perfil.getNombre())) {
            return false;
        }

        guardarPerfilEnArchivo(perfil);
        listaPerfiles= cargarPerfiles();
        return true;
    }


    public boolean eliminarPerfil(Perfil perfil) {
        File file = new File(context.getFilesDir(), getFileName(perfil.getNombre()));
        if (file.exists() && file.delete()) {
            listaPerfiles.remove(perfil);
            Log.d("ControladorPerfil", "Perfil eliminado: " + perfil.getNombre());
            return true;
        }
        Log.d("ControladorPerfil", "No se pudo eliminar perfil: " + perfil.getNombre());
        return false;
    }


    private void guardarPerfilEnArchivo(Perfil perfil) {
        try {

            Perfil perfilEncriptado = new Perfil(
                    perfil.getNombre(),
                    perfil.getCorreo(),
                    encryptPassword(perfil.getContrasena())
            );

            try (FileOutputStream fos = context.openFileOutput(getFileName(perfil.getNombre()), Context.MODE_PRIVATE);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(perfilEncriptado);
                Log.d("ControladorPerfil", "Perfil guardado: " + perfil.getNombre());
                Log.d("ControladorPerfil", "Contraseña encriptada guardada: " + perfilEncriptado.getContrasena());
            }
        } catch (Exception e) {
            Log.e("ControladorPerfil", "Error al guardar perfil", e);
        }
    }


    private ArrayList<Perfil> cargarPerfiles() {
        ArrayList<Perfil> perfiles = new ArrayList<>();
        File[] files = context.getFilesDir().listFiles((dir, name) -> name.endsWith(".dat") && !name.equals("grafo.dat"));

        if (files != null) {
            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file);
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    Perfil perfil = (Perfil) ois.readObject();
                    perfiles.add(perfil);
                } catch (Exception e) {
                    Log.e("ControladorPerfil", "Error cargando perfil desde: " + file.getName(), e);
                }
            }
        }
        return perfiles;
    }

    public boolean perfilExiste(String nombre) {
        File file = new File(context.getFilesDir(), getFileName(nombre));
        return file.exists();
    }


    public ArrayList<Perfil> obtenerPerfiles() {
        this.listaPerfiles = cargarPerfiles();
        return new ArrayList<>(listaPerfiles);
    }


    private String getFileName(String nombre) {
        return nombre + ".dat";
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest();
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e("Controlador", "Error en encriptación", e);
            return password;
        }
    }

    public boolean verifyPassword(String inputPassword, String storedPassword) {
        String encryptedInput = encryptPassword(inputPassword);
        return encryptedInput.equals(storedPassword);
    }
}
