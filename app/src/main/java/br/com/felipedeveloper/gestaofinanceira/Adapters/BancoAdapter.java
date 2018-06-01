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
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import br.com.felipedeveloper.gestaofinanceira.View.TransacoesActivity;

/**
 * Adapter responsavel por exibir a lista de bancos
 */
public class BancoAdapter extends RecyclerView.Adapter<BancoAdapter.ViewHolderAgencia> {

    private List<ContasBancarias> contasBancariasArray;
    private Context context;
    private int clickFlag = 0;
    private NumberFormat df;

    public BancoAdapter(List<ContasBancarias> contasBancarias, Context context) {
        this.contasBancariasArray = contasBancarias;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderAgencia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_adapter_cardview, parent, false);


        return new ViewHolderAgencia(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAgencia holder, int position) {
        if (!contasBancariasArray.isEmpty() && contasBancariasArray.size() > 0) {

            holder.textTituloBanco.setText(contasBancariasArray.get(position).getTituloContabanco());
            holder.textSaldoBanco.setText(String.valueOf(df.format(contasBancariasArray.get(position).getSaldoContabancaria()) + " R$"));
            render(holder,position);
        }
    }
    private void render(final ViewHolderAgencia holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,TransacoesActivity.class);
                intent.putExtra("nomeop",contasBancariasArray.get(position).getIdContaBanco());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contasBancariasArray.size();
    }

    public class ViewHolderAgencia extends RecyclerView.ViewHolder {
        private TextView textTituloBanco;
        private TextView textSaldoBanco;
        private CardView cardView;
        public ViewHolderAgencia(View itemView) {
            super(itemView);
            textTituloBanco = itemView.findViewById(R.id.tituloitem);
            textSaldoBanco = itemView.findViewById(R.id.saldoitem);
            cardView = itemView.findViewById(R.id.cardView);
            df = new DecimalFormat("#,###,##");

            click(itemView);
            remove(itemView);
        }

        private void remove(View itemView) {

        }


        // FIXME: 19/04/2018  Excluir item ;
        private void click(View itemView) {

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context, "clikc loco", Toast.LENGTH_SHORT).show();
                    view.setBackgroundColor(context.getResources().getColor(R.color.cardview_shadow_start_color));
                    clickFlag = clickFlag + 1;


                    return false;
                }
            });
        }


    }
}
