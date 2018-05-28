package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.felipedeveloper.gestaofinanceira.Model.Carteira;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CarteiraActivity extends AppCompatActivity {
    @BindView(R.id.btnnovacarteira)
    FloatingActionButton floatActionAddCarteira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carteira);
        ButterKnife.bind(this);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatActionAddCarteira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarteiraActivity.this,AddBancoActivity.class);
                intent.putExtra("opcao","carteira");
                startActivity(intent);
            }
        });

    }
}
