package br.com.felipedeveloper.gestaofinanceira.Telas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioConfiguracaoActivity extends BaseActivity {

    //region Itens da View
    @BindView(R.id.checksair)
    CheckedTextView btsair;
    @BindView(R.id.checkrestaurar)
    CheckedTextView btnrestaurar;
    @BindView(R.id.checkNome)
    CheckedTextView checkNome;
    @BindView(R.id.circleimageusuario)
    CircleImageView circleImageView;
    //endregion

    //region Variaveis Globais
    private String idUser;
    DatabaseReference reference;
    private FirebaseAuth auth;
    //endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_configuracao);
        ButterKnife.bind(this);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // instancias do firebase
        auth = FirebaseAuth.getInstance();
        reference = configFirebase(reference);
        idUser = auth.getCurrentUser().getUid();

        // baixando a imagem do usuario logado caso tenha com base na sua url que o firebase obtem
        if (!idUser.isEmpty()){
            Picasso.with(UsuarioConfiguracaoActivity.this).load(auth.getCurrentUser().getPhotoUrl()).noFade().into(circleImageView);
        }
        // atribuindo o nome do usuario logado
        checkNome.setText("Nome: "+auth.getCurrentUser().getDisplayName()+"\n\nE-Mail: "+auth.getCurrentUser().getEmail());
        btsair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!idUser.isEmpty()){
                    auth.signOut();
                    Intent intent = new Intent(UsuarioConfiguracaoActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }
        });
        // botão de restaurar e apagar os dados do app
        btnrestaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(UsuarioConfiguracaoActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Team Money")
                        .setContentText("Todos os seus dados seram excluidos, Gostaria de continuar?")
                        .setCancelText("Cancelar")
                        .setConfirmText("Continuar")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.showCancelButton(false);
                                RestaurarDadosApp();
                                sweetAlertDialog.setTitle("Team Money");
                                sweetAlertDialog.setConfirmText("OK");
                                sweetAlertDialog.setContentText("Conta Restaurada !");
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                });
                                sweetAlertDialog.show();
                            }
                        })
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();

                            }
                        })
                        .show();

            }
        });






    }

    private void RestaurarDadosApp() {
        reference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

            }
        });
    }


    public void ExibirMensagem(Context context, int successType, String s) {
    }

    // metodo do botão fisico de voltar do celular
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UsuarioConfiguracaoActivity.this,MenuActivity.class));
        finish();
    }
}
