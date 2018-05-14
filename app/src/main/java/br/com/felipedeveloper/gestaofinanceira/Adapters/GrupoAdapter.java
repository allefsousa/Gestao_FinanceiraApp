package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.Grupo;
import br.com.felipedeveloper.gestaofinanceira.R;

/**
 * Created by allef on 09/05/2018.
 */

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.ViewHolderGrupo> {
    private Context context;
    private List<Grupo> grupoList;

    public GrupoAdapter(Context context, List<Grupo> grupoList) {
        this.context = context;
        this.grupoList = grupoList;
    }


    @NonNull
    @Override
    public GrupoAdapter.ViewHolderGrupo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_grupos, parent, false);
        return new GrupoAdapter.ViewHolderGrupo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoAdapter.ViewHolderGrupo holder, int position) {
        holder.textTituloGrupo.setText(grupoList.get(position).getNomeGrupo());
        holder.textSaldoGrupo.setText(grupoList.get(position).getSaldoGrupo());
        render(holder, position);
    }

    private void render(ViewHolderGrupo holder, int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return grupoList.size();
    }

    public class ViewHolderGrupo extends RecyclerView.ViewHolder {
        private TextView textTituloGrupo;
        private TextView textSaldoGrupo;
        private CardView cardView;

        public ViewHolderGrupo(View itemView) {
            super(itemView);
            textTituloGrupo = itemView.findViewById(R.id.titulogrupo);
            textSaldoGrupo = itemView.findViewById(R.id.textsaldogrupo);
            cardView = itemView.findViewById(R.id.cardViewcontasbancaria);
        }
    }
}
