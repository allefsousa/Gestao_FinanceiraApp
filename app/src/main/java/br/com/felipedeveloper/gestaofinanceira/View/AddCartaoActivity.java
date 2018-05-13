package br.com.felipedeveloper.gestaofinanceira.View;

import android.support.design.widget.FloatingActionButton;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCartaoActivity extends AppCompatActivity {

    @BindView(R.id.btnsalvarcartao)
    FloatingActionButton btnsalvarCartao;
    @BindView(R.id.editnomecartao)
    EditText edtnomeCartao;
    @BindView(R.id.editsaldocartao)
    EditText edtsaldoCartao;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String idUser;
    private DatabaseReference myreference;
    private Cartao cartao;
    NumberFormat df;


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
        df = new DecimalFormat("#.##");

        btnsalvarCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartao.setIdcartao(UUID.randomUUID().toString());
                cartao.setTituloCartao(edtnomeCartao.getText().toString());
                if (!edtsaldoCartao.getText().toString().isEmpty()) {
                    cartao.setSaldoCartao(Double.parseDouble(edtsaldoCartao.getText().toString()));
                }


                if (cartao.getSaldoCartao() != null) {
                    if (!cartao.getTituloCartao().isEmpty()) {
                        myreference.child(cartao.getIdcartao()).setValue(cartao);
                        Toast.makeText(AddCartaoActivity.this, "Cartão Adicionado!" + df.format(cartao.getSaldoCartao()), Toast.LENGTH_LONG).show();
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
        myreference = firebaseDatabase.getReference().child("financeiro").child(idUser).child("cartao");
    }
    private void clear(){
        edtsaldoCartao.clearFocus();
        edtnomeCartao.setText("");
        edtsaldoCartao.setText("");
    }
}
