package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Helper.CreditoDebitoEnum;
import br.com.felipedeveloper.gestaofinanceira.Helper.OpcoesFinanceirasEnum;
import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MeiodePagamentoActivity extends BaseActivity {

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
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String idUser;
    private DatabaseReference myreference;
    private ContasBancarias contasBancarias;
    private Cartao cartao;
    private Carteira carteira;
    private String opcaofinanceira;
    private Lancamento lancamento;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meio_pagamento);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        opcaofinanceira = extras.getString("opcao");

        switch (opcaofinanceira) {
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
        edtSaldoBanco.addTextChangedListener(onTextChangedListener(edtSaldoBanco));

        contasBancarias = new ContasBancarias();
        cartao = new Cartao();
        carteira = new Carteira();
        lancamento = new Lancamento();


        btnAdicionarBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String dataformatada = formataData.format(date);
                dataformatada = dataformatada.replaceAll("-","/");
                Long timestamp = criaTimeStamp();
                String trocaVirgulaPonto;
                lancamento.setStatusOp(CreditoDebitoEnum.Credito.getValor());
                lancamento.setData(dataformatada);
                trocaVirgulaPonto = edtSaldoBanco.getText().toString().replaceAll(",", ".");
                lancamento.setValor(Double.parseDouble(trocaVirgulaPonto));
                lancamento.setStatusOp(CreditoDebitoEnum.Credito.getValor());
                lancamento.setIdLancamento(UUID.randomUUID().toString());
                lancamento.setCreatedAt(timestamp);


                if (opcaofinanceira.equals(OpcoesFinanceirasEnum.cartao.getValor())) {
                    cartao.setIdcartao(UUID.randomUUID().toString());
                    lancamento.setTitulo("Adicionando " + OpcoesFinanceirasEnum.cartao.getValor());
                    lancamento.setNomeopFinanceira(cartao.getIdcartao());
                    cartao.setTituloCartao(edtTituloBanco.getText().toString());
                    if (!edtSaldoBanco.getText().toString().isEmpty()) {
                        cartao.setSaldoCartao(Double.parseDouble(trocaVirgulaPonto));
                        myreference.child("cartao").child(cartao.getIdcartao()).setValue(cartao);
                        myreference.child("lancamentos").child(lancamento.getIdLancamento()).setValue(lancamento);
                        ExibirMensagem(MeiodePagamentoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Cartão Adicionado !");
                        clear();
                    }
                }
                if (opcaofinanceira.equals(OpcoesFinanceirasEnum.carteira.getValor())) {
                    carteira.setIdCarteira(UUID.randomUUID().toString());
                    carteira.setTituloCarteira(edtTituloBanco.getText().toString());
                    lancamento.setTitulo("Adicionando " + OpcoesFinanceirasEnum.carteira.getValor());
                    lancamento.setNomeopFinanceira(carteira.getIdCarteira());
                    if (!edtSaldoBanco.getText().toString().isEmpty()) {
                        carteira.setSaldoCarteira(Double.parseDouble(trocaVirgulaPonto));
                        myreference.child("carteira").child(carteira.getIdCarteira()).setValue(carteira);
                        myreference.child("lancamentos").child(lancamento.getIdLancamento()).setValue(lancamento);
                        ExibirMensagem(MeiodePagamentoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Carteira Adicionada !");
                        clear();
                    }

                }
                if (opcaofinanceira.equals(OpcoesFinanceirasEnum.banco.getValor())) {
                    contasBancarias.setIdContaBanco(UUID.randomUUID().toString());
                    contasBancarias.setTituloContabanco(edtTituloBanco.getText().toString());
                    lancamento.setTitulo("Adicionando " + OpcoesFinanceirasEnum.banco.getValor());
                    lancamento.setNomeopFinanceira(contasBancarias.getIdContaBanco());
                    if (!edtSaldoBanco.getText().toString().isEmpty()) {
                        contasBancarias.setSaldoContabancaria(Double.parseDouble(trocaVirgulaPonto));
                        myreference.child("banco").child(contasBancarias.getIdContaBanco()).setValue(contasBancarias);
                        myreference.child("lancamentos").child(lancamento.getIdLancamento()).setValue(lancamento);
                        ExibirMensagem(MeiodePagamentoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Conta Adicionada !");
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

    private void clear() {
        edtSaldoBanco.clearFocus();
        edtTituloBanco.setText("");
        edtSaldoBanco.setText("");
    }
}
