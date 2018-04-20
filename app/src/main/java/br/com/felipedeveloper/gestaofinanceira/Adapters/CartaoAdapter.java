package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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

/**
 * Created by allef on 16/04/2018.
 */

public class CartaoAdapter extends RecyclerView.Adapter<CartaoAdapter.ViewHolderAgencia> {

    private List<Cartao> cartaoArray;
    private Context context;
    NumberFormat df;

    public CartaoAdapter(List<Cartao> cartao, Context context) {
        this.cartaoArray = cartao;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderAgencia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_cartao,parent,false);


        return new ViewHolderAgencia(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAgencia holder, int position) {
        if (!cartaoArray.isEmpty() && cartaoArray.size()>0){

            holder.titulocartao.setText(cartaoArray.get(position).getTituloCartao());
            holder.saldocartao.setText(String.valueOf(df.format(cartaoArray.get(position).getSaldoCartao())+" Reais"));
        }


    }

    @Override
    public int getItemCount() {
        return cartaoArray.size();
    }

    public class ViewHolderAgencia extends RecyclerView.ViewHolder {
        TextView titulocartao;
        TextView saldocartao;

        public ViewHolderAgencia(View itemView) {
            super(itemView);
            titulocartao = itemView.findViewById(R.id.itemcartaoTitulo);
            saldocartao = itemView.findViewById(R.id.itemcartaosaldo);
            df = new DecimalFormat("#0.00");
        }
    }
}
