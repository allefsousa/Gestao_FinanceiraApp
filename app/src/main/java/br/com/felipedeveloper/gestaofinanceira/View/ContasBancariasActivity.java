package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.BancoAdapter;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContasBancariasActivity extends AppCompatActivity {
    @BindView(R.id.btnnovobanco)
    FloatingActionButton btnaddbanco;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String idUser;
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    List<ContasBancarias> contasBancariasList;
    private DatabaseReference myreference;
    ValueEventListener valueEventListener;
    ContasBancarias contasBancarias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_bancarias);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        IniciaFirebase();
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contasBancariasList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerContasbancarias);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        myreference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dd : dataSnapshot.getChildren()){
                  contasBancarias= dd.getValue(ContasBancarias.class);
                    contasBancariasList.add(contasBancarias);
                }
                BancoAdapter bancoAdapter = new BancoAdapter(contasBancariasList,ContasBancariasActivity.this);
                recyclerView.setAdapter(bancoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        btnaddbanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContasBancariasActivity.this,MeiodePagamentoActivity.class);
                intent.putExtra("opcao","banco");
                startActivity(intent);

            }
        });


    }

    /**
     * Metodo responsavel por criar o  dialog para o usuario adicionar os dados do Banco/Agencia
     */
    private void AdicionarBancoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContasBancariasActivity.this);
        View alview = getLayoutInflater().inflate(R.layout.activity_meio_pagamento, null);
        final ContasBancarias contasBancarias = new ContasBancarias();

//        edtTituloBanco = alview.findViewById(R.id.edtTituloBanco);
//        agenciaBanco = alview.findViewById(R.id.agencia);
//        edtSaldoBanco = alview.findViewById(R.id.saldo);
        Button salvar = alview.findViewById(R.id.btnsalvarbanco);
        builder.setView(alview);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setDimAmount(1.0f); // cor de fundo dialog
        alertDialog.show();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
        myreference = firebaseDatabase.getReference().child("financeiro").child(idUser).child("banco");
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
