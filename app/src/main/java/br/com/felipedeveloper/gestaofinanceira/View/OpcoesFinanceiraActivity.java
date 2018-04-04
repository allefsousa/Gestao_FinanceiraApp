package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
    Button btnlancamentos;
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
        btnlancamentos = (Button) findViewById(R.id.imageButton);
        fabmenu = (FloatingActionMenu)findViewById(R.id.menu);
         scaledown = AnimationUtils.loadAnimation(this, R.anim.down);
            scaleup = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            constraintLayout = (ConstraintLayout)findViewById(R.id.constraint);
        ButterKnife.bind(this);
//        fabmenu.setOnMenuButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//
//            }
//        });
        fabmenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(opened){
                    constraintLayout.setVisibility(View.GONE);
//                    btnlancamentos.setAnimation(scaledown);constraintLayout.setVisibility(View.GONE);
//                    cardCartao.setVisibility(View.INVISIBLE);
//                    cardCarteira.setVisibility(View.GONE);
//                    cardContaBancaria.setVisibility(View.GONE);
//                    cardGrupo.setVisibility(View.GONE);
//                    btnlancamentos.setVisibility(View.INVISIBLE);
//                    btnlancamentos.setEnabled(false);
                }else {
//                    btnlancamentos.setVisibility(View.VISIBLE);
//                    btnlancamentos.setEnabled(true);
//                    cardCartao.setVisibility(View.VISIBLE);
//                    cardCarteira.setVisibility(View.VISIBLE);
//                    cardContaBancaria.setVisibility(View.VISIBLE);
//                    cardGrupo.setVisibility(View.VISIBLE);
//                    btnlancamentos.setAnimation(scaleup);
                    constraintLayout.setVisibility(View.VISIBLE);
                }
            }
        });


    }
}
