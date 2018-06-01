package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
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

    //region elementos da View
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
    //endregion


    //region Variaveis Globais
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String idUser;
    private DatabaseReference myreference;
    private ContasBancarias contasBancarias;
    private Cartao cartao;
    private Carteira carteira;
    private String opcaofinanceira;
    private Lancamento lancamento;
    //endregion


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meio_pagamento);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        /**
         * Recebendo String da activity anterior
         * para saber qual opcao deve ser adicionada
         * a tela de Cartoes envia a contante cartao
         * a tela banco envia a constante banco
         * e a carteira envia a constante carteira
         * para que o texto da view seja mudado
         */
        Bundle extras = getIntent().getExtras();
        opcaofinanceira = extras.getString("opcao");

        /**
         * metodo responsavel por tratar as labels quando der erro na view.
         * situacao : usuario tento inserir registro em branco
         * uma mensagem aparece e este metodo remove a mensagem quando o usuario começa a digitar
         * cada editext da view tem seu metodo;
         */
        //region Tratando Labels
        edtTituloBanco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutTitulo.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtSaldoBanco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutSaldo.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
        /**
         * Verificando qual a opcao recebida da ttela anterior
         */
        switch (opcaofinanceira) {
            case "banco":
                // personalizando a view para adicionar um banco.
                getSupportActionBar().setTitle("Adicionar Banco"); // mudando o nome da toolbar
                inputLayoutTitulo.setHint("Titulo Banco"); // mudando o hint dos botoes
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

        // instacia do firebase para pegar o usuario logado
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid(); // recuperando a id do usuario logado
        IniciaFirebase(); // inicializando conexao com o bd do firebase
        edtSaldoBanco.addTextChangedListener(onTextChangedListener(edtSaldoBanco)); // adcionando um listner ao campo de valor para que esse mesmo seja formatado


        InitObjects(); // metodo que faz a instancia de um novo objeto

        /**
         * listener do botão de adicionar e onde a magica acontece !!
         */
        btnAdicionarBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // recuperando a data diaria para ser adiconada ao lancamento
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String dataformatada = formataData.format(date);
                dataformatada = dataformatada.replaceAll("-", "/"); // trocando o simbolo de - da data pelo / tradicional
                boolean validaOK = false;


                Long timestamp = criaTimeStamp(); // criando timestamp para exebir itens na lista com base na hora que foi adicionado
                String trocaVirgulaPonto;
                lancamento.setStatusOp(CreditoDebitoEnum.Credito.getValor()); // enun que passa o status como cridito
                lancamento.setData(dataformatada);
                validaOK = validaCampos();
                if (validaOK) {
                    trocaVirgulaPonto = edtSaldoBanco.getText().toString().replaceAll(",", ""); // salva sem a virgula e depois a coloca novamente
                    /**
                     * Adiconando valores ao objeto de lançemento pois nesse momento é necessario fazer um Lancamento para ficar registrado
                     * no bd o valor que voc~e adicionou e posteriormente ser exibido na linha do tempo geral como na do cartão;
                     */
                    lancamento.setValor(Double.parseDouble(trocaVirgulaPonto));
                    lancamento.setStatusOp(CreditoDebitoEnum.Credito.getValor());
                    lancamento.setIdLancamento(UUID.randomUUID().toString());
                    lancamento.setCreatedAt(timestamp);

                    /**
                     * neste momento é verificado se a opcao financeira recebida pela activity é uma Carteira um cartoa ou um banco
                     */
                    if (opcaofinanceira.equals(OpcoesFinanceirasEnum.cartao.getValor())) { // enum para verificar opcao financeira Neste caso é um CARTÂO
                        cartao.setIdcartao(UUID.randomUUID().toString());
                        lancamento.setTitulo("Adicionando " + OpcoesFinanceirasEnum.cartao.getValor()); // atribuições especificas a algumas variaveis de lancamento
                        lancamento.setNomeopFinanceira(cartao.getIdcartao());
                        cartao.setTituloCartao(edtTituloBanco.getText().toString());
                        if (!edtSaldoBanco.getText().toString().isEmpty()) {
                            cartao.setSaldoCartao(Double.parseDouble(trocaVirgulaPonto));
                            myreference.child("cartao").child(cartao.getIdcartao()).setValue(cartao); // Salvando o cartao no BD
                            myreference.child("lancamentos").child(lancamento.getIdLancamento()).setValue(lancamento);// salvando o lancamento no bd
                            ExibirMensagem(MeiodePagamentoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Cartão Adicionado !"); // exebindo a mensagem de sucesso na operação
                            clear();
                        }
                    }

                    /**
                     * mesmo precesso anterior para  salvar uma CARTEIRA e um LANCAMENTO
                     */
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
                    /**
                     * mesmo precesso anterior para  salvar uma BANCO e um LANCAMENTO
                     */
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
            }
        });

    }

    /**
     * Metodo responsavel por validar as entradas do usuario
     * nos campos da View
     *
     * @return
     */
    private boolean validaCampos() {
        boolean retorno = false; // Variavel de verificacao

        /**
         * se o campo for vazio entra no if e exibe o erro na view
         */
        if (edtSaldoBanco.getText().toString().isEmpty() || edtSaldoBanco.getText().toString() == null) {
            inputLayoutSaldo.setError("Valor Invalido");
        }
        if (edtTituloBanco.getText().toString().isEmpty() || edtTituloBanco.getText().toString() == null) {
            inputLayoutTitulo.setError("Valor Invalido");
        }
        /**
         * teste para verificar se opcoes atendende para retornar Verdadeiro e seguir com a adição.
         */
        if (!edtSaldoBanco.getText().toString().isEmpty() && !edtTituloBanco.getText().toString().isEmpty()) {
            retorno = true;
        }

        return retorno;
    }

    /**
     * iniciando novos Objetos que seram utilizados pela classe
     */
    private void InitObjects() {
        contasBancarias = new ContasBancarias();
        cartao = new Cartao();
        carteira = new Carteira();
        lancamento = new Lancamento();
    }

    /**
     * metodo responsavel por inicializar o firebase e recuperar a instanca da View
     */
    private void IniciaFirebase() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            idUser = firebaseUser.getUid();
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // instancia do Bd
        myreference = firebaseDatabase.getReference().child("financeiro").child(idUser);  // instancia do Bd porem somente so usuario logado
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

    /**
     * metoodo responsavel por limpar os campos da View
     */
    private void clear() {
        edtSaldoBanco.clearFocus();
        edtTituloBanco.setText("");
        edtSaldoBanco.setText("");
    }
}
