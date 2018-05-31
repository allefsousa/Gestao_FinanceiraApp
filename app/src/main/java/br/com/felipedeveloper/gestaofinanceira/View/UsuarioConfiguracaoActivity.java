package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioConfiguracaoActivity extends BaseActivity {

    @BindView(R.id.checksair)
    CheckedTextView btsair;
    @BindView(R.id.checkrestaurar)
    CheckedTextView btnrestaurar;
    @BindView(R.id.checkNome)
    CheckedTextView checkNome;
    @BindView(R.id.circleimageusuario)
    CircleImageView circleImageView;
    private String idUser;
    DatabaseReference reference;


    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_configuracao);
        ButterKnife.bind(this);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        reference = configFirebase(reference);
        idUser = auth.getCurrentUser().getUid();
        if (!idUser.isEmpty()){
            Picasso.with(UsuarioConfiguracaoActivity.this).load(auth.getCurrentUser().getPhotoUrl()).noFade().into(circleImageView);
        }
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
//                    if (auth.getCurrentUser().getProviderData().) todo verificar providar e deslogar api facebook
                }

            }
        });
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
}
