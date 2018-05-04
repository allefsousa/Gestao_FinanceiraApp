package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.AdapterLinhadoTempo;
import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class TransacoesActivity extends AppCompatActivity {
    @BindView(R.id.recyclertransacoes)
    RecyclerView recyclerViewtran;
    AdapterLinhadoTempo adapterLinhadoTempo;
    DatabaseReference myreference;
    private FirebaseUser firebaseUser;
    Carteira carteira;
    List<Carteira> carteiraList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        configFirebase();
        carteiraList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        adapterLinhadoTempo = new AdapterLinhadoTempo(TransacoesActivity.this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(TransacoesActivity.this);
        recyclerViewtran.setLayoutManager(layoutManager);
        recyclerViewtran.setAdapter(adapterLinhadoTempo);
        myreference.child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
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



    }
    private void configFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
         myreference = firebaseDatabase.getReference().child("financeiro").child("movimentacao");

    }
}
