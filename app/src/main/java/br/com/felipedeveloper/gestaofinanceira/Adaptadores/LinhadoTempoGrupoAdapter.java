package br.com.felipedeveloper.gestaofinanceira.Adaptadores;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.google.firebase.database.DataSnapshot;

import java.text.DecimalFormat;

import br.com.felipedeveloper.gestaofinanceira.Modelo.LancamentoGrupo;
import br.com.felipedeveloper.gestaofinanceira.R;

/**
 * Exibindo os itens da linha do tempo do grupo
 * quem foi que fez o lancamento
 * se foi credito ou debito qual o valor
 * qual a cor que deve ser
 */
public class LinhadoTempoGrupoAdapter extends RecyclerView.Adapter<LinhadoTempoGrupoAdapter.ViewHolder> {

    private Context context;
    private String idGrupoSelecionado;
    private DecimalFormat df;

    private SortedList<DataSnapshot> sortedList = new SortedList<>(DataSnapshot.class, new SortedList.Callback<DataSnapshot>() {
        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);

        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);

        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemRangeInserted(fromPosition, toPosition);

        }

        @Override
        public int compare(DataSnapshot data1, DataSnapshot data2) {
            return (int) (data1.child("createdAt").getValue(Long.class) - data2.child("createdAt").getValue(Long.class));
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(DataSnapshot oldItem, DataSnapshot newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(DataSnapshot item1, DataSnapshot item2) {
            return item1.getKey().equals(item2.getKey());
        }
    });

    public LinhadoTempoGrupoAdapter(Context context, String nomeGrupo) {
        this.context = context;
        idGrupoSelecionado = nomeGrupo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_movimentacoes_grupo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.render(sortedList.get(position), position);
    }

    public void addItem(DataSnapshot data) {
        LancamentoGrupo lancamento = data.getValue(LancamentoGrupo.class); // recuperando o lançamento
        if (!lancamento.getNomeGrupo().isEmpty()) {
            if (lancamento.getNomeGrupo().equals(idGrupoSelecionado)) { // comparando a id do grupo selecionado com  o nome do grupo
                // caso sejam iguais exibir seus gastos
                sortedList.add(data);
                notifyDataSetChanged();
            }
        }
    }

    public void removeItem(DataSnapshot data) {
        sortedList.remove(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }


    @Override
    public int getItemCount() {
        return sortedList.size();
    }


    /**
     * elementos da view que irei exibir
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        TextView data;
        TextView titulo;
        TextView valor;
        TextView status;
        TextView nomeUsuario;
        CardView cardViewlinha;

        public ViewHolder(View itemView) {
            super(itemView);
            df = new DecimalFormat("#,###,##");
            cardViewlinha = itemView.findViewById(R.id.cardmovimentacao);
            mTimelineView = itemView.findViewById(R.id.time_marker);
            titulo = itemView.findViewById(R.id.text_timeline_title);
            valor = itemView.findViewById(R.id.text_timeline_valor);
            data = itemView.findViewById(R.id.text_timeline_date);
            status = itemView.findViewById(R.id.editstatusop);
            nomeUsuario = itemView.findViewById(R.id.text_timeline_nomeusuario);
            cardViewlinha.setUseCompatPadding(true);
        }

        /**
         * montando a linha do tempo de gastos
         *
         * @param dataSnapshot
         * @param pos
         */
        private void render(DataSnapshot dataSnapshot, int pos) {
            cardViewlinha.setUseCompatPadding(true);


            Integer statusop = (dataSnapshot.child("statusOp").getValue(Integer.class));
            if (pos == 0) {
                mTimelineView.initLine(1);

            }
            if (statusop != null) {
                switch (statusop) {
                    case 0: //DEBITO
                        configTimelineCreditoDebito(context.getResources().getDrawable(R.drawable.ic_marker), context.getResources().getColor(R.color.colorSwitchdebito), "DEBITO");
                        status.setText("DEBITO");
                        break;
                    case 1:// CREDITO
                        configTimelineCreditoDebito(context.getResources().getDrawable(R.drawable.ic_marker), context.getResources().getColor(R.color.listagastos), "CREDITO");
                        break;
                }
            }


            if (pos == (sortedList.size() - 1)) {
                mTimelineView.setEndLine(context.getResources().getColor(R.color.float_transparent), 4);
            }
            // Montando e exbindo o texto da view
            String sTitulo = "Titulo: " + (dataSnapshot.child("titulo").getValue(String.class));
            String sValor = "Valor: " + (String.valueOf(df.format(dataSnapshot.child("valor").getValue(Double.class))));
            String sData = "Data: " + (dataSnapshot.child("data").getValue(String.class));
            String sNome = "Nome: " + (dataSnapshot.child("nomeColaborador").getValue(String.class));
            titulo.setText(sTitulo);
            valor.setText(sValor);
            data.setText(sData);
            nomeUsuario.setText(sNome);


        }

        private void configTimelineCreditoDebito(Drawable drawable, int color, String opfinanceira) {
            mTimelineView.setMarker(drawable);
            mTimelineView.setMarkerColor(color);
            cardViewlinha.setCardBackgroundColor(color);
            status.setText(opfinanceira);
        }
    }


}
