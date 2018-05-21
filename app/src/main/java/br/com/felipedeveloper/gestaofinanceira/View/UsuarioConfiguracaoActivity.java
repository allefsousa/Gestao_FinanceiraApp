package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioConfiguracaoActivity extends AppCompatActivity {

    @BindView(R.id.btnsair)
    Button btsair;
    @BindView(R.id.circleimageusuario)
    CircleImageView circleImageView;
    private String idUser;


    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_configuracao);
        ButterKnife.bind(this);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        if (!idUser.isEmpty()){
            Picasso.with(UsuarioConfiguracaoActivity.this).load(auth.getCurrentUser().getPhotoUrl()).into(circleImageView);
        }
        btsair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!idUser.isEmpty()){
                    auth.signOut();
                    Intent intent = new Intent(UsuarioConfiguracaoActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
//                    if (auth.getCurrentUser().getProviderData().) todo verificar providar e deslogar api facebook
                }

            }
        });



    }
}
