package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.felipedeveloper.gestaofinanceira.Adapters.AdicionarUsuarioAdapter;
import br.com.felipedeveloper.gestaofinanceira.Helper.CreditoDebitoEnum;
import br.com.felipedeveloper.gestaofinanceira.Model.Grupo;
import br.com.felipedeveloper.gestaofinanceira.Model.LancamentoGrupo;
import br.com.felipedeveloper.gestaofinanceira.Model.Usuario;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Classe responsavel por adicionar um novo grupo
 * ela herda metodos da classe base Activity
 */
public class AddGrupoActivity extends BaseActivity {

    //region Elementos da View
    @BindView(R.id.floataddgrupo)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.editnomegrupo)
    EditText inputNomeGrupo;
    @BindView(R.id.textInputLayout7)
    TextInputLayout textInputLayout;
    @BindView(R.id.editsaldogrupo)
    EditText inputSaldoGrupo;
    @BindView(R.id.editemailgrupo)
    EditText inputEmailGrupo;
    @BindView(R.id.recyclerusuariogrupo)
    RecyclerView recyclerViewuser;
    @BindView(R.id.imageaddgrupo)
    ImageView imaAddGrupo;
    //endregion

    //region Variaveis Globais de toda a classe
    AdicionarUsuarioAdapter adicionarUsuarioAdapter;
    LinearLayoutManager layoutManager;
    List<Usuario> exibirList;
    Usuario usuarioLogado;
    private DatabaseReference Usuariosreference;
    private DatabaseReference Financeiroreference;
    private Context context = AddGrupoActivity.this;
    private Grupo grupo;
    private List<Usuario> usuarioList;
    private LancamentoGrupo lancamentoGrupo;
    FirebaseAuth firebaseAuth;
    Usuario usuario;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grupo);

        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // adiconando support a toolbar
        ButterKnife.bind(this); // anotation processor para evitar codigo redundant
        firebaseAuth = FirebaseAuth.getInstance();
        // recuperandoa referencia do Banco de dados e do usuario logado
        Financeiroreference = configFirebase(Financeiroreference);
        Usuariosreference = configFirebaseUsuario(Usuariosreference);
        floatingActionButton.hide();// retirando a visibilidade do botão de criar o grupo
        // Adiconando no editext o tratamento de valor
        inputSaldoGrupo.addTextChangedListener(onTextChangedListener(inputSaldoGrupo));

        InitObjects();// inicializando as variaveis Globais

        // metodo responsavel por configurar a lista de Grupos
        ConfiguracaoRecyclerView();

        // metodo para limpar a view apos um erro quando o usuario começa a digitar
        inputNomeGrupo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout.setError("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /**
         * Buscando todos os usuarios no nó de usuarios
         * para terem seus emails comparados com os emails digitados no campo
         */
        Usuariosreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    usuario = snapshot.getValue(Usuario.class);
                    usuarioList.add(usuario);
                }
                usuarioLogado = meuUsuario(usuarioList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        /**
         * Click do botão para adicionar um novo usuario com base no email digitado
         */
        imaAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaTeclado(view); // fechando o teclado para exibir o usuario adicionado
                if (!inputEmailGrupo.getText().toString().isEmpty()) { // validando email grupo
                    String emailDigitado; // string para receber o email
                    emailDigitado = inputEmailGrupo.getText().toString();
                    for (int i = 0; i < usuarioList.size(); i++) { // percorendo a lista de usuarios
                        if (!usuarioList.get(i).getUsuarioEmail().isEmpty()) {
                            if (usuarioList.get(i).getUsuarioEmail().equals(emailDigitado)) { // comparando ao email digitado com os emails cadastrados no BD
                                exibirList.add(usuarioList.get(i)); // caso encontre adiciona na lista o usuario
                                exibirList.add(usuarioLogado); // adicionando você a lista para ser exibido tambem

                                // configurando o adapter para exibir e mostrar a lista de usuario
                                adicionarUsuarioAdapter = new AdicionarUsuarioAdapter(exibirList, context);
                                adicionarUsuarioAdapter.notifyDataSetChanged();
                                recyclerViewuser.setAdapter(adicionarUsuarioAdapter); // pasando  o adapter
                                inputEmailGrupo.getText().clear(); // limpando compo de email
                                floatingActionButton.show(); // exibindo o botão para que o grupo possa ser salvo
                            }
                        }
                    }
                    if (exibirList.size() == 0) { // se a lista de usuarios for igual a zero é pq não foi encontrado nenhum
                        ExibirMensagem(AddGrupoActivity.this, SweetAlertDialog.ERROR_TYPE, "Usuario não Encontrado.\n Verifique o Email Informado");
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
                // recuperando a data diaria para ser adiconada ao lancamento
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String dataformatada = formataData.format(date);
                dataformatada = dataformatada.replaceAll("-", "/"); // trocando o simbolo de - da data pelo / tradicional
                Long timestamp = criaTimeStamp();
                boolean ValidaOK = ValidaEntradas();

                lancamentoGrupo.setCreatedAt(timestamp);
                lancamentoGrupo.setData(dataformatada);
                lancamentoGrupo.setNomeColaborador(firebaseAuth.getCurrentUser().getDisplayName());
                lancamentoGrupo.setNomeGrupo(grupo.getIdGrupo());
                lancamentoGrupo.setStatusOp(CreditoDebitoEnum.Credito.getValor());
                lancamentoGrupo.setTitulo("Criação Grupo");
                if (ValidaOK) {
                    for (int i = 0; i < exibirList.size(); i++) {
                    }
                    Financeiroreference.child(grupo.getIdGrupo()).setValue(grupo); // salvando o grupo;
                    Financeiroreference.child(grupo.getIdGrupo()).child("lancamentos").child(UUID.randomUUID().toString()).setValue(lancamentoGrupo);

                    ExibirMensagem(AddGrupoActivity.this,SweetAlertDialog.SUCCESS_TYPE,"Grupo Adicionado.");
                    limparCampos();
                }


            }
        });
        /**
         * Metodo responsavel por retirar a visibilidade do botão de adicionar
         * quando a lista é movimentada
         */
        recyclerViewuser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) { // posição inicial do recycler view
                    // Scroll Down
                    if (floatingActionButton.isShown()) {
                        floatingActionButton.hide(); // tirando a visibilidade
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!floatingActionButton.isShown()) {
                        floatingActionButton.show(); // fazendo o botão aparecer
                    }
                }
            }
        });

    }
    /**
     * Metodo responsavel por criar o timestamp do lançamento para futuramente ter a precisao de cada lançamento
     *
     * @return timesTamp
     */
    @NonNull
    private Long criaTimeStamp() {
        Calendar cal = Calendar.getInstance();
        Date data_atual = cal.getTime();
        final Timestamp ts = new Timestamp(data_atual.getTime());
        return ts.getTime();
    }

    private boolean ValidaEntradas() {
        boolean retorno = false;

        if (inputNomeGrupo.getText().toString().isEmpty()) {
            textInputLayout.setError("O nome do grupo é obrigatorio.");
        }
        if (inputSaldoGrupo.getText().toString().isEmpty()) {
            String  valorat = "0";
                grupo.setSaldoGrupo(Double.parseDouble(valorat));  // a variavel precisa ser inicializada mesmo nao sendo obrigadotoria para ser salva no firebase
        }else {
            String  valorat = inputSaldoGrupo.getText().toString().replace(",", ""); // removendo as virgulas de validacao
            grupo.setSaldoGrupo(Double.parseDouble(valorat));
            lancamentoGrupo.setValor(Double.parseDouble(valorat));
        }
        if (!inputNomeGrupo.getText().toString().isEmpty()) {
            String uid = UUID.randomUUID().toString(); // gerando a UId unica do grupo
            grupo.setIdGrupo(uid); // atribuindo id
            grupo.setNomeGrupo(inputNomeGrupo.getText().toString());
            grupo.setUsuarioList(exibirList); // salnvondo a lista de usuarios no grupo
            retorno = true;
        }
        return retorno;
    }

    /**
     * Metodo responsavel por iniciar as variaveis Globais
     */
    private void InitObjects() {
        usuarioList = new ArrayList<>();
        exibirList = new ArrayList<>();
        grupo = new Grupo();
        usuarioLogado = new Usuario();
        lancamentoGrupo = new LancamentoGrupo();
    }

    /**
     * Metodo responsavel por definir orientação da lista de gusuarios
     * e sua devida configuração com o Recycler view
     */
    private void ConfiguracaoRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.canScrollVertically();
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerViewuser.setLayoutManager(layoutManager);
        /**
         * adiconado linhas ao fim de cada item da view
         */
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewuser.getContext(),
                layoutManager.getOrientation());
        recyclerViewuser.addItemDecoration(dividerItemDecoration);
        recyclerViewuser.setNestedScrollingEnabled(false);
    }


    private void fechaTeclado(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * tratamento ao usuario logado para ser exebido na lista de integrantes do grupo e aparecer você
     *
     * @param usuarioList
     * @return
     */
    private Usuario meuUsuario(List<Usuario> usuarioList) {
        Usuario usuario = new Usuario();
        String minhaId = UserRetornoId();

        for (Usuario ll : usuarioList) {
            if (ll.getIdUsuario().equals(minhaId)) {
                usuario.setUsuarioNome("Você");
                usuario.setUsuarioEmail(ll.getUsuarioEmail());
                usuario.setIdUsuario(ll.getIdUsuario());
                usuario.setFotoUrl(ll.getFotoUrl());

            }
        }

        return usuario;
    }

    private void limparCampos() {
        inputEmailGrupo.getText().clear();
        inputNomeGrupo.getText().clear();
        inputSaldoGrupo.getText().clear();
        adicionarUsuarioAdapter.clear();
    }


    /**
     * sobre escrita do metodo config firebase que se encontra na base reference
     * a sobre escrita se deu pelo fato de mudar o Nó
     *
     * @param reference
     * @return
     */
    @Override
    public DatabaseReference configFirebase(DatabaseReference reference) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // pegando a instancia do banco de dados do firebase
        reference = firebaseDatabase.getReference().child("financeiro").child("grupos");// definindo qual o pont oque  a referencia do firebase ficara
        return reference;
    }
}
