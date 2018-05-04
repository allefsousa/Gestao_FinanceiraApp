package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
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

import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.R;

/**
 * Created by allef on 03/05/2018.
 */

public class AdapterLinhadoTempo extends RecyclerView.Adapter<AdapterLinhadoTempo.ViewHolder> {
    private Context context;
    private List<Carteira> carteiraList;
    private Carteira carteira;
    int res = -1;
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimentacoes, parent, false),viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.render(sortedList.get(position),position);
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
    public int percoreSnapshot(DataSnapshot dataSnapshot){

            carteira = dataSnapshot.getValue(Carteira.class);
            res = res +1;

        Log.d("Allef", "percoreSnapshot: " +res);
        return carteiraList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        TextView data;
        TextView titulo;
        TextView valor;

        public ViewHolder(View itemView,int viewt) {
            super(itemView);
            carteiraList = new ArrayList<>();
            carteira = new Carteira();
            mTimelineView = itemView.findViewById(R.id.time_marker);
            titulo = itemView.findViewById(R.id.text_timeline_title);
            valor = itemView.findViewById(R.id.text_timeline_valor);
            data = itemView.findViewById(R.id.text_timeline_date);
        }


        private void render(DataSnapshot dataSnapshot,int pos) {

            int tamanho = percoreSnapshot(dataSnapshot);
            Log.d("Allef", "render: "+(sortedList.size()-1));



            Integer statusop = (dataSnapshot.child("statusOp").getValue(Integer.class));
            if (pos== 0) {
                mTimelineView.initLine(1);
                mTimelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker_inactive));
            } else {
                if (statusop != null) {
                    switch (statusop) {
                        case 0:
                            mTimelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker));
                            break;
                        case 1:
                            mTimelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker));
                            break;
                    }
                }

            }
            if (pos == (sortedList.size()-1)){
                mTimelineView.setEndLine(context.getResources().getColor(R.color.float_transparent),4);
            }
            titulo.setText((dataSnapshot.child("titulo").getValue(String.class)));
            valor.setText(String.valueOf(dataSnapshot.child("valor").getValue(Double.class)));
            data.setText((dataSnapshot.child("data").getValue(String.class)));


        }
    }


}
