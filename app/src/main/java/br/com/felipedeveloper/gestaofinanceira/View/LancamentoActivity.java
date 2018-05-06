package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LancamentoActivity extends AppCompatActivity {
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
    @BindView(R.id.btnconfirmar)
    Button btconfirmar;
    @BindView(R.id.spinneropbancaria)
    Spinner spinneropcao;
    Lancamento lancamento;
    String tagCarteira[];

    List<ContasBancarias> contasBancariasList;
    List<Cartao> cartaoList;
    List<String> financeiroSpinnerlist;
    Cartao card = new Cartao();
    DataSnapshot globalSnapshot;
    private DatabaseReference myreference;
    private Calendar myCalendar;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lançamentos");
        lancamento = new Lancamento();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ButterKnife.bind(this);
        configFirebase();
        myCalendar = Calendar.getInstance();
        final android.support.v7.widget.SwitchCompat aSwitch = findViewById(R.id.switchaaddvalor);
        contasBancariasList = new ArrayList<>();
        financeiroSpinnerlist = new ArrayList<>();
        cartaoList = new ArrayList<>();

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
                financeiroSpinnerlist.add("Forma de Pagamento"); // adicionando primeiro elemento do spinner

                /**
                 * Iterando o snapshot do firebase no nó ("banco") e passando a UUID do usuario logado para trazer somente os seus bancos.
                 *
                 */
                for (DataSnapshot d : dataSnapshot.child("banco").child(firebaseUser.getUid()).getChildren()) {
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
                for (DataSnapshot d : dataSnapshot.child("cartao").child(firebaseUser.getUid()).getChildren()) {
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
                final String nomeopFinanceira = spinneropcao.getSelectedItem().toString();// recebendo o texto selecionado do spinner
                /**
                 * verificando se o switch esta selecionado ou nao.
                 * neste caso se é CREDITO ou DEBITO a operação a ser feita
                 */
                if (aSwitch.isChecked()) {
                    lancamento.setStatusOp(1);// passando o 1 para o objeto lançamento para posteriormente saber qual lançamento foi credito ou debito
                    /**
                     * codigo responsavel por enviar o objeto lançemento para o firebase
                     */
                    salvarLancamentoFirebase(lancamento);


                    String ret = verificaOpcaoFinanceira(nomeopFinanceira);

                    if (ret.equals("cartao")) {
                        for (Cartao b : cartaoList) {
                            if (b.getTituloCartao().equals(nomeopFinanceira)) {
                                Cartao aux;
                                aux = b;
                                Cartao cardatualiza = globalSnapshot.child(ret).child(firebaseUser.getUid()).child(aux.getIdcartao()).getValue(Cartao.class);
                                Map<String, Object> rec = cardatualiza.MapCartaoCredito(cardatualiza, lancamento.getValor());
                                if (rec != null){
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("financeiro").child(ret).child(firebaseUser.getUid()).child(cardatualiza.getIdcartao()).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ExibirMensagem(LancamentoActivity.this,SweetAlertDialog.SUCCESS_TYPE,"Valores Adicionados comSucesso");

                                        }
                                    });
                                }else {
                                    String er = "Conta não possui Saldo sulficiente.";
                                    ExibirMensagem(LancamentoActivity.this,SweetAlertDialog.ERROR_TYPE,er);

                                }


                            }

                        }
                    }else if (ret.equals("banco")){
                        for (ContasBancarias contas : contasBancariasList) {
                            if (contas.getTituloContabanco().equals(nomeopFinanceira)) {
                                ContasBancarias bancarias;
                                bancarias = contas;
                                ContasBancarias contBancos = globalSnapshot.child(ret).child(firebaseUser.getUid()).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                                Map<String, Object> rec = contBancos.MapBancoCredita(contBancos, lancamento.getValor());
                                if (rec != null){
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("financeiro").child(ret).child(firebaseUser.getUid()).child(contBancos.getIdContaBanco()).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ExibirMensagem(LancamentoActivity.this,SweetAlertDialog.SUCCESS_TYPE,"Valores Adicionados com sucesso!");

                                        }
                                    });
                                }else {
                                    ExibirMensagem(LancamentoActivity.this,SweetAlertDialog.ERROR_TYPE,"Conta não possui Saldo Sulficiente!");

                                }


                            }

                        }
                    }
                } else {
                    lancamento.setStatusOp(0);
                    myreference.child("movimentacao").child(firebaseUser.getUid()).child(UUID.randomUUID().toString()).setValue(lancamento).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            limpar();
                        }
                    });
                    String ret = verificaOpcaoFinanceira(nomeopFinanceira);

                    if (ret.equals("cartao")) {
                        for (Cartao b : cartaoList) {
                            if (b.getTituloCartao().equals(nomeopFinanceira)) {
                                Cartao aux;
                                aux = b;
                                Cartao cardatualiza = globalSnapshot.child(ret).child(firebaseUser.getUid()).child(aux.getIdcartao()).getValue(Cartao.class);
                                Map<String, Object> rec = cardatualiza.MapcartaoDebito(cardatualiza, lancamento.getValor());
                                if (rec != null){
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("financeiro").child(ret).child(firebaseUser.getUid()).child(cardatualiza.getIdcartao()).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ExibirMensagem(LancamentoActivity.this,SweetAlertDialog.SUCCESS_TYPE,"Debito lançado com sucesso!");

                                        }
                                    });
                                }else {
                                    ExibirMensagem(LancamentoActivity.this,SweetAlertDialog.ERROR_TYPE,"Conta não possui Saldo Sulficiente!");
                                }

                            }

                        }
                    }else if (ret.equals("banco")){
                        for (ContasBancarias contas : contasBancariasList) {
                            if (contas.getTituloContabanco().equals(nomeopFinanceira)) {
                                ContasBancarias bancarias;
                                bancarias = contas;
                                ContasBancarias ban = globalSnapshot.child(ret).child(firebaseUser.getUid()).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                                Map<String, Object> rec = ban.MapBancoDebita(ban, lancamento.getValor());
                                if (rec != null){
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("financeiro").child(ret).child(firebaseUser.getUid()).child(ban.getIdContaBanco()).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ExibirMensagem(LancamentoActivity.this,SweetAlertDialog.SUCCESS_TYPE,"Debito lançado com Sucesso!");
                                        }
                                    });
                                }else {
                                    ExibirMensagem(LancamentoActivity.this,SweetAlertDialog.ERROR_TYPE,"Conta não possui Saldo Sulficiente!");
                                }


                            }

                        }
                    }

                }

            }
        });


    }

    /**
     * metodo reponsavel por salvar o lançamento no firebase no nó ("lancamento")
     * @param ll é o lançamento que foi digitado na tela pelo usuario. neste momento o envio ao firebase é feito
     */
    private void salvarLancamentoFirebase(Lancamento ll) {
        myreference.child("movimentacao").child(firebaseUser.getUid()).child(UUID.randomUUID().toString()).setValue(ll).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    private void RecuperandoDadosdaView() {
        lancamento.setData(textdata.getText().toString()); // recuperando oque foi digitado no campo de data
        lancamento.setTitulo(texttitulo.getText().toString()); // recuperando oque foi digitado no titulo
        lancamento.setValor(Double.parseDouble(textvalor.getText().toString()));// recupara o valor que foi digitado que entra como String
                                                                                //  e o converte para Double conforme a classe espera
    }

    /**
     * Metodo responsavel por criar o timestamp do lançamento para futuramente ter a precisao de cada lançamento
     * @return timesTamp
     */
    @NonNull
    private Long criaTimeStamp() {
        Calendar cal = Calendar.getInstance();
        Date data_atual = cal.getTime();
        final Timestamp ts = new Timestamp(data_atual.getTime());
        return ts.getTime();
    }

    private String verificaOpcaoFinanceira(String nomeopFinanceira) {
        Boolean resultBusca = false;
        String retorno = null;

        for (Cartao a : cartaoList) {
            resultBusca = a.getTituloCartao().equalsIgnoreCase(nomeopFinanceira);
            if (resultBusca) {
                retorno = "cartao";
            }
        }
        if (!resultBusca) {
            for (ContasBancarias d : contasBancariasList) {
                resultBusca = d.getTituloContabanco().equalsIgnoreCase(nomeopFinanceira);
            }
            if (resultBusca) {
                retorno = "banco";
            }
        }

        return retorno;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textdata.setText(sdf.format(myCalendar.getTime()));
    }

    private void configFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myreference = firebaseDatabase.getReference().child("financeiro");

    }

    private void limpar() {
        textdata.setText("");
        textvalor.setText("");
        texttitulo.setText("");
        spinneropcao.setSelection(0);
    }
    private void ExibirMensagem(Context context, int successType, String s) {
        new SweetAlertDialog(context, successType)
                .setTitleText(getResources().getString(R.string.app_name))
                .setContentText(s)
                .show();
    }

}
