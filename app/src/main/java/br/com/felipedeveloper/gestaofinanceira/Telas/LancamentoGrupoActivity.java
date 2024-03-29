package br.com.felipedeveloper.gestaofinanceira.Telas;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Ajuda.CreditoDebitoEnum;
import br.com.felipedeveloper.gestaofinanceira.Modelo.Cartao;
import br.com.felipedeveloper.gestaofinanceira.Modelo.Carteira;
import br.com.felipedeveloper.gestaofinanceira.Modelo.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.Modelo.Grupo;
import br.com.felipedeveloper.gestaofinanceira.Modelo.LancamentoGrupo;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LancamentoGrupoActivity extends BaseActivity {

    //region Elementos da View Botoes campos
    @BindView(R.id.textInpvalor)
    TextInputLayout layoutvalor;
    @BindView(R.id.texteditvalor)
    EditText textvalor;
    @BindView(R.id.textInpdata)
    TextInputLayout layoutdata;
    @BindView(R.id.texteditdata)
    EditText textdata;
    @BindView(R.id.textInputtitulo)
    TextInputLayout textInptitulo;
    @BindView(R.id.textedittitulo)
    EditText texttitulo;
    @BindView(R.id.floatingconfirmar)
    FloatingActionButton btconfirmar;
    @BindView(R.id.spinneropbancaria)
    Spinner spinneropcao;
    @BindView(R.id.checkedTextView)
    CheckBox checkFontesDesconhecidas;
    android.support.v7.widget.SwitchCompat aSwitch;
    //endregion

    //region Variaveis Globais
    DatePickerDialog.OnDateSetListener date;
    LancamentoGrupo lancamentoGrupo;
    List<ContasBancarias> contasBancariasList;
    List<Cartao> cartaoList;
    List<Carteira> carteiraList;
    List<String> financeiroSpinnerlist;
    DataSnapshot globalSnapshot;
    String nomeopFinanceira;
    Context context = LancamentoGrupoActivity.this;
    private DatabaseReference myreference;
    private Calendar myCalendar;
    private FirebaseUser firebaseUser;
    private String Idgrupo;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento_grupo);
        ButterKnife.bind(this);
        InitActionBarSupport();

        Bundle extras = getIntent().getExtras();
        Idgrupo = extras.getString("idgrupo");

        configFirebase();
        myCalendar = Calendar.getInstance();
        aSwitch = findViewById(R.id.switchaaddvalor);
        InitObjetos();

        // metodos utilizados para limpar o erro quando usuario comeca a digitar
        //region Estetica error
        texttitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInptitulo.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        textdata.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutvalor.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
        textvalor.addTextChangedListener(onTextChangedListener(textvalor));


        /**
         * metodo responsavel por buscar dadoso no firebase na child("financeiro")
         * neste momento busco todos os dados do firebase.
         * para na sequencia buscar os dados dentro de cada nó
         * especifico.
         *
         */
        myreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cartao card;
                String tituloOpFinanceira;
                ContasBancarias bancarias;
                Carteira carteira;
                globalSnapshot = dataSnapshot; // varivel de nó global do firebase

                /**
                 * limpando as listas pois os dados veem em tempo real,
                 * e caso a lista possua dados assim é adicionado mais
                 * tornando os dados inconsistentes
                 */
                contasBancariasList.clear();
                cartaoList.clear();
                financeiroSpinnerlist.clear();
                financeiroSpinnerlist.add("Origem dinheiro"); // adicionando primeiro elemento do spinner

                /**
                 * Iterando o snapshot do firebase no nó ("banco") e passando a UUID do usuario logado para trazer somente os seus bancos.
                 *
                 */
                for (DataSnapshot d : dataSnapshot.child(firebaseUser.getUid()).child("banco").getChildren()) {
                    bancarias = d.getValue(ContasBancarias.class);
                    if (bancarias != null) { // se a variavel bancarias nao for vazia adiciono no arrayLIst de bancos para serem exibidos no spinner.
                        tituloOpFinanceira = bancarias.getTituloContabanco();// capturando o nome dos bancos do usuario logado
                        financeiroSpinnerlist.add(tituloOpFinanceira); //  adicionando somente o titulo dos bancos na lista de strings
                    }
                    contasBancariasList.add(bancarias); // agora adiciono o objeto banco a lista de bancos
                }

                /**
                 * Iterando o snapshot do firebase no nó ("cartao") e passando a UUID do usuario logado para trazer somente os seus cartões.
                 *
                 */
                for (DataSnapshot d : dataSnapshot.child(firebaseUser.getUid()).child("cartao").getChildren()) {
                    card = d.getValue(Cartao.class);// objeto de cartao que recebe o cartao do firebase
                    if (card != null) {
                        tituloOpFinanceira = card.getTituloCartao(); // recuperando somente o nome dos cartoes  do usuario
                        financeiroSpinnerlist.add(tituloOpFinanceira); // adicionando o nome do cartao na mesma lista de string para exibilas no spinner
                    }
                    cartaoList.add(card); // adicionando o objeto cartão a lista de cartoes

                }

                for (DataSnapshot d : dataSnapshot.child(firebaseUser.getUid()).child("carteira").getChildren()) {
                    carteira = d.getValue(Carteira.class);// objeto de carteira que recebe a cartera do firebase
                    if (carteira != null) {
                        tituloOpFinanceira = carteira.getTituloCarteira(); // recuperando somente o nome das carteiras  do usuario
                        financeiroSpinnerlist.add(tituloOpFinanceira); // adicionando o nome da carteira na mesma lista de string para exibilas no spinner
                    }
                    carteiraList.add(carteira); // adicionando o objeto cartão a lista de cartoes

                }


                /**
                 * configurando o adapter de string para adicionar os ttulos das formas de pagamentos.
                 */
                ArrayAdapter<String> mesadapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, financeiroSpinnerlist);// passando a lista de titulos para
                // serem exibidos no modelo padrao de lista
                mesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinneropcao.setAdapter(mesadapter);
                spinneropcao.setFocusable(true);
                spinneropcao.setFocusableInTouchMode(true);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**
         * Listner do click do switch de credito ou debito.
         * caso a variavel b seja verdadeira muda o texto para credito
         * caso seja falso muda se o testo para debito
         * e mudando as cores do botão
         */
        //region click Switch
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    aSwitch.setText("Adicionar Dinheiro no Grupo");
                    aSwitch.setTextColor(getResources().getColor(R.color.colorSwitchcredito));
                } else {
                    aSwitch.setText("Retirar Dinheiro do Grupo");
                    aSwitch.setTextColor(getResources().getColor(R.color.colorSwitchdebito));
                }
            }
        });
        //endregion

        //region click CheckBox fontes desconhecidas
        checkFontesDesconhecidas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    spinneropcao.setEnabled(false);
                } else {

                    spinneropcao.setEnabled(true);
                }
            }
        });
        //endregion


        /**
         * Metodo responsavel por exibir a data no formato
         * dd/mm/aa  apos o dialog de data ser fechado
         */
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        /**
         *
         * Listner do botao de data.
         * quando o botão de data receber o foco ao inves de exibir o teclado ele abre o dialogo de calendario
         */
        textdata.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(context, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }


            }
        });

        /**
         * Listner de evento quando o botao de ok do teclado é clicado
         * neste momento o teclado é fechado para que o usuario possa selecionar a conta no spinner
         * melhorando o fluxo do app
         */
        texttitulo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                // se o botão for o ok do teclado. O teclado sera fechado
                if (i == EditorInfo.IME_ACTION_DONE) {
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            texttitulo.getWindowToken(), 0);
                    texttitulo.clearFocus();
                }
                return false;
            }
        });

        /**
         * listner do botão de confirmar a operação de credito ou debito;
         */
        btconfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validaOK;
                validaOK = RecuperandoDadosdaView();// recuperando oque esta digitado nos campos
                if (validaOK) {

                    lancamentoGrupo.setData(textdata.getText().toString()); // recuperando oque foi digitado no campo de data
                    lancamentoGrupo.setTitulo(texttitulo.getText().toString()); // recuperando oque foi digitado no titulo
                    String trocaVirgulaPonto = textvalor.getText().toString().replaceAll(",", ""); // trocando a virgula da mascara pelo ponto que é aceito pelo Java
                    lancamentoGrupo.setValor(Double.parseDouble(trocaVirgulaPonto));// recupara o valor que foi digitado que entra como String //  e o converte para Double conforme a classe espera
                    lancamentoGrupo.setNomeGrupo(Idgrupo); // passa pela activity anterior via Bundle
                    final Long a = criaTimeStamp(); // recuperando o timestamp criado
                    lancamentoGrupo.setCreatedAt(a);// atribuindo o timestamp ao objeto lançamento que sera enviado a firebase

                    /**
                     * verificando se a opçao de deposito é para conta corrente ou cartao.
                     */
                    nomeopFinanceira = spinneropcao.getSelectedItem().toString();// recebendo o texto selecionado do spinner
                    String ret = verificaOpcaoFinanceira(nomeopFinanceira);


                    lancamentoGrupo.setNomeColaborador(firebaseUser.getDisplayName());
                    lancamentoGrupo.setNomeopFinanceira(nomeopFinanceira);
                    Grupo g = globalSnapshot.child("grupos").child(Idgrupo).getValue(Grupo.class);

                    /**
                     * verificando se o switch esta selecionado ou nao.
                     * neste caso se é CREDITO ou DEBITO a operação a ser feita
                     */
                    if (aSwitch.isChecked()) { // creditar valor no grupo
                        lancamentoGrupo.setStatusOp(CreditoDebitoEnum.Credito.getValor());// passando o 1 para o objeto lançamento para posteriormente saber qual lançamento foi credito ou debito
                        final Map<String, Object> retorno = g.mapCreditaGrupo(g, lancamentoGrupo.getValor());
                        /**
                         * Debitando cartao de credito ou conta
                         */
                        if (spinneropcao.getSelectedItemPosition() != 0) {
                            String statusSaldo = null;
                            if (!ret.isEmpty()) {
                                if (ret.equals("cartao")) {
                                    statusSaldo = operacaoDebitoCartao(ret);
                                } else if (ret.equals("banco")) {
                                    statusSaldo = adicionandoDebitoConta(ret);
                                } else if (ret.equals("carteira")) {
                                    statusSaldo = adicionandoDebitoCarteira(ret);
                                }

                                /**
                                 * salvando dados em mais um um nó
                                 * salvando lancamento e atualizando saldo do grupo
                                 */
                                switch (statusSaldo) {
                                    case "DebitocartaoOK":
                                        ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Valores Removidos do cartão e adicionados ao grupo");
                                        salvarLancamentoFirebase(lancamentoGrupo);
                                        UpdateSaldoGrupo(retorno);
                                        break;

                                    case "saldoInsulficientecartao":
                                        ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "O Cartão não possui limite disponivel !");

                                        break;
                                    case "DebitocontaoOK":
                                        salvarLancamentoFirebase(lancamentoGrupo);
                                        UpdateSaldoGrupo(retorno);
                                        ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Valores Removidos da conta e adicionados ao grupo");
                                        break;
                                    case "saldoInsulficienteconta":
                                        ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "Conta não possui saldo sulficiente");
                                        break;
                                    case "DebitocarteiraOK":
                                        ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Valores Removidos da Carteira e adicionados ao grupo.");
                                        salvarLancamentoFirebase(lancamentoGrupo);
                                        UpdateSaldoGrupo(retorno);
                                        break;

                                    case "saldoInsulficientecarteira":
                                        ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "A Carteira não possui limite disponivel !");
                                        break;


                                }

                            }
                        }

                        /**
                         *  permitindo fontes desconhecidas remover e adicionar valores
                         *  valores entrar e sair do grupo sem estarem cadastrados dentro do app
                         */
                        if (checkFontesDesconhecidas.isChecked()) {

                            if (retorno != null) {
                                // exebindo dialogo ao usuario
                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Team Money")
                                        .setContentText("Os valores adicionados no grupo\n não seram removidos de nenhuma conta,\n Gostaria de continuar ?")
                                        .setCancelText("Cancelar")
                                        .setConfirmText("Adicionar")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.showCancelButton(false);
                                                salvarLancamentoFirebase(lancamentoGrupo);
                                                UpdateSaldoGrupo(retorno);


                                                sweetAlertDialog.setTitle("Team Money");
                                                sweetAlertDialog.setConfirmText("OK");
                                                sweetAlertDialog.setContentText("Valores Adicionados !");
                                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.cancel();
                                                    }
                                                });
                                                sweetAlertDialog.show();
                                            }
                                        })
                                        .showCancelButton(true)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();

                                            }
                                        })
                                        .show();

                            }
                        }
                        // caso não seja utilizado fontes desconhecidas é necessario selecionar uma conta
                        if (!checkFontesDesconhecidas.isChecked()) {
                            if (spinneropcao.getSelectedItemPosition() == 0) {
                                ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.WARNING_TYPE, "Selecione uma conta !");
                            }


                        }

                    } else { // Retirando dinheiro do Grupo e adiconando a alguma conta  do usuario ou fonte desconhecida

                        lancamentoGrupo.setStatusOp(CreditoDebitoEnum.Debito.getValor());
                        if (g.getSaldoGrupo() < lancamentoGrupo.getValor()) {
                            ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "Operação não realizada.\nO grupo não possui saldo sulficiente !");
                        } else {
                            final Map<String, Object> retorno = g.mapDebitaGrupo(g, lancamentoGrupo.getValor());
                            if (!lancamentoGrupo.getNomeopFinanceira().equals("Origem dinheiro")) {
                                String retornoOperacao = null;

                                // so vai entrat aqui quando uma fonte de reda for selecionada
                                if (!ret.isEmpty()) {
                                    if (ret.equals("cartao")) {
                                        retornoOperacao = adicionandoCreditoCartao(ret);
                                    } else if (ret.equals("banco")) {
                                        retornoOperacao = adicionandoCreditoBanco(ret);
                                    } else if (ret.equals("carteira")) {
                                        retornoOperacao = adicionandoCreditoCarteira(ret);
                                    }
                                    switch (retornoOperacao) {
                                        case "CreditoCartaoOK":
                                            salvarLancamentoFirebase(lancamentoGrupo);
                                            UpdateSaldoGrupo(retorno);
                                            ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "O valor foi transferido para seu cartão !");

                                            break;
                                        case "CreditoBancoOK":
                                            salvarLancamentoFirebase(lancamentoGrupo);
                                            UpdateSaldoGrupo(retorno);
                                            ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "O valor foi transferido para sua conta bancaria !");
                                            break;

                                        case "CreditoCarteiraOK":
                                            salvarLancamentoFirebase(lancamentoGrupo);
                                            UpdateSaldoGrupo(retorno);
                                            ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "O valor foi transferido para sua Carteira !");
                                            break;
                                    }

                                }

                            }
                            if (checkFontesDesconhecidas.isChecked()) {
                                //TAVA AQUI
                                //passar o saldo do grupo para comparar com o valor do debito e analisar como foi feito em lancamentoGrupo
                                if (retorno != null) {
                                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Team Money")
                                            .setContentText("Os valores removidos do grupo\n não seram adicionados a nenhuma conta,\n Gostaria de continuar ?")
                                            .setCancelText("Cancelar")
                                            .setConfirmText("Remover")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.showCancelButton(false);
                                                    salvarLancamentoFirebase(lancamentoGrupo);
                                                    myreference.child("grupos").child(Idgrupo).updateChildren(retorno);
                                                    sweetAlertDialog.setTitle("Team Money");
                                                    sweetAlertDialog.setConfirmText("OK");
                                                    sweetAlertDialog.setContentText("Valores Retirados do Grupo");
                                                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.cancel();
                                                        }
                                                    });
                                                    sweetAlertDialog.show();
                                                }
                                            })
                                            .showCancelButton(true)
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.cancel();

                                                }
                                            })
                                            .show();
                                } else {
                                    ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "Saldo Grupo Insulficiente !");
                                }
                            } else if (spinneropcao.getSelectedItemPosition() == 0) {
                                ExibirMensagem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "Selecione uma conta para recebimento. !");
                            }

                        }

                    }
                }
            }
        });
    }

    private String adicionandoCreditoCarteira(String opFinanceira) {
        String retornoStatus = "";
        if (opFinanceira.equals("carteira")) {
            for (Carteira b : carteiraList) { // percorrendo a lisat de cartoes
                if (b.getTituloCarteira().equals(nomeopFinanceira)) { // comparando os nomes
                    Carteira aux;
                    aux = b; // caso o nome seja encontrado sera armazenado na variavel aux

                    /**
                     * fazendo uma busca no snapshot global  evitando uma nova consulta e recuperando o cartao que tera seu valor atualizado;
                     * para recuperar o cartao é passado o nome do nó que no caso é cartao depois a id do usuario logado, e por final
                     * a id do cartao que foi selecionado no spinner. com isso o objeto é recuperado e esta pronto para ser atualizado.
                     *  ex: cartao a ser atualizado= "cartao" -> "idUsuariologado"-> "idcartaoselecionado";
                     */
                    Carteira carteira = globalSnapshot.child(firebaseUser.getUid()).child(opFinanceira).child(aux.getIdCarteira()).getValue(Carteira.class);


                    /**
                     * Para se atualizar os dados no firebase passar o objeto por enteiro iria o apagar e criar outro com novos dados e Ids
                     * entao a maneira correta é criando um hashMap e passando odmente oque sera atualizado
                     * que nesse caso ´o valor.
                     * o metodo mapcartao de credito  da classe cartao já atualiza os dados. fazendo a soma do valor que tinha e do novo e retornando
                     * um novo hash map.
                     * para atualizar os dados é enviado o valor adicionado no editext da view w o objeto que recebera o valor
                     *
                     */
                    Map<String, Object> Mapsaldoatualizado = carteira.MapCarteiraCredito(carteira, lancamentoGrupo.getValor());

                    if (Mapsaldoatualizado != null) {// tratando erro de saldo que é verificado na classe cartao
                        /**
                         * passando os caminhos e o map que vai atualizar o que esta no firebase
                         */
                        atualizaSaldoOpcaoFinanceiraFirebase(opFinanceira, carteira.getIdCarteira(), Mapsaldoatualizado);
                        retornoStatus = "CreditoCarteiraOK";
                    }


                }

            }

        }
        return retornoStatus;

    }

    private String adicionandoDebitoCarteira(String opFinanceira) {
        String retorno = "";
        if (opFinanceira.equals("carteira")) {
            for (Carteira b : carteiraList) {
                if (b.getTituloCarteira().equals(nomeopFinanceira)) {
                    Carteira aux;
                    aux = b;
                    Carteira carteira = globalSnapshot.child(firebaseUser.getUid()).child(opFinanceira).child(aux.getIdCarteira()).getValue(Carteira.class);
                    Map<String, Object> rec = carteira.MapCarteiraDebito(carteira, lancamentoGrupo.getValor());
                    if (rec != null) {
                        atualizaSaldoOpcaoFinanceiraFirebase(opFinanceira, carteira.getIdCarteira(), rec);
                        retorno = "DebitocarteiraOK";
                    } else {
                        retorno = "saldoInsulficientecarteira";
                    }

                }

            }
        }

        return retorno;

    }

    private void InitActionBarSupport() {
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lançamentos Grupo");
    }

    private void UpdateSaldoGrupo(Map<String, Object> retorno) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("financeiro").child("grupos").child(Idgrupo).updateChildren(retorno);
    }

    private void InitObjetos() {
        contasBancariasList = new ArrayList<>();
        financeiroSpinnerlist = new ArrayList<>();
        cartaoList = new ArrayList<>();
        carteiraList = new ArrayList<>();
        lancamentoGrupo = new LancamentoGrupo();
    }

    /**
     * @param ret
     * @return boolean false sem saldo, True com saldo
     */
    private String operacaoDebitoCartao(String ret) {
        String retorno = "";
        if (ret.equals("cartao")) {
            for (Cartao b : cartaoList) {
                if (b.getTituloCartao().equals(nomeopFinanceira)) {
                    Cartao aux;
                    aux = b;
                    Cartao cardatualiza = globalSnapshot.child(firebaseUser.getUid()).child(ret).child(aux.getIdcartao()).getValue(Cartao.class);
                    Map<String, Object> rec = cardatualiza.MapcartaoDebito(cardatualiza, lancamentoGrupo.getValor());
                    if (rec != null) {
                        atualizaSaldoOpcaoFinanceiraFirebase(ret, cardatualiza.getIdcartao(), rec);
                        retorno = "DebitocartaoOK";
                    } else {
                        retorno = "saldoInsulficientecartao";
                    }

                }

            }
        }

        return retorno;
    }

    private String adicionandoDebitoConta(String ret) {
        String retornoStatus = "";
        if (ret.equals("banco")) {
            for (ContasBancarias contas : contasBancariasList) {
                if (contas.getTituloContabanco().equals(nomeopFinanceira)) {
                    ContasBancarias bancarias;
                    bancarias = contas;
                    ContasBancarias ban = globalSnapshot.child(firebaseUser.getUid()).child(ret).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                    Map<String, Object> rec = ban.MapBancoDebita(ban, lancamentoGrupo.getValor());
                    if (rec != null) {
                        atualizaSaldoOpcaoFinanceiraFirebase(ret, bancarias.getIdContaBanco(), rec);
                        retornoStatus = "DebitocontaoOK";
                    } else {
                        retornoStatus = "saldoInsulficienteconta";
                    }
                }

            }
        }
        return retornoStatus;
    }


    private String adicionandoCreditoBanco(String ret) {
        String retornoStatus = "";
        //caso a opcao financeira selecionada seja cartao a lista de cartoes sera percorrida até el ser encontrado
        if (ret.equals("banco")) {
            for (ContasBancarias contas : contasBancariasList) {
                if (contas.getTituloContabanco().equals(nomeopFinanceira)) {
                    ContasBancarias bancarias;
                    bancarias = contas;
                    ContasBancarias contBancos = globalSnapshot.child(firebaseUser.getUid()).child(ret).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                    Map<String, Object> rec = contBancos.MapBancoCredita(contBancos, lancamentoGrupo.getValor());
                    if (rec != null) {

                        atualizaSaldoOpcaoFinanceiraFirebase(ret, bancarias.getIdContaBanco(), rec);
                        retornoStatus = "CreditoBancoOK";
                    }


                }

            }
        }


        return retornoStatus;
    }

    private String adicionandoCreditoCartao(String opFinanceira) {
        String retornoStatus = "";
        if (opFinanceira.equals("cartao")) {
            for (Cartao b : cartaoList) { // percorrendo a lisat de cartoes
                if (b.getTituloCartao().equals(nomeopFinanceira)) { // comparando os nomes
                    Cartao aux;
                    aux = b; // caso o nome seja encontrado sera armazenado na variavel aux

                    /**
                     * fazendo uma busca no snapshot global  evitando uma nova consulta e recuperando o cartao que tera seu valor atualizado;
                     * para recuperar o cartao é passado o nome do nó que no caso é cartao depois a id do usuario logado, e por final
                     * a id do cartao que foi selecionado no spinner. com isso o objeto é recuperado e esta pronto para ser atualizado.
                     *  ex: cartao a ser atualizado= "cartao" -> "idUsuariologado"-> "idcartaoselecionado";
                     */
                    Cartao cardatualiza = globalSnapshot.child(firebaseUser.getUid()).child(opFinanceira).child(aux.getIdcartao()).getValue(Cartao.class);


                    /**
                     * Para se atualizar os dados no firebase passar o objeto por enteiro iria o apagar e criar outro com novos dados e Ids
                     * entao a maneira correta é criando um hashMap e passando odmente oque sera atualizado
                     * que nesse caso ´o valor.
                     * o metodo mapcartao de credito  da classe cartao já atualiza os dados. fazendo a soma do valor que tinha e do novo e retornando
                     * um novo hash map.
                     * para atualizar os dados é enviado o valor adicionado no editext da view w o objeto que recebera o valor
                     *
                     */
                    Map<String, Object> Mapsaldoatualizado = cardatualiza.MapCartaoCredito(cardatualiza, lancamentoGrupo.getValor());

                    if (Mapsaldoatualizado != null) {// tratando erro de saldo que é verificado na classe cartao
                        /**
                         * passando os caminhos e o map que vai atualizar o que esta no firebase
                         */
                        atualizaSaldoOpcaoFinanceiraFirebase(opFinanceira, cardatualiza.getIdcartao(), Mapsaldoatualizado);
                        retornoStatus = "CreditoCartaoOK";
                    }


                }

            }

        }
        return retornoStatus;
    }

    /**
     * Metodo responsavel por atualizar os valores do BANCO e do CARTÃO de credito.
     * tando credito quanto debito
     *
     * @param opFinanceira       nome do nó .EX ou cartao ou banco
     * @param idOpFinanceira     id do cartao ou do banco
     * @param mapsaldoatualizado hash map do banco ou cartao com os dados atuaalizados para update
     */
    public void atualizaSaldoOpcaoFinanceiraFirebase(String opFinanceira, String idOpFinanceira, Map<String, Object> mapsaldoatualizado) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(); // recuperando uma nova referenciaa do banco de dados
        reference.child("financeiro").child(firebaseUser.getUid()).child(opFinanceira).child(idOpFinanceira).updateChildren(mapsaldoatualizado);
    }

    /**
     * metodo reponsavel por salvar o lançamento no firebase no nó ("lancamentoGrupo")
     *
     * @param ll é o lançamento que foi digitado na tela pelo usuario. neste momento o envio ao firebase é feito
     */
    private void salvarLancamentoFirebase(LancamentoGrupo ll) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("financeiro").child("grupos").child(Idgrupo).child("lancamentos").child(UUID.randomUUID().toString()).setValue(ll);
        limpar();// limpando a view apos inserir os dados

    }

    /**
     * atribuindo os dados informados na view  a um objeto lançamento que fara a movimentação dos dados
     * até que o mesmo seja enviado ao firebase
     */
    private boolean RecuperandoDadosdaView() {
        boolean retorno = false;
        if (textdata.getText().toString().isEmpty()) {
            layoutdata.setError("Data não pode ser Vazia.");
        }
        if (textvalor.getText().toString().isEmpty()) {
            layoutvalor.setError("Valor não pode ser vazio.");
        }
        if (texttitulo.getText().toString().isEmpty()) {
            textInptitulo.setError("Titulo não pode ser vazio.");
        }

        if (!texttitulo.getText().toString().isEmpty() && !textvalor.getText().toString().isEmpty() && !textdata.getText().toString().isEmpty()) {
            retorno = true;
        }


        return retorno;
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
     * metodo responsavel por fazer uma busca na lista de cartoes e de bancos comparando o nome selecionado pelo
     * usuario na spinner e retornando outra string informando se foi banco ou cartao a forma de pagamento selecionada.
     *
     * @param nomeopFinanceira nome selecionado no spinner pela view
     * @return banco ou cartao.
     */
    private String verificaOpcaoFinanceira(String nomeopFinanceira) {
        Boolean resultBusca = false; // flag para verificar s efoi encontrado
        String retorno = null; // variavel de retorno

        //percorrendo a lista de cartoes e comparando todos os nomes  com o recbido como paramentro
        for (Cartao a : cartaoList) {
            resultBusca = a.getTituloCartao().equalsIgnoreCase(nomeopFinanceira); // retorna true ou false
            if (resultBusca) {// caso seja true retorna a palavra cartao
                retorno = "cartao";
            }
        }
        // se o resultado ainda nao foi encontrado percorre a lista de contas bancarias
        if (!resultBusca) {
            //percorrendo a lista de cartoes e comparando todos os nomes  com o recbido como paramentro
            for (ContasBancarias d : contasBancariasList) {
                resultBusca = d.getTituloContabanco().equalsIgnoreCase(nomeopFinanceira);// retorna true ou false
                if (resultBusca) { // caso seja true retorna a palavra banco
                    retorno = "banco";
                }
            }

        }
        if (!resultBusca) {
            for (Carteira c : carteiraList) {
                resultBusca = c.getTituloCarteira().equalsIgnoreCase(nomeopFinanceira);
                if (resultBusca) {
                    retorno = "carteira";
                }
            }
        }


        return retorno; // retorno global do metodo
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textdata.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * metodo responsavel por inicializar as configurações do firebase na classe.
     */
    public DatabaseReference configFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // pegando a instancia do banco de dados do firebase
        myreference = firebaseDatabase.getReference().child("financeiro");// definindo qual o pont oque  a referencia do firebase ficara
        return myreference;
    }

    /**
     * metodo de limpar campos
     */
    private void limpar() {
        textdata.setText("");
        textvalor.setText("");
        texttitulo.setText("");
//        spinneropcao.setSelection(0);
        checkFontesDesconhecidas.setChecked(false);
    }


    //region Funcionalidades Botoes de voltar View
    private void VoltarViewAnterior() {
        Intent i = new Intent(LancamentoGrupoActivity.this, TransacoesGrupoActivity.class);
        i.putExtra("idgrupo", Idgrupo);
        startActivity(i);
        finish();
    }

    /**
     * meotdo responsavel por tratar o botão fisico de voltar do celular
     */
    @Override
    public void onBackPressed() {
        VoltarViewAnterior();

    }

    @Override
    public boolean onSupportNavigateUp() {
        VoltarViewAnterior();
        return true;
    }
    //endregion
}
