package br.com.felipedeveloper.gestaofinanceira.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCartaoActivity extends AppCompatActivity {

    @BindView(R.id.btnsalvarcartao)
    Button btnsalvarCartao;
    @BindView(R.id.editnomecartao)
    EditText edtnomeCartao;
    @BindView(R.id.editsaldocartao)
    EditText edtsaldoCartao;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String idUser;
    private DatabaseReference myreference;
    private Cartao cartao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cartao);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        IniciaFirebase();
        cartao = new Cartao();

        btnsalvarCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartao.setIdcartao(UUID.randomUUID().toString());
                cartao.setTituloCartao(edtnomeCartao.getText().toString());
                if (!edtsaldoCartao.getText().toString().isEmpty()) {
                    cartao.setSaldoCartao((edtsaldoCartao.getText().toString()));
                }


                if (!cartao.getSaldoCartao().isEmpty()) {
                    if (!cartao.getTituloCartao().isEmpty()) {
                        myreference.child(cartao.getIdcartao()).setValue(cartao);
                        Toast.makeText(AddCartaoActivity.this, "Cartão Adicionado!", Toast.LENGTH_LONG).show();
                        clear();
                    } else {

                    }
                } else {

                }
            }
        });

    }
    private void IniciaFirebase() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            idUser = firebaseUser.getUid();
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myreference = firebaseDatabase.getReference().child("financeiro").child("cartao").child(idUser);
    }
    private void clear(){
        edtsaldoCartao.clearFocus();
        edtnomeCartao.setText("");
        edtsaldoCartao.setText("");
    }
}
