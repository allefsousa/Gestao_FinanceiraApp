package br.com.felipedeveloper.gestaofinanceira.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.felipedeveloper.gestaofinanceira.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class lancamentosGrupoFragment extends Fragment {


    public lancamentosGrupoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lancamentos_grupo, container, false);
    }

}
