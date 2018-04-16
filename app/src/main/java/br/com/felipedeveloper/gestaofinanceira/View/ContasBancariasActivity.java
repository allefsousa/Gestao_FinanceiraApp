package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class ContasBancariasActivity extends AppCompatActivity {
    @BindView(R.id.btnnovobanco)
    Button btnaddbanco;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String idUser;
    private DatabaseReference myreference;
    AlertDialog alertDialog;
    EditText titulobanco;
    EditText agenciaBanco;
    EditText saldobanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_bancarias);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        IniciaFirebase();
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnaddbanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AdicionarBancoDialog();

            }
        });

    }

    /**
     * Metodo responsavel por criar o  dialog para o usuario adicionar os dados do Banco/Agencia
     */
    private void AdicionarBancoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContasBancariasActivity.this);
        View alview = getLayoutInflater().inflate(R.layout.activity_addbanco, null);
        final ContasBancarias contasBancarias = new ContasBancarias();

         titulobanco = alview.findViewById(R.id.titulobanco);
         agenciaBanco = alview.findViewById(R.id.agencia);
         saldobanco = alview.findViewById(R.id.saldo);
        Button salvar = alview.findViewById(R.id.btnsalvarbanco);
        builder.setView(alview);
         alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setDimAmount(1.0f); // cor de fundo dialog
        alertDialog.show();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contasBancarias.setIdContabancaria(UUID.randomUUID().toString());
                contasBancarias.setAgencia(agenciaBanco.getText().toString());
                contasBancarias.setTituloContabancaria(titulobanco.getText().toString());
                contasBancarias.setSaldoContabancaria(Double.parseDouble(saldobanco.getText().toString()));

                if (!contasBancarias.getAgencia().isEmpty()) {
                    if (!contasBancarias.getSaldoContabancaria().toString().isEmpty()) {
                        if (!contasBancarias.getTituloContabancaria().isEmpty()) {
                            myreference.child(myreference.getKey()).setValue(contasBancarias);
                            Toast.makeText(ContasBancariasActivity.this, "Conta Adicionada com sucesso !", Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
                        } else {

                        }
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
        if(firebaseUser != null ){
            idUser = firebaseUser.getUid();
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myreference = firebaseDatabase.getReference().child("financeiro").child("banco").child(idUser);
    }

}
