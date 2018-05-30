package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.Grupo;
import br.com.felipedeveloper.gestaofinanceira.R;
import br.com.felipedeveloper.gestaofinanceira.View.TransacoesGrupoActivity;


public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.ViewHolderGrupo> {
    private Context context;
    private List<Grupo> grupoList;
    private NumberFormat df;

    public GrupoAdapter(Context context, List<Grupo> grupoList) {
        this.context = context;
        this.grupoList = grupoList;
    }


    @NonNull
    @Override
    public GrupoAdapter.ViewHolderGrupo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_adapter_cardview, parent, false);
        return new GrupoAdapter.ViewHolderGrupo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoAdapter.ViewHolderGrupo holder, int position) {
        holder.textTituloGrupo.setText(grupoList.get(position).getNomeGrupo());
        holder.textSaldoGrupo.setText(String.valueOf(df.format(grupoList.get(position).getSaldoGrupo())));
        render(holder, position);
    }

    /**
     * neste cenario é necessario passar o nome do grupo para ser salvo junto com o lançamento para facilicatr o filtro
     * @param holder
     * @param position
     */
    private void render(final ViewHolderGrupo holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,TransacoesGrupoActivity.class);
                intent.putExtra("idgrupo",grupoList.get(position).getIdGrupo());
               context.startActivity(intent);
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
            df = new DecimalFormat("#0.00");
            textTituloGrupo = itemView.findViewById(R.id.tituloitem);
            textSaldoGrupo = itemView.findViewById(R.id.saldoitem);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
