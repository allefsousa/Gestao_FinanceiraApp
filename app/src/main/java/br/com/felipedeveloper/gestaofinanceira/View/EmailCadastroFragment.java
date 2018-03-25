package br.com.felipedeveloper.gestaofinanceira.View;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


import br.com.felipedeveloper.gestaofinanceira.R;

public class EmailCadastroFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.email_cadastro_activity,container,false);
        return rootView;
    }

}
