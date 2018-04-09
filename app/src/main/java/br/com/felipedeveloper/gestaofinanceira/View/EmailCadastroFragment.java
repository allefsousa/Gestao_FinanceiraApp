package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.felipedeveloper.gestaofinanceira.Model.Usuario;
import br.com.felipedeveloper.gestaofinanceira.R;

public class EmailCadastroFragment extends Fragment {

    private Button btaddusuario;
    private EditText edtConfirmasenha;
    private EditText edtemail;
    private EditText edtnome;
    private EditText edtsenha;
    private View rootView;
    private FirebaseAuth firebaseAuth;
    private Usuario user;
    private DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.email_cadastro_activity, container, false);
        btaddusuario = rootView.findViewById(R.id.btnsalvar);
        edtConfirmasenha = rootView.findViewById(R.id.editconfirmesenha);
        edtnome = rootView.findViewById(R.id.editnome);
        edtsenha = rootView.findViewById(R.id.editsenha);
        edtemail = rootView.findViewById(R.id.editemail);
        user = new Usuario();
        inicializaFirebase();
        firebaseAuth = FirebaseAuth.getInstance(); // pegando a instancia de login do firebase


        btaddusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtConfirmasenha.clearFocus();
                user.setUsuarioNome(edtnome.getText().toString());
                user.setUsuarioEmail(edtemail.getText().toString());
                user.setUsuarioSenha(edtsenha.getText().toString());
                user.setUsuarioconfirmaSenha(edtConfirmasenha.getText().toString());
                if (user.Verificasenha()) {
                    criarContaFirebase(user);
                } else {
                    Toast.makeText(rootView.getContext(), "Senhas nao conferem", Toast.LENGTH_SHORT).show();
                    // TODO: 02/04/2018  exibir erro no edittext
                }


            }
        });

        return rootView;


    }

    private void inicializaFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("usuario");
    }

    private void criarContaFirebase(Usuario user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getUsuarioEmail(), user.getUsuarioSenha()).addOnCompleteListener((Activity) rootView.getContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    limparcampos();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    salvarUsuarioBD(firebaseUser);

                    Toast.makeText(rootView.getContext(), "Conta criada com sucesso.", Toast.LENGTH_SHORT).show();
                }
                if (!task.isSuccessful()) {

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

    private void salvarUsuarioBD(FirebaseUser firebaseUser) {
        user.setUsuarioEmail(firebaseUser.getEmail());
        user.setUsuarioNome(user.getUsuarioNome());
        user.setIdUsuario(firebaseUser.getUid());
        reference.child(user.getIdUsuario()).setValue(user);
    }

    private void limparcampos() {
        edtConfirmasenha.setText("");
        edtemail.setText("");
        edtnome.setText("");
        edtsenha.setText("");
    }

}
