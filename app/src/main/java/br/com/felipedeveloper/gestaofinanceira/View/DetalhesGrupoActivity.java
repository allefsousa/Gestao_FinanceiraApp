package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;


import br.com.felipedeveloper.gestaofinanceira.Adapters.AdapterLinhadoTempo;
import br.com.felipedeveloper.gestaofinanceira.Model.Lancamento;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetalhesGrupoActivity extends AppCompatActivity {
    @BindView(R.id.toolbargrupo)
    Toolbar toolbarGrupo;
    @BindView(R.id.floatlancamentogrupo)
    FloatingActionButton floatingActionButton;
    AdapterLinhadoTempo adapterLinhadoTempo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_grupo);
        ButterKnife.bind(this);

        String newString1 = retornoNomeGrupo(savedInstanceState);

        setSupportActionBar(toolbarGrupo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(newString1);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(DetalhesGrupoActivity.this,LancamentoGrupoActivity.class));
            }
        });

    }
    // recuperando os dados passados da activity anterior
    // neste caso o nome do grupo para ser exibido na toolbar
    private String retornoNomeGrupo(Bundle savedInstanceState) {
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("nomegrupo");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("nomegrupo");
        }
        return newString;
    }
}
