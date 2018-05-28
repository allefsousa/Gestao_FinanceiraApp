package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddBancoActivity extends BaseActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String idUser;
    @BindView(R.id.textInputLayout8)
    TextInputLayout inputLayoutTitulo;
    @BindView(R.id.textInputLayout9)
    TextInputLayout inputLayoutSaldo;
    @BindView(R.id.titulobanco)
    TextInputEditText edtTituloBanco;
    @BindView(R.id.saldo)
    TextInputEditText edtSaldoBanco;
    @BindView(R.id.btnsalvarbanco)
    FloatingActionButton btnAdicionarBanco;
    private DatabaseReference myreference;
    private ContasBancarias contasBancarias;
    private Cartao cartao;
    private Carteira carteira;
    private String opcaofinanceira;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbanco);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        opcaofinanceira = extras.getString("opcao");
        switch (opcaofinanceira){
            case "banco":
                getSupportActionBar().setTitle("Adicionar Banco");
                inputLayoutTitulo.setHint("Titulo Banco");
                inputLayoutSaldo.setHint("Saldo Banco");
                break;
            case "cartao":
                getSupportActionBar().setTitle("Adicionar Cartão");
                inputLayoutTitulo.setHint("Titulo Cartão");
                inputLayoutSaldo.setHint("Saldo Cartão");
                break;
            case "carteira":
                getSupportActionBar().setTitle("Adicionar Carteira");
                inputLayoutTitulo.setHint("Titulo Carteira");
                inputLayoutSaldo.setHint("Saldo Carteira");
                break;
        }


        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        IniciaFirebase();
        // TODO: 28/05/2018 validar urgente contiuar a mudança e reduçaõ de activites
        
        contasBancarias = new ContasBancarias();
        cartao = new Cartao();
        carteira  = new Carteira();

                btnAdicionarBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opcaofinanceira.equals("cartao")){

                }
                if (opcaofinanceira.equals("carteira")){

                }
                if (opcaofinanceira.equals("banco")){
                    contasBancarias.setIdContaBanco(UUID.randomUUID().toString());
                    contasBancarias.setTituloContabanco(edtTituloBanco.getText().toString());
                    if (!edtSaldoBanco.getText().toString().isEmpty()) {
                        contasBancarias.setSaldoContabancaria(Double.parseDouble(edtSaldoBanco.getText().toString()));
                    }
                }




                if (contasBancarias.getSaldoContabancaria()!= null) {
                    if (!contasBancarias.getTituloContabanco().isEmpty()) {
                        if (opcaofinanceira.equals("banco")){
                            myreference.child(contasBancarias.getIdContaBanco()).setValue(contasBancarias);
                            ExibirMensagem(AddBancoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Conta Adicionada com sucesso !");
                            clear();
                        }
                        if (opcaofinanceira.equals("carteira")){
                            myreference.child(contasBancarias.getIdContaBanco()).setValue(contasBancarias);
                            ExibirMensagem(AddBancoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Conta Adicionada com sucesso !");
                            clear();
                        }
                        if (opcaofinanceira.equals("cartao")){
                            myreference.child(contasBancarias.getIdContaBanco()).setValue(contasBancarias);
                            ExibirMensagem(AddBancoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Conta Adicionada com sucesso !");
                            clear();
                        }
                        myreference.child(contasBancarias.getIdContaBanco()).setValue(contasBancarias);
                        ExibirMensagem(AddBancoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Conta Adicionada com sucesso !");
                        clear();
                    } else {
                        // TODO: 20/05/2018 validar 
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
        myreference = firebaseDatabase.getReference().child("financeiro").child(idUser).child("banco");
    }
    private void clear(){
        edtSaldoBanco.clearFocus();
        edtTituloBanco.setText("");
        edtSaldoBanco.setText("");
    }
}
