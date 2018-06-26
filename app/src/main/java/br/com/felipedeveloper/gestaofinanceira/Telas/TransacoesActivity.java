package br.com.felipedeveloper.gestaofinanceira.Telas;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adaptadores.LinhadoTempoPessoalAdapter;
import br.com.felipedeveloper.gestaofinanceira.Modelo.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class TransacoesActivity extends AppCompatActivity {
    @BindView(R.id.recyclertransacoes)
    RecyclerView recyclerViewtran;
    @BindView(R.id.edittotaladicionado)
    TextView totaladiconado;
    @BindView(R.id.edittotalgasto)
    TextView totalGasto;
    @BindView(R.id.editstatusfinal)
    TextView statusfinal;
    @BindView(R.id.toolbarcontet)
    Toolbar toolbar;
    @BindView(R.id.floatfiltro)
    FloatingActionButton floatingActionFiltro;
    Context context;
    String mes[];
    int filtroMes = 0;
    private List<Lancamento> list;
    private List<Lancamento> lancamentoList;
    private LinhadoTempoPessoalAdapter adapterLinhadoTempo;
    private DatabaseReference myreference;
    private FirebaseUser firebaseUser;
    private DecimalFormat df;
    private String nomeOpfinanceira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transações");
        context = TransacoesActivity.this;
        list = new ArrayList<>();
        df = new DecimalFormat("#,###,##");
//        RelativeLayout item = (RelativeLayout)findViewById(R.id.item);

//        item.addView(child);
        mes = getResources().getStringArray(R.array.meses);
        final ArrayAdapter<String> mesadapter = new ArrayAdapter<String>(TransacoesActivity.this, android.R.layout.simple_list_item_1, mes);// passando a lista de titulos para
        // serem exibidos no modelo padrao de lista


        nomeOpfinanceira = retornoNomeGrupo(savedInstanceState);

        if (nomeOpfinanceira == null) {
            nomeOpfinanceira = "Transacoesgeral";
        }


        configFirebase();

        adapterLinhadoTempo = new LinhadoTempoPessoalAdapter(TransacoesActivity.this, nomeOpfinanceira);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TransacoesActivity.this);
        recyclerViewtran.setLayoutManager(layoutManager);
        recyclerViewtran.setAdapter(adapterLinhadoTempo);

        floatingActionFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View child = getLayoutInflater().inflate(R.layout.filtro_dialog, null);
                final Spinner spinner = child.findViewById(R.id.spinermes);
                spinner.setAdapter(mesadapter);


                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(TransacoesActivity.this, SweetAlertDialog.NORMAL_TYPE);
                sweetAlertDialog.setTitleText("Filtro");
                sweetAlertDialog.setConfirmText("Filtrar");
                sweetAlertDialog.setCustomView(child);
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        adapterLinhadoTempo.removeItem();
                        filtroMes = spinner.getSelectedItemPosition();

                        sweetAlertDialog.dismiss();
                        BuscarTransacoes();

                    }
                });
                sweetAlertDialog.show();


            }


        });

        /**
         * Metodo responsavel por exibir ou não o botão de filtro
         * caso a tela seja rolada para cima o botão perde a visibilidade
         */
        recyclerViewtran.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) { // posição inicial do recycler view
                    // Scroll Down
                    if (floatingActionFiltro.isShown()) {
                        floatingActionFiltro.hide(); // tirando a visibilidade
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!floatingActionFiltro.isShown()) {
                        floatingActionFiltro.show(); // fazendo o botão aparecer
                    }
                }
            }
        });
        BuscarTransacoes();

    }


    private void BuscarTransacoes() {

        myreference.child("lancamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                Lancamento lancamento;
                for (DataSnapshot dd : dataSnapshot.getChildren()) {
                    lancamento = dd.getValue(Lancamento.class);
                    list.add(lancamento);


                    String ac[] = lancamento.getData().split("/");
                    int mesDaOperacao = Integer.parseInt(ac[1]);

                    if (nomeOpfinanceira.equals("Transacoesgeral")) { // constante passada pela view de transacoes para diferenciar da view (Cartao, carteira ,banco)
                        if (filtroMes == 0) {
                            adapterLinhadoTempo.addItemm(lancamento);
                        }
                        if (mesDaOperacao == (filtroMes)) {
                            adapterLinhadoTempo.addItemm(lancamento);
                        }

                    } else {
                        if (lancamento.getNomeopFinanceira().equals(nomeOpfinanceira)) {


                            if (filtroMes == 0) {
                                adapterLinhadoTempo.addItemm(lancamento);
                            }
                            if (mesDaOperacao == (filtroMes)) {
                                adapterLinhadoTempo.addItemm(lancamento);
                            }

                        }
                    }

                }
                totalGastoPeriodo(list);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void totalGastoPeriodo(List<Lancamento> a) {
         Double totalstatus = 0.0;
         Double totaladcionado = 0.0;
         Double totalgasto = 0.0;
        limparDetalhesOperecoes();

        if (!a.isEmpty()) {
            for (Lancamento lan : a) {

                String data = lan.getData();
                String mesop[] = data.split("/");
                int me = Integer.parseInt(mesop[1]);

                if (filtroMes == 0) {
                    if (lan.getNomeopFinanceira().equals(nomeOpfinanceira)) {
                        limparDetalhesOperecoes();
                        if (lan.getStatusOp() == 1) {
                            totaladcionado = totaladcionado + lan.getValor();
                        } else {
                            totalgasto = totalgasto - lan.getValor();
                        }

                    } else if (nomeOpfinanceira.equals("Transacoesgeral")) {
                        limparDetalhesOperecoes();
                        if (lan.getStatusOp() == 1) {
                            totaladcionado = totaladcionado + lan.getValor();
                        } else {
                            totalgasto = totalgasto - lan.getValor();
                        }
                    }

                    totalstatus = totaladcionado + totalgasto;
                    totalGasto.setText("Debito: " + String.valueOf(df.format(totalgasto)));
                    totaladiconado.setText("Credito: " + String.valueOf(df.format(totaladcionado)));
                    statusfinal.setText("Liquido: " + String.valueOf(df.format(totalstatus)));

                } else if (filtroMes == (me)) {

                    if (lan.getNomeopFinanceira().equals(nomeOpfinanceira)) {
                        limparDetalhesOperecoes();
                        if (lan.getStatusOp() == 1) {
                            totaladcionado = totaladcionado + lan.getValor();
                        } else {
                            totalgasto = totalgasto - lan.getValor();
                        }
                    } else if (nomeOpfinanceira.equals("Transacoesgeral")) {
                        limparDetalhesOperecoes();
                        if (lan.getStatusOp() == 1) {
                            totaladcionado = totaladcionado + lan.getValor();
                        } else {
                            totalgasto = totalgasto - lan.getValor();
                        }
                    }

                    totalstatus = totaladcionado + totalgasto;
                    totalGasto.setText("Debito: " + String.valueOf(df.format(totalgasto)));
                    totaladiconado.setText("Credito: " + String.valueOf(df.format(totaladcionado)));
                    statusfinal.setText("Liquido: " + String.valueOf(df.format(totalstatus)));

                }
            }
        }
    }

    private void limparDetalhesOperecoes() {
        totalGasto.setText("");
        totaladiconado.setText("");
        statusfinal.setText("");

    }

    private void configFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myreference = firebaseDatabase.getReference().child("financeiro").child(firebaseUser.getUid());

    }

    // recuperando os dados passados da activity anterior
    // neste caso o nome do grupo para ser exibido na toolbar
    private String retornoNomeGrupo(Bundle savedInstanceState) {
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("nomeop");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("nomeop");
        }
        return newString;
    }


}
