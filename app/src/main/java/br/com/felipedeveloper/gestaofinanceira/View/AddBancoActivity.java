package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Helper.CreditoDebitoEnum;
import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
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
    private Lancamento lancamento;



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
        lancamento = new Lancamento();


                btnAdicionarBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String dataformatada = formataData.format(date);
                Long timestamp = criaTimeStamp();
                lancamento.setStatusOp(CreditoDebitoEnum.Credito.getValor());
                lancamento.setData(dataformatada);
                lancamento.setValor(Double.parseDouble(edtSaldoBanco.getText().toString()));
                lancamento.setStatusOp(CreditoDebitoEnum.Credito.getValor());
                lancamento.setIdLancamento(UUID.randomUUID().toString());
                lancamento.setCreatedAt(timestamp);
//                git add *


                if (opcaofinanceira.equals("cartao")){
                    cartao.setIdcartao(UUID.randomUUID().toString());
                    lancamento.setTitulo("Adicionando " + cartao.getClass().getName());
                    cartao.setTituloCartao(edtTituloBanco.getText().toString());
                    if (!edtSaldoBanco.getText().toString().isEmpty()){
                        carteira.setSaldoCarteira(Double.parseDouble(edtSaldoBanco.getText().toString()));
                        myreference.child("cartao").child(cartao.getIdcartao()).setValue(cartao);
                        myreference.child("lancamentos").child(lancamento.getIdLancamento()).setValue(lancamento);
                        ExibirMensagem(AddBancoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Cartão Adicionado !");
                        clear();
                    }
                }
                if (opcaofinanceira.equals("carteira")){
                    carteira.setIdCarteira(UUID.randomUUID().toString());
                    carteira.setTituloCarteira(edtTituloBanco.getText().toString());
                    lancamento.setTitulo("Adicionando " + carteira.getClass().getName());
                    if (!edtSaldoBanco.getText().toString().isEmpty()){
                        carteira.setSaldoCarteira(Double.parseDouble(edtSaldoBanco.getText().toString()));
                        myreference.child("carteira").child(carteira.getIdCarteira()).setValue(carteira);
                        myreference.child("lancamentos").child(lancamento.getIdLancamento()).setValue(lancamento);
                        ExibirMensagem(AddBancoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Carteira Adicionada !");
                        clear();
                    }

                }
                if (opcaofinanceira.equals("banco")){
                    contasBancarias.setIdContaBanco(UUID.randomUUID().toString());
                    contasBancarias.setTituloContabanco(edtTituloBanco.getText().toString());
                    if (!edtSaldoBanco.getText().toString().isEmpty()) {
                        contasBancarias.setSaldoContabancaria(Double.parseDouble(edtSaldoBanco.getText().toString()));
                        myreference.child("banco").child(contasBancarias.getIdContaBanco()).setValue(contasBancarias);
                        myreference.child("lancamentos").child(lancamento.getIdLancamento()).setValue(lancamento);
                        ExibirMensagem(AddBancoActivity.this, SweetAlertDialog.NORMAL_TYPE, "Conta Adicionada !");
                        clear();
                    }


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
        myreference = firebaseDatabase.getReference().child("financeiro").child(idUser);
    }
    /**
     * Metodo responsavel por criar o timestamp do lançamento para futuramente ter a precisao de cada lançamento
     *
     * @return timesTamp
     */
    @NonNull
    private Long criaTimeStamp() {
        Calendar cal = Calendar.getInstance();
        Date data_atual = cal.getTime();
        final Timestamp ts = new Timestamp(data_atual.getTime());
        return ts.getTime();
    }
    private void clear(){
        edtSaldoBanco.clearFocus();
        edtTituloBanco.setText("");
        edtSaldoBanco.setText("");
    }
}
