package br.com.felipedeveloper.gestaofinanceira.View;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import br.com.felipedeveloper.gestaofinanceira.R;

public class EmailLoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.email_login_activity,container,false);
        return rootView;
    }

}
