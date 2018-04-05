package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.os.Bundle;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OpcoesFinanceiraActivity extends AppCompatActivity {
    FloatingActionButton ffab;
    FloatingActionMenu fabmenu;
     Animation scaledown;
     Animation scaleup;
     ConstraintLayout constraintLayout;
     @BindView(R.id.cardViewcarteira)
    CardView cardCarteira;
     @BindView(R.id.cardViewcartao)
     CardView cardCartao;
     @BindView(R.id.cardViewcontasbancaria)
     CardView cardContaBancaria;
     @BindView(R.id.cardViewgrupo)
     CardView cardGrupo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes_financeira);
        fabmenu = (FloatingActionMenu)findViewById(R.id.menu);
         scaledown = AnimationUtils.loadAnimation(this, R.anim.down);
            scaleup = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            constraintLayout = (ConstraintLayout)findViewById(R.id.constraint);
        ButterKnife.bind(this);
        fabmenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(opened){
                    constraintLayout.setVisibility(View.GONE);
//
                }else {
//
                    constraintLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        cardCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpcoesFinanceiraActivity.this,CartaoActivity.class);
//                new Intent(OpcoesFinanceiraActivity.this,CartaoActivity.class);
                startActivity(intent);
            }
        });
        cardCarteira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cardContaBancaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cardGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fabmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_call) {
            Intent intent = new Intent(OpcoesFinanceiraActivity.this,UsuarioConfiguracaoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
