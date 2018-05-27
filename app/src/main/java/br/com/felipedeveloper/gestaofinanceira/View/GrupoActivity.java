package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

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
    Context context;
    EditText editText;
    @BindView(R.id.fabaddgrupo)
    FloatingActionButton floatingActionButton;
    DatabaseReference reference,mm;
    String idUserlogado;
    List<Grupo> grupoList;
    Grupo grupo;
    @BindView(R.id.recyclertitulogrupo)
    RecyclerView recyclerViewGrupos;
    GrupoAdapter grupoAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        ButterKnife.bind(this);
        context = GrupoActivity.this;
        editText = new EditText(context);
        mm= FirebaseDatabase.getInstance().getReference().child("financeiro");

        grupoList = new ArrayList<>();
        grupo = new Grupo();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.canScrollVertically();
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerViewGrupos.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewGrupos.getContext(),
                layoutManager.getOrientation());
        recyclerViewGrupos.addItemDecoration(dividerItemDecoration);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,AddGrupoActivity.class));
            }
        });


        mm.child("grupos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Grupo grupo;
                grupoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    grupo = snapshot.getValue(Grupo.class);
                    grupoList.add(grupo);
                }
                grupoAdapter = new GrupoAdapter(context,grupoList);
                recyclerViewGrupos.setAdapter(grupoAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void mensagem() {

        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Criar Grupo")
                .setConfirmText("Adicionar")
                .setCustomView(editText)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        boolean a = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                                editText.getWindowToken(), 0);
                        if (a) {
                            sweetAlertDialog.setTitleText("Deleted!")
                                    .setContentText("Your imaginary file has been deleted!")
                                    .setConfirmText(editText.getText().toString())
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }


                    }
                })
                .show();


    }
}
