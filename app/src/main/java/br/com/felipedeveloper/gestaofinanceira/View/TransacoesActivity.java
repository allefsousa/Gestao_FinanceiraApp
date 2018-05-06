package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.AdapterLinhadoTempo;
import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class TransacoesActivity extends AppCompatActivity {
    @BindView(R.id.recyclertransacoes)
     RecyclerView recyclerViewtran;
    private AdapterLinhadoTempo adapterLinhadoTempo;
    private DatabaseReference myreference;
    private FirebaseUser firebaseUser;
    @BindView(R.id.edittotaladicionado)
    TextView totaladiconado;
    @BindView(R.id.edittotalgasto)
    TextView totalGasto;
    @BindView(R.id.editstatusfinal)
    TextView statusfinal;
    Double totalstatus=0.0;
    Double totaladcionado=0.0;
    Double totalgasto=0.0;
    List<Lancamento> list ;


    @BindView(R.id.toolbarcontet)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transações");
        list = new ArrayList<>();


        configFirebase();

        adapterLinhadoTempo = new AdapterLinhadoTempo(TransacoesActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TransacoesActivity.this);
        recyclerViewtran.setLayoutManager(layoutManager);
        recyclerViewtran.setAdapter(adapterLinhadoTempo);
        myreference.child("lancamentos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapterLinhadoTempo.addItem(dataSnapshot);
               // totalGastoPeriodo(dataSnapshot);
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
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
        if (!a.isEmpty())
        for (Lancamento lan : a) {

            if (lan.getStatusOp()==1){
                totaladcionado = totaladcionado + lan.getValor();
            }else {
                totalgasto = totalgasto - lan.getValor();
            }
        }

        totalstatus= totaladcionado+totalgasto;
        totalGasto.setText("Total Gasto: "+String.valueOf(totalgasto));
        totaladiconado.setText("Total Adicionado: "+String.valueOf(totaladcionado));
        statusfinal.setText("Ver se utiliza: "+String.valueOf(totalstatus));
    }

    private void configFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
         myreference = firebaseDatabase.getReference().child("financeiro").child(firebaseUser.getUid());

    }
}
