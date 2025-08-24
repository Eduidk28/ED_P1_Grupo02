package com.example.ED_P1_Grupo02.Vista;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecEstructura.R;
import com.example.ED_P1_Grupo02.Controlador.ControladorPerfil;
import com.example.ED_P1_Grupo02.Modelo.Perfil;

;

import java.util.List;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.PerfilViewHolder> {
    private List<Perfil> perfiles;
    private final Context context;
    private final ControladorPerfil controladorPerfil;

    public PerfilAdapter(List<Perfil> perfiles, Context context) {
        this.perfiles = perfiles;
        this.context = context;
        this.controladorPerfil = new ControladorPerfil(context);
    }

    @NonNull
    @Override
    public PerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_perfil, parent, false);
        return new PerfilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerfilViewHolder holder, int position) {
        Perfil perfil = perfiles.get(position);
        Log.d("PerfilAdapter", "onBindViewHolder posición " + position + ": " + perfil.getNombre());
        holder.tvNombre.setText(perfil.getNombre());
        holder.tvCorreo.setText(perfil.getCorreo());

        holder.itemView.setOnClickListener(v -> {
            PasswordDialog dialog = new PasswordDialog(context, perfil, new PasswordDialog.OnPasswordActionsListener() {
                @Override
                public void onPasswordCorrect(Perfil perfil) {
                    Intent intent = new Intent(context, VisualizarGrafoActivity.class);
                    intent.putExtra("perfil", perfil);
                    context.startActivity(intent);
                }

                @Override
                public void onPerfilEliminado(Perfil perfilEliminado) {

                    int position = perfiles.indexOf(perfilEliminado);
                    if (position != -1) {
                        perfiles.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Perfil eliminado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        });
    }

    public void actualizarLista(List<Perfil> nuevosPerfiles) {
        this.perfiles.clear();
        this.perfiles.addAll(nuevosPerfiles);
        Log.d("PerfilAdapter", "actualizarLista con " + nuevosPerfiles.size() + " perfiles");
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return perfiles.size();
    }

    public static class PerfilViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCorreo;

        public PerfilViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCorreo = itemView.findViewById(R.id.tvCorreo);
        }
    }
}