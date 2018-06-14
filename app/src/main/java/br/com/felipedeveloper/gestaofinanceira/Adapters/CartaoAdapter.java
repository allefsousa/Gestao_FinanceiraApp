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

import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.R;
import br.com.felipedeveloper.gestaofinanceira.View.TransacoesActivity;

/**
 * Adapter responsavel por exibir a lista de cartoes
 */
public class CartaoAdapter extends RecyclerView.Adapter<CartaoAdapter.ViewHolderCartao> {

    private List<Cartao> cartaoArray;
    private Context context;
    private NumberFormat df;

    public CartaoAdapter(List<Cartao> cartao, Context context) {
        this.cartaoArray = cartao;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderCartao onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_adapter_cardview, parent, false);


        return new ViewHolderCartao(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCartao holder, int position) {
        if (!cartaoArray.isEmpty() && cartaoArray.size() > 0) {

            holder.titulocartao.setText(cartaoArray.get(position).getTituloCartao());
            holder.saldocartao.setText(String.valueOf("R$ "+ df.format(cartaoArray.get(position).getSaldoCartao())));
            render(holder,position);
        }


    }
    private void render(final ViewHolderCartao holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,TransacoesActivity.class);
                intent.putExtra("nomeop",cartaoArray.get(position).getIdcartao());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartaoArray.size();
    }

    public class ViewHolderCartao extends RecyclerView.ViewHolder {
       private TextView titulocartao;
        private TextView saldocartao;
        private CardView cardView;

        public ViewHolderCartao(View itemView) {
            super(itemView);
            titulocartao = itemView.findViewById(R.id.tituloitem);
            saldocartao = itemView.findViewById(R.id.saldoitem);
            df = new DecimalFormat("#,###,##");
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
