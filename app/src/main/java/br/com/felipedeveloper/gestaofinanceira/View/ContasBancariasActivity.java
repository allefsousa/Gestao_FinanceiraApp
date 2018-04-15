package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContasBancariasActivity extends AppCompatActivity {
    @BindView(R.id.btnnovobanco)
    Button btnaddbanco;
    private DatabaseReference myreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_bancarias);
        ButterKnife.bind(this);
        IniciaFirebaseDatabase();
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
        View alview =getLayoutInflater().inflate(R.layout.activity_addbanco,null);

        EditText titulobanco = alview.findViewById(R.id.titulobanco);
        EditText agenciaBanco = alview.findViewById(R.id.agencia);
        EditText  saldobanco = alview.findViewById(R.id.saldo);
        Button salvar = alview.findViewById(R.id.btnsalvarbanco);

        builder.setView(alview);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setDimAmount(1.0f); // cor de fundo dialog
        alertDialog.show();



    }

    private void IniciaFirebaseDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myreference = firebaseDatabase.getReference().child("financeiro").child("carteira");
    }

}
