package br.com.felipedeveloper.gestaofinanceira.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.felipedeveloper.gestaofinanceira.R;

public class TransacoesContaActivity extends AppCompatActivity {
    String retornoactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes_conta);
        retornoactivity = retornoNomeConta(savedInstanceState);

    }

    private String retornoNomeConta(Bundle savedInstanceState) {
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("tituloConta");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("tituloConta");
        }
        return newString;
    }
}
