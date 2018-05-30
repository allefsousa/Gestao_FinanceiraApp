package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import br.com.felipedeveloper.gestaofinanceira.Adapters.LinhadoTempoPessoalAdapter;
import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class TransacoesActivity extends AppCompatActivity {
    @BindView(R.id.recyclertransacoes)
    RecyclerView recyclerViewtran;
    @BindView(R.id.edittotaladicionado)
    TextView totaladiconado;
    @BindView(R.id.edittotalgasto)
    TextView totalGasto;
    @BindView(R.id.editstatusfinal)
    TextView statusfinal;
    Double totalstatus = 0.0;
    Double totaladcionado = 0.0;
    Double totalgasto = 0.0;
    List<Lancamento> list;
    @BindView(R.id.toolbarcontet)
    Toolbar toolbar;
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
        list = new ArrayList<>();
        df = new DecimalFormat("#0.00");

        nomeOpfinanceira = retornoNomeGrupo(savedInstanceState);

        if (nomeOpfinanceira == null) {
            nomeOpfinanceira = "Transacoesgeral";
        }


        configFirebase();


        adapterLinhadoTempo = new LinhadoTempoPessoalAdapter(TransacoesActivity.this, nomeOpfinanceira);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TransacoesActivity.this);
        recyclerViewtran.setLayoutManager(layoutManager);
        recyclerViewtran.setAdapter(adapterLinhadoTempo);

        myreference.child("lancamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Lancamento lancamento;
                for (DataSnapshot dd : dataSnapshot.getChildren()){
                    lancamento = dd.getValue(Lancamento.class);
                    if (nomeOpfinanceira.equals("Transacoesgeral")){
                        adapterLinhadoTempo.addItemm(lancamento);
                    }else {
                        if (lancamento.getNomeopFinanceira().equals(nomeOpfinanceira)){
                            adapterLinhadoTempo.addItemm(lancamento);
                        }
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myreference.child("lancamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Lancamento lancamento = snapshot.getValue(Lancamento.class);
                    list.add(lancamento);
                }
                totalGastoPeriodo(list);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void totalGastoPeriodo(List<Lancamento> a) {
        if (!a.isEmpty()) {
            for (Lancamento lan : a) {
                if (lan.getNomeopFinanceira().equals(nomeOpfinanceira)) {
                    if (lan.getStatusOp() == 1) {
                        totaladcionado = totaladcionado + lan.getValor();
                    } else {
                        totalgasto = totalgasto - lan.getValor();
                    }
                } else if (nomeOpfinanceira.equals("Transacoesgeral")) {
                    if (lan.getStatusOp() == 1) {
                        totaladcionado = totaladcionado + lan.getValor();
                    } else { // TODO: 21/05/2018 refactor
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
