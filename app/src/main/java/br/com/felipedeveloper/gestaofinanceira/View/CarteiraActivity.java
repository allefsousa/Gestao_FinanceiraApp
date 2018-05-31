package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.CarteiraAdapter;
import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CarteiraActivity extends BaseActivity {

    @BindView(R.id.recyclerCarteira)
    RecyclerView recyclerViewCarteira;
    @BindView(R.id.btnnovacarteira)
    FloatingActionButton floatActionAddCarteira;
    LinearLayoutManager layoutManager;
    private DatabaseReference financeiroreference;
    private DatabaseReference usuariosreference;
    Carteira carteira ;
    List<Carteira> carteiraList;
    CarteiraAdapter carteiraAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carteira);
        ButterKnife.bind(this);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        financeiroreference = configFirebase(financeiroreference);
        usuariosreference = configFirebaseUsuario(usuariosreference);
        carteiraList = new ArrayList<>();
        carteira = new Carteira();

        layoutManager = new LinearLayoutManager(this);
        layoutManager.canScrollVertically();
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerViewCarteira.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewCarteira.getContext(),
                layoutManager.getOrientation());
        recyclerViewCarteira.addItemDecoration(dividerItemDecoration);
        recyclerViewCarteira.setNestedScrollingEnabled(false);

        financeiroreference.child("carteira").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /**
                 * por que limpar antes de adicionar ?
                 *  pelo fato de você adicionar uma nova carteira em outra tela e logo em seguida retornar a esta
                 *   desse modo os dados anteriores sao mantidos na lista e ao retornar mostra elementos duplicados
                 */
                carteiraList.clear();

                for (DataSnapshot a : dataSnapshot.getChildren()){
                    carteira = a.getValue(Carteira.class);
                    carteiraList.add(carteira);
                }
                carteiraAdapter = new CarteiraAdapter(carteiraList,CarteiraActivity.this);
                recyclerViewCarteira.setAdapter(carteiraAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        floatActionAddCarteira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarteiraActivity.this, MeiodePagamentoActivity.class);
                intent.putExtra("opcao", "carteira");
                startActivity(intent);
            }
        });

        /**
         * Metodo responsavel por exibir ou não o botão de filtro
         * caso a tela seja rolada para cima o botão perde a visibilidade
         */
        recyclerViewCarteira.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) { // posição inicial do recycler view
                    // Scroll Down
                    if (floatActionAddCarteira.isShown()) {
                        floatActionAddCarteira.hide(); // tirando a visibilidade
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!floatActionAddCarteira.isShown()) {
                        floatActionAddCarteira.show(); // fazendo o botão aparecer
                    }
                }
            }
        });


    }
}
