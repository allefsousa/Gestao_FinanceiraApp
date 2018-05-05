package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.CartaoAdapter;
import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CartaoActivity extends AppCompatActivity {

    @BindView(R.id.floatnovo)
    FloatingActionButton flo;
    @BindView(R.id.recyclerViewcartoes)
    RecyclerView recyclerViewCartoes;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String idUser;
    List<Cartao> cartaoList;
    private DatabaseReference myreference;
    ValueEventListener valueEventListener;
    Cartao cartao;
    CartaoAdapter cartaoAdapter;
    private NumberFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        cartao = new Cartao();
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        IniciaFirebase();
        cartao = new Cartao();
        cartaoList = new ArrayList<>();
        df = new DecimalFormat("#0.00");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.canScrollVertically();
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerViewCartoes.setLayoutManager(layoutManager);
        myreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    cartao = d.getValue(Cartao.class);
                    cartaoList.add(cartao);
                }
                cartaoAdapter = new CartaoAdapter(cartaoList,CartaoActivity.this);
                recyclerViewCartoes.setAdapter(cartaoAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        flo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartaoActivity.this,AddCartaoActivity.class));
            }
        });
    }
    private void IniciaFirebase() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            idUser = firebaseUser.getUid();
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myreference = firebaseDatabase.getReference().child("financeiro").child("cartao").child(idUser);
    }

}
