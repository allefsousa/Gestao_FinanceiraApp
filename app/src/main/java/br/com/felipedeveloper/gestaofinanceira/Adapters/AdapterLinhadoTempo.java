package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by allef on 03/05/2018.
 */

public class AdapterLinhadoTempo extends RecyclerView.Adapter<AdapterLinhadoTempo.ViewHolderLinhadoTempo> {

    Context context;

    @NonNull
    @Override
    public ViewHolderLinhadoTempo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLinhadoTempo holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderLinhadoTempo extends RecyclerView.ViewHolder {
        public ViewHolderLinhadoTempo(View itemView) {
            super(itemView);
        }
    }
}
