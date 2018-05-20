package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Toast;

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

import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.Model.Grupo;
import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LancamentoGrupoActivity extends BaseActivity {
    DatePickerDialog.OnDateSetListener date;
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
    CheckBox checkBox;
    Lancamento lancamento;


    List<ContasBancarias> contasBancariasList;
    List<Cartao> cartaoList;
    List<String> financeiroSpinnerlist;
    DataSnapshot globalSnapshot;
    String nomeopFinanceira;
    Context context = LancamentoGrupoActivity.this;
    private DatabaseReference myreference;
    private Calendar myCalendar;
    private FirebaseUser firebaseUser;
    private String Idgrupo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento_grupo);
        ButterKnife.bind(this);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lançamentos Grupo");

        Bundle extras = getIntent().getExtras();
        Idgrupo = extras.getString("idgrupo");

        configFirebase();
        myCalendar = Calendar.getInstance();
        final android.support.v7.widget.SwitchCompat aSwitch = findViewById(R.id.switchaaddvalor);
        InitObjetos();


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
                String titulosCardConta;
                ContasBancarias bancarias;
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
                for (DataSnapshot d : dataSnapshot.child("banco").getChildren()) {
                    bancarias = d.getValue(ContasBancarias.class);
                    if (bancarias != null) { // se a variavel bancarias nao for vazia adiciono no arrayLIst de bancos para serem exibidos no spinner.
                        titulosCardConta = bancarias.getTituloContabanco();// capturando o nome dos bancos do usuario logado
                        financeiroSpinnerlist.add(titulosCardConta); //  adicionando somente o titulo dos bancos na lista de strings
                    }
                    contasBancariasList.add(bancarias); // agora adiciono o objeto banco a lista de bancos
                }

                /**
                 * Iterando o snapshot do firebase no nó ("cartao") e passando a UUID do usuario logado para trazer somente os seus cartões.
                 *
                 */
                for (DataSnapshot d : dataSnapshot.child("cartao").getChildren()) {
                    card = d.getValue(Cartao.class);// objeto de cartao que recebe o cartao do firebase
                    if (card != null) {
                        titulosCardConta = card.getTituloCartao(); // recuperando somente o nome dos cartoes  do usuario
                        financeiroSpinnerlist.add(titulosCardConta); // adicionando o nome do cartao na mesma lista de string para exibilas no spinner
                    }
                    cartaoList.add(card); // adicionando o objeto cartão a lista de cartoes

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
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    spinneropcao.setEnabled(false);
                } else {

                    spinneropcao.setEnabled(true);
                }
            }
        });


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
                RecuperandoDadosdaView();// recuperando oque esta digitado nos campos
                final Long a = criaTimeStamp(); // recuperando o timestamp criado
                lancamento.setCreatedAt(a);// atribuindo o timestamp ao objeto lançamento que sera enviado a firebase

                /**
                 * verificando se a opçao de deposito é para conta corrente ou cartao.
                 */
                nomeopFinanceira = spinneropcao.getSelectedItem().toString();// recebendo o texto selecionado do spinner
                String ret = verificaOpcaoFinanceira(nomeopFinanceira);
                // TODO: 15/05/2018 op financeira vazio 


                lancamento.setNomeColaborador(firebaseUser.getDisplayName());// TODO: 16/05/2018 validar vazio
                lancamento.setNomeopFinanceira(nomeopFinanceira);
                Grupo g = globalSnapshot.child("grupo").child(Idgrupo).getValue(Grupo.class);

                /**
                 * verificando se o switch esta selecionado ou nao.
                 * neste caso se é CREDITO ou DEBITO a operação a ser feita
                 */
                if (aSwitch.isChecked()) { // creditar valor no grupo
                    lancamento.setStatusOp(1);// passando o 1 para o objeto lançamento para posteriormente saber qual lançamento foi credito ou debito
                    Map<String, Object> retorno = g.mapCreditaGrupo(g, lancamento.getValor());
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
                            }

                            switch (statusSaldo) {
                                case "DebitocartaoOK":
                                    ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Valores Removidos do cartao e adicionados ao grupo");
                                    salvarLancamentoFirebase(lancamento);
                                    UpdateSaldoGrupo(retorno);
                                    break;

                                case "saldoInsulficientecartao":
                                    ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "O Cartão não possui limite disponivel !");

                                    break;
                                case "DebitocontaoOK":
                                    salvarLancamentoFirebase(lancamento);
                                    UpdateSaldoGrupo(retorno);
                                    ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Valores Removidos da conta e adicionados ao grupo");
                                    break;
                                case "saldoInsulficienteconta":
                                    ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "Conta não possui saldo sulficiente");

                                    break;


                            }

                        }
                    }

                    if (checkBox.isChecked()) { // permitindo fontes desconhecidas remover e adicionar valores

                        if (retorno != null) {
                            /**
                             * codigo responsavel por enviar o objeto lançamento para o firebase
                             */
                            salvarLancamentoFirebase(lancamento);
                            UpdateSaldoGrupo(retorno);

                        }
                    }
                    if (!checkBox.isChecked()) {
                        if (spinneropcao.getSelectedItemPosition() == 0) {
                            ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.WARNING_TYPE, "Selecione uma conta !");
                        }


                    }

                } else {

                    lancamento.setStatusOp(0);
                    if (g.getSaldoGrupo() < lancamento.getValor()) {
                        ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "Operação não realizada.\nO grupo não possui saldo sulficiente !");
                    } else {
                        final Map<String, Object> retorno = g.mapDebitaGrupo(g, lancamento.getValor());
                        if (!lancamento.getNomeopFinanceira().equals("Origem dinheiro")) {
                            String retornoOperacao = null;

                            // so vai entrat aqui quando uma fonte de reda for selecionada
                            if (!ret.isEmpty()) {
                                if (ret.equals("cartao")) {
                                    retornoOperacao = adicionandoCreditoCartao(ret);
                                } else if (ret.equals("banco")) {
                                    retornoOperacao = adicionandoCreditoBanco(ret);
                                }
                                switch (retornoOperacao) {
                                    case "CreditoCartaoOK":
                                        salvarLancamentoFirebase(lancamento);
                                        UpdateSaldoGrupo(retorno);
                                        ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "O valor foi transferido para seu cartão !");

                                        break;
                                    case "CreditoBancoOK":
                                        salvarLancamentoFirebase(lancamento);
                                        UpdateSaldoGrupo(retorno);
                                        ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "O valor foi transferido para sua conta bancaria !");
                                        break;
                                }

                            }

                        }
                        if (checkBox.isChecked()) {
                            //TAVA AQUI
                            //passar o saldo do grupo para comparar com o valor do debito e analisar como foi feito em lancamento
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
                                                salvarLancamentoFirebase(lancamento);
                                                myreference.child("grupo").child(Idgrupo).updateChildren(retorno);

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
                                ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "Saldo Grupo Insulficiente !");
                            }
                        } else if (spinneropcao.getSelectedItemPosition() == 0) {
                            ExibirMensageeem(LancamentoGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "Selecione uma conta para recebimento. !");
                        }


                    }


                }
            }
        });
    }

    private void UpdateSaldoGrupo(Map<String, Object> retorno) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("financeiro").child(firebaseUser.getUid()).child("grupo").child(Idgrupo).updateChildren(retorno);
    }

    private void InitObjetos() {
        contasBancariasList = new ArrayList<>();
        financeiroSpinnerlist = new ArrayList<>();
        cartaoList = new ArrayList<>();
        lancamento = new Lancamento();
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
                    Cartao cardatualiza = globalSnapshot.child(ret).child(aux.getIdcartao()).getValue(Cartao.class);
                    Map<String, Object> rec = cardatualiza.MapcartaoDebito(cardatualiza, lancamento.getValor());
                    if (rec != null) {
                        atualizaSaldoCartaoBancoFirebase(ret, cardatualiza.getIdcartao(), rec);
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
                    ContasBancarias ban = globalSnapshot.child(ret).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                    Map<String, Object> rec = ban.MapBancoDebita(ban, lancamento.getValor());
                    if (rec != null) {
                        atualizaSaldoCartaoBancoFirebase(ret, bancarias.getIdContaBanco(), rec);
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
                    ContasBancarias contBancos = globalSnapshot.child(ret).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                    Map<String, Object> rec = contBancos.MapBancoCredita(contBancos, lancamento.getValor());
                    if (rec != null) {

                        atualizaSaldoCartaoBancoFirebase(ret, bancarias.getIdContaBanco(), rec);
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
                    Cartao cardatualiza = globalSnapshot.child(opFinanceira).child(aux.getIdcartao()).getValue(Cartao.class);


                    /**
                     * Para se atualizar os dados no firebase passar o objeto por enteiro iria o apagar e criar outro com novos dados e Ids
                     * entao a maneira correta é criando um hashMap e passando odmente oque sera atualizado
                     * que nesse caso ´o valor.
                     * o metodo mapcartao de credito  da classe cartao já atualiza os dados. fazendo a soma do valor que tinha e do novo e retornando
                     * um novo hash map.
                     * para atualizar os dados é enviado o valor adicionado no editext da view w o objeto que recebera o valor
                     *
                     */
                    Map<String, Object> Mapsaldoatualizado = cardatualiza.MapCartaoCredito(cardatualiza, lancamento.getValor());

                    if (Mapsaldoatualizado != null) {// tratando erro de saldo que é verificado na classe cartao
                        /**
                         * passando os caminhos e o map que vai atualizar o que esta no firebase
                         */
                        atualizaSaldoCartaoBancoFirebase(opFinanceira, cardatualiza.getIdcartao(), Mapsaldoatualizado);
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
     * @param idcartao           id do cartao ou do banco
     * @param mapsaldoatualizado hash map do banco ou cartao com os dados atuaalizados para update
     *
     */
    public void atualizaSaldoCartaoBancoFirebase(String opFinanceira, String idcartao, Map<String, Object> mapsaldoatualizado) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(); // recuperando uma nova referenciaa do banco de dados
        reference.child("financeiro").child(firebaseUser.getUid()).child(opFinanceira).child(idcartao).updateChildren(mapsaldoatualizado);
    }

    /**
     * metodo reponsavel por salvar o lançamento no firebase no nó ("lancamento")
     *
     * @param ll é o lançamento que foi digitado na tela pelo usuario. neste momento o envio ao firebase é feito
     */
    private void salvarLancamentoFirebase(Lancamento ll) {
        myreference.child("lancamentosgrupo").child(UUID.randomUUID().toString()).setValue(ll);
        limpar();// limpando a view apos inserir os dados

    }

    /**
     * atribuindo os dados informados na view  a um objeto lançamento que fara a movimentação dos dados
     * até que o mesmo seja enviado ao firebase
     */
    private void RecuperandoDadosdaView() {
        // TODO: 05/05/2018 validar dados inpedir nulo e vazio
        lancamento.setData(textdata.getText().toString()); // recuperando oque foi digitado no campo de data
        lancamento.setTitulo(texttitulo.getText().toString()); // recuperando oque foi digitado no titulo
        lancamento.setValor(Double.parseDouble(textvalor.getText().toString()));// recupara o valor que foi digitado que entra como String //  e o converte para Double conforme a classe espera
        lancamento.setNomeGrupo(Idgrupo);
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
        myreference = firebaseDatabase.getReference().child("financeiro").child(firebaseUser.getUid());// definindo qual o pont oque  a referencia do firebase ficara
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
        checkBox.setChecked(false);
    }


    /**
     * meotdo responsavel por tratar o botão fisico de voltar do celular
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    private void ExibirMensageeem(Context context, int successType, String s) {
        new SweetAlertDialog(context, successType)
                .setTitleText(getResources().getString(R.string.app_name))
                .setContentText(s)
                .show();
    }
}
