package br.com.felipedeveloper.gestaofinanceira.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContasBancariasActivity extends AppCompatActivity {
    @BindView(R.id.btnnovobanco)
    Button btnaddbanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_bancarias);

        ButterKnife.bind(this);
        btnaddbanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 12/04/2018 dialog ou activity analisar 
            }
        });
    }
}
