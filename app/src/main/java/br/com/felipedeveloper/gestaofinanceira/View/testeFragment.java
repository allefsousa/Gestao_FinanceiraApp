package br.com.felipedeveloper.gestaofinanceira.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import br.com.felipedeveloper.gestaofinanceira.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class testeFragment extends Fragment {


    public testeFragment() {
        // Required empty public constructor
    }
    EditText editText;
    ViewGroup cont;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        editText = new EditText(container.getContext());
        cont = container;
        mensagem();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teste, container, false);
    }
    private void mensagem() {
        new SweetAlertDialog(cont.getContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Custom view")
                .setConfirmText("Ok")
                .setCustomView(editText)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.setTitleText("Deleted!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmText(editText.getText().toString())
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    }
                })
                .show();




    }

}
