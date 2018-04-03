package br.com.felipedeveloper.gestaofinanceira.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.felipedeveloper.gestaofinanceira.Model.usuario;
import br.com.felipedeveloper.gestaofinanceira.R;

public class EmailLoginFragment extends Fragment {
    Button btnlogar;
    EditText eemail;
    EditText esenha;
    TextView tRecuperarsenha;
    View rootView;
    FirebaseAuth auth;
    usuario usuaario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.email_login_activity, container, false);
        btnlogar = rootView.findViewById(R.id.btnentrar);
        eemail = rootView.findViewById(R.id.editemaillogin);
        esenha = rootView.findViewById(R.id.editsenhalogin);
        auth = FirebaseAuth.getInstance();
        btnlogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usuaario.getUsuarioEmail().isEmpty() && !usuaario.getUsuarioSenha().isEmpty()){
                    loginEmailFirebase(usuaario.getUsuarioEmail(), usuaario.getUsuarioSenha());
                }else {

                }

            }

        });


        return rootView;


    }

    private void loginEmailFirebase(String usuarioEmail, String usuarioSenha) {
            auth.signInWithEmailAndPassword(usuarioEmail, usuarioSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        logadoComSucesso();
                    }
                }
            });


    }

    private void logadoComSucesso() {
        Intent intent = new Intent(rootView.getContext(), OpcoesFinanceiraActivity.class);
        startActivity(intent);
    }

}
