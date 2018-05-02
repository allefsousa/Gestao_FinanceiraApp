package br.com.felipedeveloper.gestaofinanceira.View;


import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.felipedeveloper.gestaofinanceira.Model.Usuario;
import br.com.felipedeveloper.gestaofinanceira.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmailLoginFragment extends Fragment {
    private Button btnlogar;
    private EditText eemail;
    private EditText esenha;
    private TextView tRecuperarsenha;
    private View rootView;
    private FirebaseAuth auth;
    private Usuario usuaario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.email_login_activity, container, false);
        btnlogar = rootView.findViewById(R.id.btnentrar);
        eemail = rootView.findViewById(R.id.editemaillogin);
        esenha = rootView.findViewById(R.id.editsenhalogin);
        tRecuperarsenha = rootView.findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        btnlogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuaario = new Usuario();
                usuaario.setUsuarioEmail(eemail.getText().toString());
                usuaario.setUsuarioSenha(esenha.getText().toString());
                if (!usuaario.getUsuarioEmail().isEmpty() && !usuaario.getUsuarioSenha().isEmpty()) {
                    loginEmailFirebase(usuaario.getUsuarioEmail(), usuaario.getUsuarioSenha());
                } else {
                    ExibirMensagem(rootView.getContext(), SweetAlertDialog.ERROR_TYPE, "Usuario ou senha em branco");

                }

            }

        });
        tRecuperarsenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return rootView;


    }

    private void loginEmailFirebase(String usuarioEmail, String usuarioSenha) {
        auth.signInWithEmailAndPassword(usuarioEmail, usuarioSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    ExibirMensagem(rootView.getContext(), SweetAlertDialog.SUCCESS_TYPE, "Login realizado com sucesso. Vamos lá ?");

                } else {
                    try {
                        throw task.getException();
                        // TODO: 29/04/2018 verificar exceptions de login
                    } catch (FirebaseAuthWeakPasswordException e) {

                    } catch (FirebaseAuthInvalidCredentialsException c) {

                    } catch (FirebaseAuthUserCollisionException d) {
                        //
                    } catch (Exception e) {

                    }

                }

            }
        });


    }

    private void logadoComSucesso() {
        Intent intent = new Intent(rootView.getContext(), OpcoesFinanceiraActivity.class);
        startActivity(intent);
    }

    private void ExibirMensagem(Context context, int successType, String s) {
        new SweetAlertDialog(context, successType)
                .setTitleText(getResources().getString(R.string.app_name))
                .setContentText(s)
                .setConfirmButton("Entrar!", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        logadoComSucesso();
                    }
                })
                .show();
    }

}
