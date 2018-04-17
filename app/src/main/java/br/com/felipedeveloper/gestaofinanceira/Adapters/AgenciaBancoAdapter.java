package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;

/**
 * Created by allef on 16/04/2018.
 */

public class AgenciaBancoAdapter extends RecyclerView.Adapter<AgenciaBancoAdapter.ViewHolderAgencia> {

    private List<ContasBancarias> contasBancariasArray;
    private Context context;

    public AgenciaBancoAdapter(List<ContasBancarias> contasBancarias, Context context) {
        this.contasBancariasArray = contasBancarias;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderAgencia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_conta_bancaria,parent,false);


        return new ViewHolderAgencia(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAgencia holder, int position) {
        if (!contasBancariasArray.isEmpty() && contasBancariasArray.size()>0){

            holder.tituloConta.setText(contasBancariasArray.get(position).getTituloContabancaria());
            holder.saldoConta.setText(String.valueOf(contasBancariasArray.get(position).getSaldoContabancaria()+" Reais"));
        }


    }

    @Override
    public int getItemCount() {
        return contasBancariasArray.size();
    }

    public class ViewHolderAgencia extends RecyclerView.ViewHolder {
        TextView tituloConta;
        TextView saldoConta;

        public ViewHolderAgencia(View itemView) {
            super(itemView);
            tituloConta = itemView.findViewById(R.id.titulocontabancarias);
            saldoConta = itemView.findViewById(R.id.cardsaldocontabancaria);
        }
    }
}
