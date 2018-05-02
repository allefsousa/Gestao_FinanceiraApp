package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CarteiraActivity extends AppCompatActivity {
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
    Carteira carteira;
    String tagCarteira[];
    List<Carteira> arrayValorPositivo;
    List<Carteira> arrayValorNegativo;
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
        setContentView(R.layout.activity_carteiira);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        carteira = new Carteira();
        tagCarteira = getResources().getStringArray(R.array.tagcarteira);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ButterKnife.bind(this);
        configFirebase();
        myCalendar = Calendar.getInstance();
        final android.support.v7.widget.SwitchCompat aSwitch = (android.support.v7.widget.SwitchCompat) findViewById(R.id.switchaaddvalor);
        arrayValorNegativo = new ArrayList<>();
        arrayValorPositivo = new ArrayList<>();
        contasBancariasList = new ArrayList<>();
        financeiroSpinnerlist = new ArrayList<>();
        financeiroSpinnerlist.add("Forma de Pagamento");
        cartaoList = new ArrayList<>();


        myreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Carteira carte;
                Cartao card;
                String titulosCardConta;
                ContasBancarias bancarias;
                globalSnapshot = dataSnapshot;
                for (DataSnapshot d : dataSnapshot.child("banco").child(firebaseUser.getUid()).getChildren()) {
                    bancarias = d.getValue(ContasBancarias.class);
                    if (bancarias != null) {
                        titulosCardConta = bancarias.getTituloContabanco();
                        financeiroSpinnerlist.add(titulosCardConta);
                    }
                    contasBancariasList.add(bancarias);
                }
                for (DataSnapshot d : dataSnapshot.child("carteira").child(firebaseUser.getUid()).getChildren()) {

                }
                for (DataSnapshot d : dataSnapshot.child("cartao").child(firebaseUser.getUid()).getChildren()) {
                    card = d.getValue(Cartao.class);
                    if (card != null) {
                        titulosCardConta = card.getTituloCartao();
                        financeiroSpinnerlist.add(titulosCardConta);
                    }
                    cartaoList.add(card);

                }

                ArrayAdapter<String> mesadapter = new ArrayAdapter<String>(CarteiraActivity.this, android.R.layout.simple_list_item_1, financeiroSpinnerlist);
                mesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinneropcao.setAdapter(mesadapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };


        textdata.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(CarteiraActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }


            }
        });
        btconfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carteira.setData(textdata.getText().toString());
                carteira.setTitulo(texttitulo.getText().toString());
                carteira.setValor(Double.parseDouble(textvalor.getText().toString()));
                /**
                 * verificando se a opçao de deposito é para conta corrente ou cartao.
                 */
                final String nomeopFinanceira = spinneropcao.getSelectedItem().toString();
                if (aSwitch.isChecked()) {
                    carteira.setStatusOp(1);
                    myreference.child("movimentacao").child(firebaseUser.getUid()).child(UUID.randomUUID().toString()).setValue(carteira).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                Map<String, Object> rec = cardatualiza.MapCartaoCredito(cardatualiza, carteira.getValor());
                                if (rec != null){
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("financeiro").child(ret).child(firebaseUser.getUid()).child(cardatualiza.getIdcartao()).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ExibirMensagem(CarteiraActivity.this,SweetAlertDialog.SUCCESS_TYPE,"Valor adicionado com Sucesso");

                                        }
                                    });
                                }else {
                                    ExibirMensagem(CarteiraActivity.this,SweetAlertDialog.ERROR_TYPE,"Conta não possui Saldo Sulficiente !");

                                }


                            }

                        }
                    }else if (ret.equals("banco")){
                        for (ContasBancarias contas : contasBancariasList) {
                            if (contas.getTituloContabanco().equals(nomeopFinanceira)) {
                                ContasBancarias bancarias;
                                bancarias = contas;
                                ContasBancarias contBancos = globalSnapshot.child(ret).child(firebaseUser.getUid()).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                                Map<String, Object> rec = contBancos.MapBancoCredita(contBancos, carteira.getValor());
                                if (rec != null){
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("financeiro").child(ret).child(firebaseUser.getUid()).child(contBancos.getIdContaBanco()).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ExibirMensagem(CarteiraActivity.this,SweetAlertDialog.SUCCESS_TYPE,"Valor adicionado com Sucesso");

                                        }
                                    });
                                }else {
                                    ExibirMensagem(CarteiraActivity.this,SweetAlertDialog.ERROR_TYPE,"Conta não possui Saldo Sulficiente !");

                                }


                            }

                        }
                    }
                } else {
                    carteira.setStatusOp(0);
                    myreference.child("movimentacao").child(firebaseUser.getUid()).child(UUID.randomUUID().toString()).setValue(carteira).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                Map<String, Object> rec = cardatualiza.MapcartaoDebito(cardatualiza, carteira.getValor());
                                if (rec != null){
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("financeiro").child(ret).child(firebaseUser.getUid()).child(cardatualiza.getIdcartao()).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ExibirMensagem(CarteiraActivity.this,SweetAlertDialog.SUCCESS_TYPE,"Debito lançado com sucesso !");

                                        }
                                    });
                                }else {
                                    ExibirMensagem(CarteiraActivity.this,SweetAlertDialog.ERROR_TYPE,"Conta não possui Saldo Sulficiente !");
                                }

                            }

                        }
                    }else if (ret.equals("banco")){
                        for (ContasBancarias contas : contasBancariasList) {
                            if (contas.getTituloContabanco().equals(nomeopFinanceira)) {
                                ContasBancarias bancarias;
                                bancarias = contas;
                                ContasBancarias ban = globalSnapshot.child(ret).child(firebaseUser.getUid()).child(bancarias.getIdContaBanco()).getValue(ContasBancarias.class);
                                Map<String, Object> rec = ban.MapBancoDebita(ban, carteira.getValor());
                                if (rec != null){
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("financeiro").child(ret).child(firebaseUser.getUid()).child(ban.getIdContaBanco()).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ExibirMensagem(CarteiraActivity.this,SweetAlertDialog.SUCCESS_TYPE,"Debito lançado com sucesso !");
                                        }
                                    });
                                }else {
                                    ExibirMensagem(CarteiraActivity.this,SweetAlertDialog.ERROR_TYPE,"Conta não possui Saldo Sulficiente !");
                                }


                            }

                        }
                    }

                }

            }
        });


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
    }
    private void ExibirMensagem(Context context, int successType, String s) {
        new SweetAlertDialog(context, successType)
                .setTitleText(getResources().getString(R.string.app_name))
                .setContentText(s)
                .show();
    }

}
