package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Adapters.AdapterAdcionarUsuario;
import br.com.felipedeveloper.gestaofinanceira.FormadePagamentoEnum;
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

    private DatabaseReference Usuariosreference;
    private DatabaseReference Financeiroreference;
    private Context context = AddGrupoActivity.this;
    private Grupo grupo;
    private List<Usuario> usuarioList;
    List<Usuario>exibirList;
    Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grupo);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        Financeiroreference = configFirebase(Financeiroreference);
        Usuariosreference =  configFirebaseUsuario(Usuariosreference);
        usuarioList = new ArrayList<>();
        exibirList = new ArrayList<>();
        grupo = new Grupo();
        usuarioLogado = new Usuario();

        FormadePagamentoEnum ff = FormadePagamentoEnum.Carteira;


         layoutManager = new LinearLayoutManager(this);
       layoutManager.canScrollVertically();
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerViewuser.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewuser.getContext(),
                layoutManager.getOrientation());
        recyclerViewuser.addItemDecoration(dividerItemDecoration);
        recyclerViewuser.setNestedScrollingEnabled(false);




        Usuariosreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    usuario = snapshot.getValue(Usuario.class);
                    usuarioList.add(usuario);
                }
                usuarioLogado = meuUsuario(usuarioList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        imaAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaTeclado(view);
                if (!inputEmailGrupo.getText().toString().isEmpty()){
                    String emailDigitado;
                    emailDigitado = inputEmailGrupo.getText().toString();
                    for (int i =0;i<usuarioList.size();i++){
                        if (!usuarioList.get(i).getUsuarioEmail().isEmpty()){
                            if (usuarioList.get(i).getUsuarioEmail().equals(emailDigitado)){
                                exibirList.add(usuarioList.get(i));
                                exibirList.add(usuarioLogado);
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
        // fechar teclado quando apertar o botão OK
        inputEmailGrupo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            textView.getWindowToken(), 0);
                    textView.clearFocus();
                }
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: 13/05/2018 validar entradas nulas e vazias
                String uid = UUID.randomUUID().toString();
                grupo.setIdGrupo(uid);
                grupo.setNomeGrupo(inputNomeGrupo.getText().toString());
                grupo.setSaldoGrupo(Double.parseDouble(inputSaldoGrupo.getText().toString()));
                grupo.setUsuarioList(exibirList);
                for (int i =0; i < exibirList.size();i++){
                    Financeiroreference.child(uid).setValue(grupo);
                }
                limparCampos();


            }
        });
    }

    private void fechaTeclado(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private Usuario meuUsuario(List<Usuario> usuarioList) {
        Usuario usuario = new Usuario();
        String minhaId = UserRetornoId();

        for (Usuario ll : usuarioList){
            if (ll.getIdUsuario().equals(minhaId)){
              usuario.setUsuarioNome("Você");
              usuario.setUsuarioEmail(ll.getUsuarioEmail());
              usuario.setIdUsuario(ll.getIdUsuario());
              usuario.setFotoUrl(ll.getFotoUrl());

            }
        }

        return usuario;
    }

    private void limparCampos(){
        inputEmailGrupo.getText().clear();
        inputNomeGrupo.getText().clear();
        inputSaldoGrupo.getText().clear();
        adapterAdcionarUsuario.clear();
    }
    @Override
    public DatabaseReference configFirebase(DatabaseReference reference) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // pegando a instancia do banco de dados do firebase
        reference = firebaseDatabase.getReference().child("financeiro").child("grupos");// definindo qual o pont oque  a referencia do firebase ficara
        return reference;
    }
}
