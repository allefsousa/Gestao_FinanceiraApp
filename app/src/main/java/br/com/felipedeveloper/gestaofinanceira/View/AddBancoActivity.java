package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by allef on 16/04/2018.
 */

public class AddBancoActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String idUser;
    @BindView(R.id.titulobanco)
    EditText edtTituloBanco;
    @BindView(R.id.saldo)
    EditText edtSaldoBanco;
    @BindView(R.id.btnsalvarbanco)
    Button btnAdicionarBanco;
    private DatabaseReference myreference;
    private ContasBancarias contasBancarias;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbanco);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        IniciaFirebase();
        contasBancarias = new ContasBancarias();

        btnAdicionarBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contasBancarias.setIdContaBanco(UUID.randomUUID().toString());
                contasBancarias.setTituloContabanco(edtTituloBanco.getText().toString());
                if (!edtSaldoBanco.getText().toString().isEmpty()) {
                    contasBancarias.setSaldoContabancaria(Double.parseDouble(edtSaldoBanco.getText().toString()));
                }


                if (contasBancarias.getSaldoContabancaria()!= null) {
                    if (!contasBancarias.getTituloContabanco().isEmpty()) {
                        myreference.child(contasBancarias.getIdContaBanco()).setValue(contasBancarias);
                        Toast.makeText(AddBancoActivity.this, "Conta Adicionada com sucesso !", Toast.LENGTH_LONG).show();
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
        myreference = firebaseDatabase.getReference().child("financeiro").child("banco").child(idUser);
    }
    private void clear(){
        edtSaldoBanco.clearFocus();
        edtTituloBanco.setText("");
        edtSaldoBanco.setText("");
    }
}
