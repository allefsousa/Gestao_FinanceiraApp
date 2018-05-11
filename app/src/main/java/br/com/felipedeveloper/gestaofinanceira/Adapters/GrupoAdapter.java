package br.com.felipedeveloper.gestaofinanceira.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by allef on 09/05/2018.
 */

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.ViewHolderGrupo> {
    private Context context;


    @NonNull
    @Override
    public GrupoAdapter.ViewHolderGrupo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoAdapter.ViewHolderGrupo holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderGrupo extends RecyclerView.ViewHolder {
        public ViewHolderGrupo(View itemView) {
            super(itemView);
        }
    }
}
