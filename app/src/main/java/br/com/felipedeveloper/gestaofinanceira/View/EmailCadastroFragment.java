package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.felipedeveloper.gestaofinanceira.Model.Usuario;
import br.com.felipedeveloper.gestaofinanceira.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmailCadastroFragment extends Fragment {

    //region Itens da View
    private FloatingActionButton btaddusuario;
    private EditText edtConfirmasenha;
    private EditText edtemail;
    private EditText edtnome;
    private EditText edtsenha;
    private TextInputLayout inputLayoutNome;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutSenha;
    private TextInputLayout inputLayoutConfirma;

    //endregion

    private View rootView;
    private FirebaseAuth firebaseAuth;
    private Usuario user;
    private DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.email_cadastro_activity, container, false); // inflando a view email_cadastro

        // cast dos botoes da view para poder ser utilizado
        btaddusuario = rootView.findViewById(R.id.btnsalvar);
        edtConfirmasenha = rootView.findViewById(R.id.editconfirmesenha);
        edtnome = rootView.findViewById(R.id.editnome);
        edtsenha = rootView.findViewById(R.id.editsenha);
        edtemail = rootView.findViewById(R.id.editemail);
        inputLayoutNome = rootView.findViewById(R.id.textInputLayout);
        inputLayoutEmail = rootView.findViewById(R.id.textInputLayout2);
        inputLayoutSenha = rootView.findViewById(R.id.textInputLayout3);
        inputLayoutConfirma = rootView.findViewById(R.id.textInputLayout4);

        // instanciando novos objetos
        user = new Usuario();
        inicializaFirebase();
        firebaseAuth = FirebaseAuth.getInstance(); // pegando a instancia de login do firebase




        //region Tratamento Labels erro view
        edtnome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutNome.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtConfirmasenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutSenha.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutEmail.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtConfirmasenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutConfirma.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion


        /**
         * lisner do botao de adicionar um novo usuario
         */
        btaddusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtConfirmasenha.clearFocus();
                boolean validaOk = ValidarCampos();

                user.setUsuarioNome(edtnome.getText().toString());
                user.setUsuarioEmail(edtemail.getText().toString());
                user.setUsuarioSenha(edtsenha.getText().toString());
                user.setUsuarioconfirmaSenha(edtConfirmasenha.getText().toString());
                if (!user.getUsuarioEmail().isEmpty()) {
                    if (user.Verificasenha()) {
                        criarContaFirebase(user);
                    }else {
                        ExibirMensagem(rootView.getContext(), SweetAlertDialog.ERROR_TYPE, "Senhas nao conferem !");
                    }
                }

            }
        });

        return rootView;


    }

    private boolean ValidarCampos() {
        Boolean retorno = false;
        if (edtnome.getText().toString().isEmpty()){

        }
        if (edtsenha.getText().toString().isEmpty()){

        }
        if (edtemail.getText().toString().isEmpty()){

        }
        if (edtConfirmasenha.getText().toString().isEmpty()){

        }
        if (edtnome.getText().toString().isEmpty() && edtsenha.getText().toString().isEmpty() && edtemail.getText().toString().isEmpty() && edtConfirmasenha.getText().toString().isEmpty()){
            retorno = true;
        }
        return  retorno;
    }

    private void inicializaFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("usuario");
    }

    private void criarContaFirebase(final Usuario user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getUsuarioEmail(), user.getUsuarioSenha()).addOnCompleteListener((Activity) rootView.getContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    limparcampos();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    // atualizando o usuario cadastrad para exibir o nome no app
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(user.getUsuarioNome())
                            .build();

                    if (firebaseUser != null){
                        firebaseUser.updateProfile(profileUpdates);
                    }
                    salvarUsuarioBD(firebaseUser);
                    ExibirMensagem(rootView.getContext(), SweetAlertDialog.SUCCESS_TYPE, "Conta criada com sucesso !");


                }
                if (!task.isSuccessful()) {

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        ExibirMensagem(rootView.getContext(), SweetAlertDialog.ERROR_TYPE, "Senha Fraca, Utilize Numeros e Letras");
                        edtsenha.setText("");
                        edtConfirmasenha.setText("");
                        edtsenha.requestFocus();
//                        icadSenha.setError("Senha Fraca, Utilize Numeros e Letras.");
                        edtsenha.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException c) {
                        ExibirMensagem(rootView.getContext(), SweetAlertDialog.ERROR_TYPE, "Email invalido !!");
                        edtemail.setText("");
                        edtemail.requestFocus();
//                       icadEmail.setError("Email invalido !!");
                    } catch (FirebaseAuthUserCollisionException d) {
//                        icadEmail.setError("Usuario ja existe!!");
                        ExibirMensagem(rootView.getContext(), SweetAlertDialog.ERROR_TYPE, "Usuario ja existe !!");
                        limparcampos();
                    } catch (Exception e) {
                        ExibirMensagem(rootView.getContext(), SweetAlertDialog.ERROR_TYPE, "Verifique sua conexao com a Internet");
                        limparcampos();
                    }
                }

            }
        });
    }

    private void ExibirMensagem(Context context, int successType, String s) {
        new SweetAlertDialog(context, successType)
                .setTitleText(getResources().getString(R.string.app_name))
                .setContentText(s)
                .show();
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
