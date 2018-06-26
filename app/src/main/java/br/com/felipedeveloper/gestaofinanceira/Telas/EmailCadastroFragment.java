package br.com.felipedeveloper.gestaofinanceira.Telas;

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
import android.widget.EditText;

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

import br.com.felipedeveloper.gestaofinanceira.Modelo.Usuario;
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

    //region Variaveis Globais-
    private View rootView;
    private FirebaseAuth firebaseAuth;
    private Usuario user;
    private DatabaseReference reference;
    //endregion

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
                boolean validaOk = false;
                 validaOk = ValidarCampos();
                if (validaOk) {
                    // atribuindo valor das views para o objeto usuario
                    user.setUsuarioNome(edtnome.getText().toString());
                    user.setUsuarioEmail(edtemail.getText().toString());
                    user.setUsuarioSenha(edtsenha.getText().toString());
                    user.setUsuarioconfirmaSenha(edtConfirmasenha.getText().toString());
                    if (!user.getUsuarioEmail().isEmpty()) {
                        if (user.Verificasenha()) {
                            criarContaFirebase(user); // metodo que cria a conta
                        } else {
                            ExibirMensagem(rootView.getContext(), SweetAlertDialog.ERROR_TYPE, "Senhas nao conferem !");
                        }
                    }
                }

            }
        });

        return rootView;


    }

    /**
     * validando os campos e retorna True se todos tiverem corretos
     *
     * @return
     */
    private boolean ValidarCampos() {
        Boolean retorno = false;
        if (edtnome.getText().toString().isEmpty()) {
            inputLayoutNome.setError("Nome Invalido.");
        }
        if (edtsenha.getText().toString().isEmpty()) {
            inputLayoutSenha.setError("Senha Invalida.");
        }
        if (edtemail.getText().toString().isEmpty()) {
            inputLayoutEmail.setError("Email Invalido.");
        }
        if (edtConfirmasenha.getText().toString().isEmpty()) {
            inputLayoutConfirma.setError("senha Invalida");
        }
        if (edtnome.getText().toString().isEmpty() && edtsenha.getText().toString().isEmpty() && edtemail.getText().toString().isEmpty() && edtConfirmasenha.getText().toString().isEmpty()) {
            retorno = true;
        }
        return retorno;
    }

    /**
     * instancia do firebase para salvar o usuario logado no BD
     */
    private void inicializaFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("usuario");
    }

    /**
     * criando a conta do usuario no firebase
     * porem essa conta só é criada com email e senha assim nao exibindo o nome do usuario em futuras consultas
     * por isso um metodo de update do usuario foi criado para adicionar o nome do usuario
     *
     * @param user
     */
    private void criarContaFirebase(final Usuario user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getUsuarioEmail(), user.getUsuarioSenha()).addOnCompleteListener((Activity) rootView.getContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    limparcampos();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // pegando a instancia do usuario logado apos cadastro

                    // atualizando o usuario cadastrad para exibir o nome no app
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(user.getUsuarioNome())
                            .build();
                    if (firebaseUser != null) {
                        firebaseUser.updateProfile(profileUpdates);// atualizando perfil
                    }
                    salvarUsuarioBD(firebaseUser); // salvando usuario no BD
                    ExibirMensagem(rootView.getContext(), SweetAlertDialog.SUCCESS_TYPE, "Conta criada com sucesso !");


                }
                /**
                 * caso nao seja sucesso criar a conta
                 * abaixo segui o tratamento de erro
                 */
                if (!task.isSuccessful()) {

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        ExibirMensagem(rootView.getContext(), SweetAlertDialog.ERROR_TYPE, "Senha Fraca, Utilize Numeros e Letras");
                        edtsenha.setText("");
                        edtConfirmasenha.setText("");
                        edtsenha.requestFocus();
                        inputLayoutSenha.setError("Senha Fraca, Utilize Numeros e Letras");
                        inputLayoutConfirma.setError("Senha Fraca, Utilize Numeros e Letras");
                        edtsenha.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException c) {
                        edtemail.setText("");
                        edtemail.requestFocus();
                        inputLayoutEmail.setError("Email Invalido");
                    } catch (FirebaseAuthUserCollisionException d) {
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

    /**
     * Exibir mensagem ao usuario
     * @param context
     * @param successType
     * @param s
     */
    private void ExibirMensagem(Context context, int successType, String s) {
        new SweetAlertDialog(context, successType)
                .setTitleText(getResources().getString(R.string.app_name))
                .setContentText(s)
                .show();
    }

    /**
     * responsavel por salvar o usuario no BD do firebase
     * @param firebaseUser
     */
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
