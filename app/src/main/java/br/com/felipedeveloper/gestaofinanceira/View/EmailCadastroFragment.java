package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EmailCadastroFragment extends Fragment {

    Button btaddusuario;
    EditText edtConfirmasenha;
    EditText edtemail;
    EditText edtnome;
    EditText edtsenha;
    View rootView;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.email_cadastro_activity,container,false);
        btaddusuario = rootView.findViewById(R.id.btnsalvar);
        edtConfirmasenha = rootView.findViewById(R.id.editconfirmesenha);
        edtnome = rootView.findViewById(R.id.editnome);
        edtsenha = rootView.findViewById(R.id.editsenha);
        edtemail = rootView.findViewById(R.id.editemail);

        firebaseAuth = FirebaseAuth.getInstance(); // pegando a instancia de login do firebase


        btaddusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email,senha,confirmasenha;
                email = edtemail.getText().toString();
                senha = edtsenha.getText().toString();
                confirmasenha = edtConfirmasenha.getText().toString();
                if(!email.isEmpty() && !senha.isEmpty()){
                    if(senha.equals(confirmasenha)){
                        criarContaFirebase(email,senha);
                    }else {
                        Toast.makeText(rootView.getContext(), "Senhas nao conferem", Toast.LENGTH_SHORT).show();
                        // TODO: 02/04/2018  exibir erro no edittext
                    }
                }



            }
        });

        return rootView;



    }

    private void criarContaFirebase(final String email, String senha) {
        firebaseAuth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener((Activity) rootView.getContext(),new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    limparcampos();
                    Toast.makeText(rootView.getContext(), "Conta criada com sucesso.", Toast.LENGTH_SHORT).show();
                }
                if(!task.isSuccessful()) {

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
//                        icadSenha.setError("Senha Fraca, Utilize Numeros e Letras.");
                        edtsenha.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException c) {
//                        icadEmail.setError("Email invalido !!");
                        edtemail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException d) {
//                        icadEmail.setError("Usuario ja existe!!");
                        edtemail.requestFocus();
                    } catch (Exception e) {

                    }
                }

            }
        });
    }

    private void limparcampos() {
        edtConfirmasenha.setText("");
        edtemail.setText("");
        edtnome.setText("");
        edtsenha.setText("");
    }

}
