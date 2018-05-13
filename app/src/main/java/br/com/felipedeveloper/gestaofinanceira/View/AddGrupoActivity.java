package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.AdapterAdcionarUsuario;
import br.com.felipedeveloper.gestaofinanceira.Model.Grupo;
import br.com.felipedeveloper.gestaofinanceira.Model.Usuario;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddGrupoActivity extends BaseActivity {

    @BindView(R.id.floataddgrupo)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.editnomegrupo)
    EditText inputNomeGrupo;
    @BindView(R.id.editsaldogrupo)
    EditText inputSaldoGrupo;
    @BindView(R.id.editemailgrupo)
    EditText inputEmailGrupo;
    @BindView(R.id.recyclerusuariogrupo)
    RecyclerView recyclerViewuser;

    @BindView(R.id.imageaddgrupo)
    ImageView imaAddGrupo;
    AdapterAdcionarUsuario adapterAdcionarUsuario ;
    LinearLayoutManager layoutManager;

    private DatabaseReference reference;
    private Context context = AddGrupoActivity.this;
    private Grupo grupo;
    private List<Usuario> usuarioList;
    List<Usuario>exibirList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grupo);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        grupo = new Grupo();
        reference=  configFirebaseUsuario(reference);
        usuarioList = new ArrayList<>();
         layoutManager = new LinearLayoutManager(this);
       layoutManager.canScrollVertically();
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerViewuser.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewuser.getContext(),
                layoutManager.getOrientation());
        recyclerViewuser.addItemDecoration(dividerItemDecoration);
        recyclerViewuser.setNestedScrollingEnabled(false);
        exibirList = new ArrayList<>();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    usuario = snapshot.getValue(Usuario.class);
                    usuarioList.add(usuario);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
        imaAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputEmailGrupo.getText().toString().isEmpty()){
                    String emailDigitado;
                    emailDigitado = inputEmailGrupo.getText().toString();
                    for (int i =0;i<usuarioList.size();i++){
                        if (!usuarioList.get(i).getUsuarioEmail().isEmpty()){
                            if (usuarioList.get(i).getUsuarioEmail().equals(emailDigitado)){
                                exibirList.add(usuarioList.get(i));
                                adapterAdcionarUsuario = new AdapterAdcionarUsuario(exibirList,context);
                                adapterAdcionarUsuario.notifyDataSetChanged();
                                recyclerViewuser.setAdapter(adapterAdcionarUsuario);
                                inputEmailGrupo.getText().clear();
                            }
                        }
                    }
                }

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limparCampos();
//                grupo.setNomeGrupo(inputNomeGrupo.getText().toString());
//                grupo.setSaldoGrupo(inputSaldoGrupo.getText().toString());
//                grupo.setUsuarioList(exibirList);

            }
        });
    }
    private void limparCampos(){
        inputEmailGrupo.getText().clear();
        inputNomeGrupo.getText().clear();
        inputSaldoGrupo.getText().clear();
        adapterAdcionarUsuario.clear();
    }
}
