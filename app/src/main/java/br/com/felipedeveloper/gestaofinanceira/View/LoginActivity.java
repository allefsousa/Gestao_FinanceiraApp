package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daimajia.slider.library.SliderLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    // variaveis globais
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myref;

    @BindView(R.id.btnfacebook)
    Button btnfacebookLogin;
    @BindView(R.id.btngoogle)
    Button btngooglelogin;
    @BindView(R.id.btnemail)
    Button btnemaillogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        inicializaFirebase(); // chamada de metodo que inicia firebase
        btnfacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,OpcoesFinanceiraActivity.class));

            }
        });


    }

    /**
     * metodo responsavel por inicializar firebase
     * neste caso o banco de dados realtime database
     */
    public void inicializaFirebase(){
        database = FirebaseDatabase.getInstance();
        myref = database.getReference();
    }
}
