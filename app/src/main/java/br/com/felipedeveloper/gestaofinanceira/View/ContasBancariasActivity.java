package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.BancoAdapter;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ContasBancariasActivity extends BaseActivity {
    //region Variaveis Gloabais e item da View
    @BindView(R.id.btnnovobanco)
    FloatingActionButton floatingAddBanco;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    List<ContasBancarias> contasBancariasList;
    ContasBancarias contasBancarias;
    private DatabaseReference myreference;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_bancarias);
        ButterKnife.bind(this);
        myreference = configFirebase(myreference); // recebendo intancia do firebase

        // tratando action bar
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contasBancariasList = new ArrayList<>();

        // configurando a lista para exibir as contas bancarias
        configRecyclerView();

        myreference.child("banco").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contasBancariasList.clear();  // limpando a lista de bancos

                for (DataSnapshot dd : dataSnapshot.getChildren()) {
                    contasBancarias = dd.getValue(ContasBancarias.class); // banco trazido do firebase
                    contasBancariasList.add(contasBancarias); // banco a dicionado a lista
                }
                BancoAdapter bancoAdapter = new BancoAdapter(contasBancariasList, ContasBancariasActivity.this); // passando lista de banco para o adapter montar
                recyclerView.setAdapter(bancoAdapter); // passando o adapter montado ara ser exibido
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ExibirMensagem(ContasBancariasActivity.this, SweetAlertDialog.ERROR_TYPE,"Erro ao Buscar Bancos !!");
            }
        });

        /**
         * click do botão para adiconar um banco
         * enviando para outra activity o texto banco para ja ser montado o layou.
         */
        floatingAddBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContasBancariasActivity.this, MeiodePagamentoActivity.class);
                intent.putExtra("opcao", "banco");
                startActivity(intent);

            }
        });

        /**
         * Metodo responsavel por exibir ou não o botão de filtro
         * caso a tela seja rolada para cima o botão perde a visibilidade
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) { // posição inicial do recycler view
                    // Scroll Down
                    if (floatingAddBanco.isShown()) {
                        floatingAddBanco.hide(); // tirando a visibilidade
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!floatingAddBanco.isShown()) {
                        floatingAddBanco.show(); // fazendo o botão aparecer
                    }
                }
            }
        });


    }

    /**
     * configurando Recycler View
     */
    private void configRecyclerView() {
        recyclerView = findViewById(R.id.recyclerContasbancarias);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    /**
     * Metodo responsavel por criar o  dialog para o usuario adicionar os dados do Banco/Agencia
     */
    private void AdicionarBancoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContasBancariasActivity.this);
        View alview = getLayoutInflater().inflate(R.layout.activity_meio_pagamento, null);
        final ContasBancarias contasBancarias = new ContasBancarias();
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

    //region Ciclo de Vida da Activity
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
    //endregion
}
