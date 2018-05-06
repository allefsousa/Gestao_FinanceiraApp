package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.R;

public class AdapterLinhadoTempo extends RecyclerView.Adapter<AdapterLinhadoTempo.ViewHolder> {

    private Context context;

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

    public AdapterLinhadoTempo(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_movimentacoes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.render(sortedList.get(position), position);
    }

    public void addItem(DataSnapshot data) {
        sortedList.add(data);
        notifyDataSetChanged();
    }

    public void removeItem(DataSnapshot data) {
        sortedList.remove(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        TextView data;
        TextView titulo;
        TextView valor;
        TextView status;
        CardView cardViewlinha;

        public ViewHolder(View itemView) {
            super(itemView);
            cardViewlinha = itemView.findViewById(R.id.cardmovimentacao);
            mTimelineView = itemView.findViewById(R.id.time_marker);
            titulo = itemView.findViewById(R.id.text_timeline_title);
            valor = itemView.findViewById(R.id.text_timeline_valor);
            data = itemView.findViewById(R.id.text_timeline_date);
            status = itemView.findViewById(R.id.editstatusop);
            cardViewlinha.setUseCompatPadding(true);
        }


        private void render(DataSnapshot dataSnapshot, int pos) {
            cardViewlinha.setUseCompatPadding(true);




            Integer statusop = (dataSnapshot.child("statusOp").getValue(Integer.class));
            if (pos == 0) {
                mTimelineView.initLine(1);
                mTimelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker_inactive));
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
            titulo.setText((dataSnapshot.child("titulo").getValue(String.class)));
            valor.setText(String.valueOf(dataSnapshot.child("valor").getValue(Double.class)));
            data.setText((dataSnapshot.child("data").getValue(String.class)));


        }
    }


}
