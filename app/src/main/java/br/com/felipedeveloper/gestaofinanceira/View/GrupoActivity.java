package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.GrupoAdapter;
import br.com.felipedeveloper.gestaofinanceira.Model.Grupo;
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
    String firebaseAuth;
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

        ConfigRecyclerView();


        floatingAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GrupoActivity.this,AddGrupoActivity.class));
            }
        });


        // buscando os Grupos no Nó Grupos, os adiconando a lista para serem enviado ao adapter
        reference.child("grupos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                grupoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                  Grupo grupo = snapshot.getValue(Grupo.class);
                    List<String> ss;
                    ss = grupo.getUsuarioList();
                    for (String a : ss){
                        if (firebaseAuth.equals(a)){
                            grupoList.add(grupo);
                        }
                    }
//                    if (snapshot.child("usuarioList").hasChild(firebaseAuth)){
//                        grupoList.add(grupo);
//                    }




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

//    private void mensagem() {
//
//        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
//                .setTitleText("Criar Grupo")
//                .setConfirmText("Adicionar")
//                .setCustomView(editText)
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                        boolean a = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
//                                editText.getWindowToken(), 0);
//                        if (a) {
//                            sweetAlertDialog.setTitleText("Deleted!")
//                                    .setContentText("Your imaginary file has been deleted!")
//                                    .setConfirmText(editText.getText().toString())
//                                    .setConfirmClickListener(null)
//                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                        }
//
//
//                    }
//                })
//                .show();
//
//
//    }
}
