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

    }
}
