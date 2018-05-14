package br.com.felipedeveloper.gestaofinanceira.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.felipedeveloper.gestaofinanceira.R;

public class LancamentoGrupoActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento_grupo);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
