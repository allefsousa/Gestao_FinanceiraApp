package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.R;
import br.com.felipedeveloper.gestaofinanceira.View.TransacoesActivity;


public class CarteiraAdapter extends RecyclerView.Adapter<CarteiraAdapter.ViewHolderAgencia> {

    private List<Carteira> carteiraList;
    private Activity context;
    private int clickFlag = 0;
    private NumberFormat df;

    public CarteiraAdapter(List<Carteira> carteiras, Activity activity) {
        this.carteiraList = carteiras;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolderAgencia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_adapter_cardview, parent, false);


        return new ViewHolderAgencia(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderAgencia holder, final int position) {
        if (!carteiraList.isEmpty() && carteiraList.size() > 0) {

            holder.textTituloCarteira.setText(carteiraList.get(position).getTituloCarteira());
            holder.textSaldoCarteira.setText(String.valueOf(df.format(carteiraList.get(position).getSaldoCarteira()) + " R$"));
//            render(holder,position);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,TransacoesActivity.class);
                    intent.putExtra("nomeop",carteiraList.get(position).getIdCarteira());
                    context.startActivity(intent);
                }
            });
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context, "Removido", Toast.LENGTH_SHORT).show();
                    carteiraList.remove(position);
                    notifyDataSetChanged();
                    return false;
                }
            });
        }
    }
    private void render(final ViewHolderAgencia holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,TransacoesActivity.class);
                intent.putExtra("nomeop",carteiraList.get(position).getIdCarteira());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return carteiraList.size();
    }

    public class ViewHolderAgencia extends RecyclerView.ViewHolder {
        private TextView textTituloCarteira;
        private TextView textSaldoCarteira;
        private CardView cardView;

        public ViewHolderAgencia(View itemView) {
            super(itemView);
            df = new DecimalFormat("#,###,##");
            textTituloCarteira = itemView.findViewById(R.id.tituloitem);
            textSaldoCarteira = itemView.findViewById(R.id.saldoitem);
            cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnCreateContextMenuListener(context);

        }




        // FIXME: 19/04/2018  Excluir item ;
        public void click(View itemView) {

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {


                    return false;
                }
            });
        }


    }
}
