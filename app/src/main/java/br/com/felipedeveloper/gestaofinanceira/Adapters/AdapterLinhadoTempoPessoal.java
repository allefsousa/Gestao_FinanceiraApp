package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.R;

public class AdapterLinhadoTempoPessoal extends RecyclerView.Adapter<AdapterLinhadoTempoPessoal.ViewHolder> {

    String nomefinanceiro;
    List<String> lancamentoList;
    private Context context;
    private DecimalFormat df;
    private SortedList<Lancamento> lancamentoSortedList = new SortedList<Lancamento>(Lancamento.class, new SortedList.Callback<Lancamento>() {
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
        public int compare(Lancamento data1, Lancamento data2) {
            return (int) (data1.getCreatedAt() - data2.getCreatedAt());
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Lancamento oldItem, Lancamento newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Lancamento item1, Lancamento item2) {
            return item1.getTitulo().equals(item2.getTitulo());
        }
    });


    public AdapterLinhadoTempoPessoal(Context context, String nomeGrupo) {
        this.context = context;
        this.nomefinanceiro = nomeGrupo;
        lancamentoList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_movimentacoes_pessoal, parent, false);
        return new ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.render(lancamentoSortedList, position);

    }
    public void addItemm(Lancamento lancamento) {
        lancamentoSortedList.add(lancamento);
        notifyDataSetChanged();
    }

    public void removeItem(Lancamento data) {
        lancamentoList.remove(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lancamentoSortedList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        TextView data;
        TextView titulo;
        TextView valor;
        TextView status;
        CardView cardViewlinha;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            df = new DecimalFormat("#0.00");
            cardViewlinha = itemView.findViewById(R.id.cardmovimentacao);
            mTimelineView = itemView.findViewById(R.id.time_marker);
            titulo = itemView.findViewById(R.id.text_timeline_title);
            valor = itemView.findViewById(R.id.text_timeline_valor);
            data = itemView.findViewById(R.id.text_timeline_date);
            status = itemView.findViewById(R.id.editstatusop);
            cardViewlinha.setUseCompatPadding(true);
            mTimelineView.initLine(viewType);

        }


        private void render(SortedList<Lancamento> sortedList, int pos) {
            cardViewlinha.setUseCompatPadding(true);



                // TODO: 21/05/2018  Dois metodos igal refatorar e so azer a chamada

                Integer statusop = (sortedList.get(pos).getStatusOp());
                if (pos == 0) {
                    mTimelineView.initLine(1);
                }
                if (statusop != null) {
                    switch (statusop) {
                        case 0:
                            mTimelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker));
                            cardViewlinha.setCardBackgroundColor(context.getResources().getColor(R.color.colorSwitchdebito));
                            mTimelineView.setMarkerColor(context.getResources().getColor(R.color.colorSwitchdebito));
                            status.setText("DEBITO");
                            break;
                        case 1:
                            mTimelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker));
                            mTimelineView.setMarkerColor(context.getResources().getColor(R.color.listagastos));
                            cardViewlinha.setCardBackgroundColor(context.getResources().getColor(R.color.listagastos));
                            status.setText("CREDITO");
                            break;
                    }
                }


                if (pos == (sortedList.size() - 1)) {
                    mTimelineView.setEndLine(context.getResources().getColor(R.color.float_transparent), 4);
                }

                titulo.setText(sortedList.get(pos).getTitulo());
                valor.setText(String.valueOf(df.format(sortedList.get(pos).getValor())));
                data.setText(sortedList.get(pos).getData());

                }
            }
        }



