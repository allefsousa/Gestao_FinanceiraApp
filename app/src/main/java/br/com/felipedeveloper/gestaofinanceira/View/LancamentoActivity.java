package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Helper.CreditoDebitoEnum;
import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LancamentoActivity extends AppCompatActivity {
    //region ELEMENTOS DA VIEW
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
    TextInputLayout layouttitulo;
    @BindView(R.id.textedittitulo)
    EditText texttitulo;
    @BindView(R.id.floatingconfirmar)
    FloatingActionButton btconfirmar;
    @BindView(R.id.spinneropbancaria)
    Spinner spinneropcao;
    @BindColor(R.color.vermelho)
    int riplecolor;
    //endregion


    //region VariaveisGlobais
    private Lancamento lancamento;
    private List<ContasBancarias> contasBancariasList;
    private List<Cartao> cartaoList;
    private List<Carteira> carteiraList;
    private List<String> financeiroSpinnerlist;
    private DataSnapshot globalSnapshot;
    private String nomeopFinanceira;
    private DatabaseReference myreference;
    private Calendar myCalendar;
    private FirebaseUser firebaseUser;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lançamentos");
        ButterKnife.bind(this);
        configFirebase();
        myCalendar = Calendar.getInstance();
        final android.support.v7.widget.SwitchCompat aSwitch = findViewById(R.id.switchaaddvalor);
        InitObjetos();
        //region Tratamento de labels de Erros
        textvalor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutvalor.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        texttitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layouttitulo.setError("");
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
                layoutdata.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion  exi
        textvalor.addTextChangedListener(onTextChangedListener());


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
                String titulosopcaofinanceira;
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
                carteiraList.clear();
                financeiroSpinnerlist.clear();
                financeiroSpinnerlist.add("Forma de Pagamento"); // adicionando primeiro elemento do spinner


                /**
                 * Iterando o snapshot do firebase no nó ("banco") e passando a UUID do usuario logado para trazer somente os seus bancos.
                 *
                 */
                for (DataSnapshot d : dataSnapshot.child("banco").getChildren()) {
                    bancarias = d.getValue(ContasBancarias.class);
                    if (bancarias != null) { // se a variavel bancarias nao for vazia adiciono no arrayLIst de bancos para serem exibidos no spinner.
                        titulosopcaofinanceira = bancarias.getTituloContabanco();// capturando o nome dos bancos do usuario logado
                        financeiroSpinnerlist.add(titulosopcaofinanceira); //  adicionando somente o titulo dos bancos na lista de strings
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
                        titulosopcaofinanceira = card.getTituloCartao(); // recuperando somente o nome dos cartoes  do usuario
                        financeiroSpinnerlist.add(titulosopcaofinanceira); // adicionando o nome do cartao na mesma lista de string para exibilas no spinner
                    }
                    cartaoList.add(card); // adicionando o objeto cartão a lista de cartoes

                }

                for (DataSnapshot d : dataSnapshot.child("carteira").getChildren()) {
                    carteira = d.getValue(Carteira.class);// objeto de Carteira que recebe a carteira do firebase
                    if (carteira != null) {
                        titulosopcaofinanceira = carteira.getTituloCarteira(); // recuperando somente o nome dos das carteiras  do usuario
                        financeiroSpinnerlist.add(titulosopcaofinanceira); // adicionando o nome da carteira na mesma lista de string para exibilas no spinner
                    }
                    carteiraList.add(carteira); // adicionando o objeto Carteira a lista de carteiras

                }


                /**
                 * configurando o adapter de string para adicionar os ttulos das formas de pagamentos.
                 */
                ArrayAdapter<String> mesadapter = new ArrayAdapter<String>(LancamentoActivity.this, android.R.layout.simple_list_item_1, financeiroSpinnerlist);// passando a lista de titulos para
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
                    aSwitch.setText("Lançamento Credito");
                    aSwitch.setTextColor(getResources().getColor(R.color.colorSwitchcredito));
                } else {
                    aSwitch.setText("Lançamento Debito");
                    aSwitch.setTextColor(getResources().getColor(R.color.colorSwitchdebito));
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
                    new DatePickerDialog(LancamentoActivity.this, date, myCalendar
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
                    ((InputMethodManager) LancamentoActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            texttitulo.getWindowToken(), 0);
                    texttitulo.clearFocus();
                }
                return false;
            }
        });
        btconfirmar.setRippleColor(riplecolor);

        /**
         * listner do botão de confirmar a operação de credito ou debito;
         */
        //region Botão de confirmar
        btconfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ok = RecuperandoDadosdaView();// recuperando oque esta digitado nos campos colocar


                if (ok) {
                    final Long a = criaTimeStamp(); // recuperando o timestamp criado
                    lancamento.setCreatedAt(a);// atribuindo o timestamp ao objeto lançamento que sera enviado a firebase
                    /**
                     * verificando se a opçao de deposito é para conta corrente ou cartao.
                     */
                    nomeopFinanceira = spinneropcao.getSelectedItem().toString();// recebendo o texto selecionado do spinner
                    String ret = verificaOpcaoFinanceira(nomeopFinanceira);
                    if (!ret.isEmpty()) {
//
//                        if (ret.equals("cartao")) {
//                              debitoCartao(ret);
//                        } else if (ret.equals("banco")) {
//                              debitoBanco(ret);
//                        } else if (ret.equals("carteira")){
//                            debitoCarteira(ret);
//                        }


                        lancamento.setNomeopFinanceira(spinneropcao.getSelectedItem().toString());


                        /**
                         * verificando se o switch esta selecionado ou nao.
                         * neste caso se é CREDITO ou DEBITO a operação a ser feita
                         */
                        if (aSwitch.isChecked()) {
                            lancamento.setStatusOp(CreditoDebitoEnum.Credito.getValor());// passando o 1 para o objeto lançamento para posteriormente saber qual lançamento foi credito ou debito

                            /**
                             * codigo responsavel por enviar o objeto lançamento para o firebase
                             */
                            salvarLancamentoFirebase(lancamento);

                            // Metodos para adicionar credito ao cartao ou banco
                            creditoCartao(ret);
                            creditoBanco(ret);
                            creditoCarteira(ret);


                        } else {
                            lancamento.setStatusOp(CreditoDebitoEnum.Debito.getValor());
                            salvarLancamentoFirebase(lancamento);
                            // Metodos para adicionar Debito ao cartao ou banco
                            debitoCartao(ret);
                            debitoBanco(ret);
                            debitoCarteira(ret);
//                            ExibirMensagem(LancamentoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Debito Realizado.");


                        }
                    } else {
                        ExibirMensagem(LancamentoActivity.this, SweetAlertDialog.ERROR_TYPE, "Erro !! Tente novamente");
                    }
                }
            }
        });
        //endregion

    }


    private void InitObjetos() {
        contasBancariasList = new ArrayList<>();
        financeiroSpinnerlist = new ArrayList<>();
        carteiraList = new ArrayList<>();
        cartaoList = new ArrayList<>();
        lancamento = new Lancamento();

    }

    private String debitoCarteira(String ret) {
        String retorno = null;
        if (ret.equals("carteira")) {

            for (Carteira b : carteiraList) {
                if (b.getTituloCarteira().equals(nomeopFinanceira)) {
                    Carteira aux;
                    aux = b;
                    Carteira cardatualiza = globalSnapshot.child(ret).child(aux.getIdCarteira()).getValue(Carteira.class);
                    Map<String, Object> rec = cardatualiza.MapCarteiraDebito(cardatualiza, lancamento.getValor());
                    if (rec != null) {
                        atualizaSaldoOpFinanceira(ret, cardatualiza.getIdCarteira(), rec, false);
                        retorno = cardatualiza.getTituloCarteira();
                    } else { // TODO: 21/05/2018 analisar dupla menssage ao adiconar dinheiro
                        ExibirMensagem(LancamentoActivity.this, SweetAlertDialog.ERROR_TYPE, "Conta não possui Saldo Sulficiente!");
                    }

                }

            }

        }
        return retorno;
    }

    private String debitoCartao(String ret) {
        String retorno = null;
        if (ret.equals("cartao")) {

            for (Cartao b : cartaoList) {
                if (b.getTituloCartao().equals(nomeopFinanceira)) {
                    Cartao aux;
                    aux = b;
                    Cartao cardatualiza = globalSnapshot.child(ret).child(aux.getIdcartao()).getValue(Cartao.class);
                    Map<String, Object> rec = cardatualiza.MapcartaoDebito(cardatualiza, lancamento.getValor());
                    if (rec != null) {
                        atualizaSaldoOpFinanceira(ret, cardatualiza.getIdcartao(), rec, false);
                        retorno = cardatualiza.getTituloCartao();
                    } else { // TODO: 21/05/2018 analisar dupla menssage ao adiconar dinheiro
                        ExibirMensagem(LancamentoActivity.this, SweetAlertDialog.ERROR_TYPE, "Conta não possui Saldo Sulficiente!");
                    }

                }

            }

        }
        return retorno;
    }

    private String debitoBanco(String ret) {
        String retorno = null;
        if (ret.equals("banco")) {
            for (ContasBancarias contas : contasBancariasList) {
                if (contas.getTituloContabanco().equals(nomeopFinanceira)) {
                    ContasBancarias bancarias;
                    bancarias = contas;
                    ContasBancarias ban = globalSnapshot.child(ret).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                    Map<String, Object> rec = ban.MapBancoDebita(ban, lancamento.getValor());
                    if (rec != null) {
                        atualizaSaldoOpFinanceira(ret, bancarias.getIdContaBanco(), rec, false);
                        retorno = ban.getTituloContabanco();
                    } else {
                        ExibirMensagem(LancamentoActivity.this, SweetAlertDialog.ERROR_TYPE, "Conta não possui Saldo Sulficiente!");
                    }


                }

            }
        }
        return retorno;
    }

    private void creditoCartao(String opFinanceira) {

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
                        atualizaSaldoOpFinanceira(opFinanceira, cardatualiza.getIdcartao(), Mapsaldoatualizado, true);
                    }


                }

            }

        }
    }

    private void creditoBanco(String ret) {

        //caso a opcao financeira selecionada seja cartao a lista de cartoes sera percorrida até el ser encontrado
        if (ret.equals("banco")) {
            for (ContasBancarias contas : contasBancariasList) {
                if (contas.getTituloContabanco().equals(nomeopFinanceira)) {
                    ContasBancarias bancarias;
                    bancarias = contas;
                    ContasBancarias contBancos = globalSnapshot.child(ret).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                    Map<String, Object> rec = contBancos.MapBancoCredita(contBancos, lancamento.getValor());
                    if (rec != null) {

                        atualizaSaldoOpFinanceira(ret, bancarias.getIdContaBanco(), rec, true);
                    }


                }

            }
        }


    }

    private void creditoCarteira(String ret) {

        //caso a opcao financeira selecionada seja cartao a lista de cartoes sera percorrida até el ser encontrado
        if (ret.equals("carteira")) {
            for (Carteira carteira : carteiraList) {
                if (carteira.getTituloCarteira().equals(nomeopFinanceira)) {
                    Carteira car;
                    car = carteira;
                    Carteira carteiraBD = globalSnapshot.child(ret).child(car.getIdCarteira()).getValue(Carteira.class);
                    Map<String, Object> rec = carteiraBD.MapCarteiraCredito(carteiraBD, lancamento.getValor());
                    if (rec != null) {
                        atualizaSaldoOpFinanceira(ret, car.getIdCarteira(), rec, true);
                    }


                }

            }
        }


    }

    /**
     * Metodo responsavel por atualizar os valores do BANCO e do CARTÃO de credito.
     * tando credito quanto debito
     *
     * @param opFinanceira       nome do nó .EX ou cartao ou banco
     * @param idOpFinanceira     id do cartao ou do banco
     * @param mapsaldoatualizado hash map do banco ou cartao com os dados atuaalizados para update
     * @param debitoCredito      variavel booleana para fazer tratamento da mensagem
     */
    private void atualizaSaldoOpFinanceira(String opFinanceira, String idOpFinanceira, Map<String, Object> mapsaldoatualizado, final Boolean debitoCredito) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(); // recuperando uma nova referenciaa do banco de dados
        reference.child("financeiro").child(firebaseUser.getUid()).child(opFinanceira).child(idOpFinanceira).updateChildren(mapsaldoatualizado).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (debitoCredito) {
                    ExibirMensagem(LancamentoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Credito Adicionado !"); // mensagme de sucesso.
                } else {
                    ExibirMensagem(LancamentoActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Debito Realizado !");
                }
            }
        });
    }


    /**
     * metodo reponsavel por salvar o lançamento no firebase no nó ("lancamentoGrupo")
     *
     * @param ll é o lançamento que foi digitado na tela pelo usuario. neste momento o envio ao firebase é feito
     */
    private void salvarLancamentoFirebase(Lancamento ll) {
        myreference.child("lancamentos").child(UUID.randomUUID().toString()).setValue(ll).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                limpar();// limpando a view apos inserir os dados
            }
        });
    }

    /**
     * atribuindo os dados informados na view  a um objeto lançamento que fara a movimentação dos dados
     * até que o mesmo seja enviado ao firebase
     */
    private boolean RecuperandoDadosdaView() {
        boolean todosPreenchidos = false;

        if (textdata.getText() == null || textdata.getText().toString().isEmpty()) {
            layoutdata.setErrorEnabled(true);
            layoutdata.setError("Escolha uma data !");
        }
        if (textvalor.getText().toString().isEmpty() || textvalor.getText() == null) {
            layoutvalor.setErrorEnabled(true);
            layoutvalor.setError("Informe um valor !");
        }

        if (texttitulo.getText().toString().isEmpty() || texttitulo.getText() == null) {
            layouttitulo.setErrorEnabled(true);
            layouttitulo.setError("Informe o titulo !");
        }

        if (spinneropcao.getSelectedItemPosition() == 0) {

            ExibirMensagem(LancamentoActivity.this, SweetAlertDialog.ERROR_TYPE, "Lançamento não realizado. \nEscolha uma Forma de Pagamento !");
        }
        if (!textdata.getText().toString().isEmpty() && !textvalor.getText().toString().isEmpty() && !texttitulo.getText().toString().isEmpty() && spinneropcao.getSelectedItemPosition() != 0) {
            todosPreenchidos = true;
            String virgulaponto;
            lancamento.setData(textdata.getText().toString());
            lancamento.setTitulo(texttitulo.getText().toString());
            virgulaponto = textvalor.getText().toString().replaceAll(",", "");
            lancamento.setValor(Double.parseDouble(virgulaponto));
            lancamento.setStatusOp(spinneropcao.getSelectedItemPosition());
        }


        return todosPreenchidos;
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
        // se o resultado ainda nao foi encontrado percorre a lista de carteiras
        if (!resultBusca) {
            //percorrendo a lista de carteiras e comparando todos os nomes  com o recbido como paramentro
            for (Carteira d : carteiraList) {
                resultBusca = d.getTituloCarteira().equalsIgnoreCase(nomeopFinanceira);// retorna true ou false
                if (resultBusca) { // caso seja true retorna a palavra carteiras
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
        myreference = firebaseDatabase.getReference().child("financeiro").child(firebaseUser.getUid());// definindo qual o pont oque  a referencia do firebase ficara
        return myreference;
    }

    /**
     * metodo de limpar campos
     */
    private void limpar() {
        textdata.setText("");
        textvalor.getText().clear();
        texttitulo.setText("");
        spinneropcao.setSelection(0);

    }


    /**
     * metodo pré configurado para exibir as menssagens assim reduzindo o codigo escrito em todas as chamadas
     *
     * @param context
     * @param successType
     * @param s
     */
    private void ExibirMensagem(Context context, int successType, String s) {
        new SweetAlertDialog(context, successType)
                .setTitleText(getResources().getString(R.string.app_name))
                .setContentText(s)
                .show();
    }

    /**
     * meotdo responsavel por tratar o botão fisico de voltar do celular
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    /**
     * metodo responsavel por tratar o botão de voltar a tela anterior
     * neste caso fecha a view atual quando o botão é precionado
     *
     * @return
     */
    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    /**
     * metodo responsavel por  formatar o campo de valor e ja ir adiconando as virgulas
     *
     * @return
     */
    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textvalor.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,##");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    textvalor.setText(formattedString);
                    textvalor.setSelection(textvalor.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                textvalor.addTextChangedListener(this);
            }
        };
    }
}
