package br.com.felipedeveloper.gestaofinanceira.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.felipedeveloper.gestaofinanceira.R;

public class AddGrupoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grupo);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
