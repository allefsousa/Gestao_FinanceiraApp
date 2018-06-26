package br.com.felipedeveloper.gestaofinanceira.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Modelo.Usuario;
import br.com.felipedeveloper.gestaofinanceira.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * adapter responsavel por exibir os usuarios do grupo
 */
public class AdicionarUsuarioAdapter extends RecyclerView.Adapter<AdicionarUsuarioAdapter.ViewHoderUsuario> {
    @NonNull
    private List<Usuario> usuarioList;
    private Context context;

    public AdicionarUsuarioAdapter(List<Usuario> usuarioList, Context context) {
        this.usuarioList = usuarioList;
        this.context = context;
    }

    @Override
    public ViewHoderUsuario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_grupo_add_user, parent, false);


        return new AdicionarUsuarioAdapter.ViewHoderUsuario(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoderUsuario holder, int position) {
        holder.textNome.setText(usuarioList.get(position).getUsuarioNome());
        holder.textEmail.setText(usuarioList.get(position).getUsuarioEmail());
        if (usuarioList.get(position).getFotoUrl() != null){
            Glide.with(context).load(usuarioList.get(position).getFotoUrl()).into(holder.imageView); // baixando a foto do usuario
        }

    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public void clear() {
        usuarioList.clear();
        notifyDataSetChanged();
    }

    public class ViewHoderUsuario extends RecyclerView.ViewHolder {
       private CircleImageView imageView;
       private TextView textNome;
       private TextView textEmail;

        public ViewHoderUsuario(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.circleimageuser);
            textNome = itemView.findViewById(R.id.edititemnome);
            textEmail = itemView.findViewById(R.id.edititememail);
        }
    }
}
