package br.com.felipedeveloper.gestaofinanceira.Telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adaptadores.GrupoAdapter;
import br.com.felipedeveloper.gestaofinanceira.Modelo.Grupo;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class GrupoActivity extends BaseActivity {
    //region Variaveis Globais e Variaveis Da view
    @BindView(R.id.fabaddgrupo)
    FloatingActionButton floatingAddGrupo;
    DatabaseReference reference;
    List<Grupo> grupoList;
    Grupo grupo;
    @BindView(R.id.recyclertitulogrupo)
    RecyclerView recyclerViewGrupos;
    GrupoAdapter grupoAdapter;
    ValueEventListener valueEventListener;
    String firebaseAuth;
    List<String> ss ;
    //endregion




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        ButterKnife.bind(this);

        // referencia do banc para buscar os grupos
        reference = configFirebase(reference);
        reference= FirebaseDatabase.getInstance().getReference().child("financeiro");
        firebaseAuth = UserRetornoId();
        //instanciando novos objetos
        grupoList = new ArrayList<>();
        grupo = new Grupo();
        ss= new ArrayList<>();

        ConfigRecyclerView();

        floatingAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GrupoActivity.this,AddGrupoActivity.class));
            }
        });



        // buscando os Grupos no Nó Grupos, os adiconando a lista para serem enviado ao adapter
        valueEventListener = reference.child("grupos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                grupoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                  Grupo grupo = snapshot.getValue(Grupo.class);

                      ss = grupo.getUsuarioList();//  recuperando a lista de ids de usuarios dos grupos
                      if (ss != null){
                          for (String s : ss){
                              if (firebaseAuth.equals(s)){ // comparando as ids do banco com a do usuario logado para ser exibida
                                  grupoList.add(grupo); // adicionando o grupo a lista de grupos
                              }
                          }
                      }



                }
                grupoAdapter = new GrupoAdapter(GrupoActivity.this,grupoList); // passando os grupos para o adapter para mortar a view
                recyclerViewGrupos.setAdapter(grupoAdapter); // passando o adapter para a lista que exibira os grupos


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ExibirMensagem(GrupoActivity.this,SweetAlertDialog.ERROR_TYPE,"Erro ao Buscar Grupos");

            }
        });

        /**
         * Metodo responsavel por exibir ou não o botão de filtro
         * caso a tela seja rolada para cima o botão perde a visibilidade
         */
        recyclerViewGrupos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) { // posição inicial do recycler view
                    // Scroll Down
                    if (floatingAddGrupo.isShown()) {
                        floatingAddGrupo.hide(); // tirando a visibilidade
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!floatingAddGrupo.isShown()) {
                        floatingAddGrupo.show(); // fazendo o botão aparecer
                    }
                }
            }
        });



    }

    /**
     * configurando o recycler view que vai exibir os grupos
     */
    private void ConfigRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.canScrollVertically();
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerViewGrupos.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewGrupos.getContext(),
                layoutManager.getOrientation());
        recyclerViewGrupos.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reference.child("grupos").addValueEventListener(valueEventListener);
    }


    @Override
    protected void onDestroy() {
        reference.child("grupos").removeEventListener(valueEventListener);
        super.onDestroy();
    }
}
