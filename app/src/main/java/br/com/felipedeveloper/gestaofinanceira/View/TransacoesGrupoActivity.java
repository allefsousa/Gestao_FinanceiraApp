package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.LinhadoTempoGrupoAdapter;
import br.com.felipedeveloper.gestaofinanceira.Model.LancamentoGrupo;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TransacoesGrupoActivity extends AppCompatActivity {
    @BindView(R.id.recyclergrupotrans)
    RecyclerView recyclerViewtranGrupo;
    @BindView(R.id.edittotaladicionado)
    TextView totaladiconado;
    @BindView(R.id.edittotalgasto)
    TextView totalGasto;
    @BindView(R.id.editstatusfinal)
    TextView statusfinal;
    List<LancamentoGrupo> list;
    @BindView(R.id.toolbargrupo)
    Toolbar toolbarGrupo;
    @BindView(R.id.floatlancamentogrupo)
    FloatingActionButton floatingActionButton;

    Context context = TransacoesGrupoActivity.this;
    private LinhadoTempoGrupoAdapter adapterLinhadoTempo;
    private DatabaseReference myreference;
    private String nomeGrupo;
    private DecimalFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes_grupo);
        ButterKnife.bind(this);
         nomeGrupo = retornoNomeGrupo(savedInstanceState);

        setSupportActionBar(toolbarGrupo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transações");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransacoesGrupoActivity.this,LancamentoGrupoActivity.class);
                i.putExtra("idgrupo",nomeGrupo);
                startActivity(i);
                finish();
            }
        });
        configFirebase();
        list = new ArrayList<>();
        df = new DecimalFormat("#,###,##");
        adapterLinhadoTempo = new LinhadoTempoGrupoAdapter(context,nomeGrupo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewtranGrupo.setLayoutManager(layoutManager);
        recyclerViewtranGrupo.setAdapter(adapterLinhadoTempo);

        myreference.child("lancamentos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapterLinhadoTempo.addItem(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapterLinhadoTempo.addItem(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapterLinhadoTempo.removeItem(dataSnapshot);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myreference.child("lancamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LancamentoGrupo lancamentoGrupo = snapshot.getValue(LancamentoGrupo.class);
                    if (lancamentoGrupo.getNomeGrupo().equals(nomeGrupo)){
                        list.add(lancamentoGrupo);
                    }

                }
                totalGastoPeriodo(list);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**
         * Metodo responsavel por exibir ou não o botão de filtro
         * caso a tela seja rolada para cima o botão perde a visibilidade
         */
        recyclerViewtranGrupo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) { // posição inicial do recycler view
                    // Scroll Down
                    if (floatingActionButton.isShown()) {
                        floatingActionButton.hide(); // tirando a visibilidade
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!floatingActionButton.isShown()) {
                        floatingActionButton.show(); // fazendo o botão aparecer
                    }
                }
            }
        });



    }

    private void totalGastoPeriodo(List<LancamentoGrupo> a) {
        Double totalstatus;
        Double totaladcionado = 0.0;
        Double totalgasto = 0.0;
        if (!a.isEmpty())
            for (LancamentoGrupo lan : a) {

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

    private void configFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myreference = firebaseDatabase.getReference().child("financeiro").child("grupos").child(nomeGrupo);

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
                newString = extras.getString("idgrupo");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("idgrupo");
        }
        return newString;
    }

    @Override
    protected void onResume() {
        super.onResume();




    }

    @Override
    protected void onRestart() {
        super.onRestart();



    }
}
