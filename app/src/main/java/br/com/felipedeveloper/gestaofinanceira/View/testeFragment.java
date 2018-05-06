package br.com.felipedeveloper.gestaofinanceira.View;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.felipedeveloper.gestaofinanceira.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class testeFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    EditText editText;
    ViewGroup cont;
    public testeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        editText = new EditText(container.getContext());
        editText.setHint("Email do usuario");
        editText.setMaxLines(1);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        cont = container;
        mensagem();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teste, container, false);
    }

    private void mensagem() {
        new SweetAlertDialog(cont.getContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Criar Grupo")
                .setConfirmText("Adicionar")
                .setCustomView(editText)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        boolean a = ((InputMethodManager) cont.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                                editText.getWindowToken(), 0);
                        if (a) {
                            sweetAlertDialog.setTitleText("Deleted!")
                                    .setContentText("Your imaginary file has been deleted!")
                                    .setConfirmText(editText.getText().toString())
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }


                    }
                })
                .show();


    }

}
