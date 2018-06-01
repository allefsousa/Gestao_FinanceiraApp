package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartaoActivity extends BaseActivity {

    //region variaveis Globais
    @BindView(R.id.floatnovo)
    FloatingActionButton floatingNovocartao;
    @BindView(R.id.recyclerViewcartoes)
    RecyclerView recyclerViewCartoes;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String idUser;
    List<Cartao> cartaoList;
    private DatabaseReference myreference;
    Cartao cartao;
    CartaoAdapter cartaoAdapter;
    private NumberFormat df;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        // instancia de objetos
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();

        cartao = new Cartao();
        cartaoList = new ArrayList<>();
        df = new DecimalFormat("#0.00");
        myreference = configFirebase(myreference); // referencia do Bd
        ConfigRecyclerView();

        /**
         * buscando os cartães no BD
         */
        myreference.child("cartao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartaoList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    cartao = d.getValue(Cartao.class);
                    cartaoList.add(cartao);
                }
                cartaoAdapter = new CartaoAdapter(cartaoList,CartaoActivity.this);
                recyclerViewCartoes.setAdapter(cartaoAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                ExibirMensagem(CartaoActivity.this, SweetAlertDialog.ERROR_TYPE,"Erro ao Buscar Cartões");
            }
        });


        /**
         * click do botão de adiconar um novo cartão
         * passando a palavra cartao para outra activity tratar como um cadatro de cartao
         */
        floatingNovocartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartaoActivity.this,MeiodePagamentoActivity.class);
                intent.putExtra("opcao","cartao");
                startActivity(intent);

            }
        });

        /**
         * Metodo responsavel por exibir ou não o botão de filtro
         * caso a tela seja rolada para cima o botão perde a visibilidade
         */
        recyclerViewCartoes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) { // posição inicial do recycler view
                    // Scroll Down
                    if (floatingNovocartao.isShown()) {
                        floatingNovocartao.hide(); // tirando a visibilidade
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!floatingNovocartao.isShown()) {
                        floatingNovocartao.show(); // fazendo o botão aparecer
                    }
                }
            }
        });


    }

    private void ConfigRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.canScrollVertically();
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerViewCartoes.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewCartoes.getContext(),
                layoutManager.getOrientation());
        recyclerViewCartoes.addItemDecoration(dividerItemDecoration);
    }


}
