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
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.Cartao;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OpcoesFinanceiraActivity extends AppCompatActivity {

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
    @BindView(R.id.cardsaldocontabancaria)
    TextView saldoContabbancaria;
    @BindView(R.id.cardviewsaldocartao)
    TextView saldoCartao;

    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private String idUser;
    private ContasBancarias conta;
    private List<ContasBancarias> contasBancariasList;
    private NumberFormat df;
    private Cartao cartaoModel;
    private List<Cartao> cartaoList;
    private FloatingActionButton actionButtonitemcartao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes_financeira);
        fabmenu = (FloatingActionMenu) findViewById(R.id.menu);
        scaledown = AnimationUtils.loadAnimation(this, R.anim.down);
        scaleup = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        actionButtonitemcartao = findViewById(R.id.menu_itemcartao);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        IniciaFirebase();
        contasBancariasList = new ArrayList<>();
        cartaoList = new ArrayList<>();
        cartaoModel = new Cartao();
        df = new DecimalFormat("#0.00");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double result = null;
                Double result1 = null;

                /**
                 * limpando os dados da lista pois quando algo é modificado na arvore
                 * todo o processo do banco de dados é refeito  em tempo real
                 */
                contasBancariasList.clear();
                if (dataSnapshot.child("banco").hasChild(idUser)) {
                    for (DataSnapshot dd : dataSnapshot.child("banco").child(idUser).getChildren()) {
                        conta = dd.getValue(ContasBancarias.class);
                        contasBancariasList.add(conta);
                    }
                    Boolean flag = false;
                    for (ContasBancarias cc : contasBancariasList) {

                        if (!flag) {
                            result = Double.parseDouble(cc.getSaldoContabancaria().toString());
                            flag = true;
                        } else {
                            result = result + Double.parseDouble(cc.getSaldoContabancaria().toString());
                        }


                    }
                    saldoContabbancaria.setText(String.valueOf(("Saldo: " + df.format(result))));


                }
                if (dataSnapshot.child("cartao").hasChild(idUser)) {

                    cartaoList.clear();
                    for (DataSnapshot dd : dataSnapshot.child("cartao").child(idUser).getChildren()) {
                        cartaoModel = dd.getValue(Cartao.class);
                        cartaoList.add(cartaoModel);
                    }
                    Boolean flag = false;
                    for (Cartao cc : cartaoList) {
                        if (!flag) {
                            result1 = (cc.getSaldoCartao());
                            flag = true;
                        } else {
                            result1 = result1 + cc.getSaldoCartao();

                        }
                    }
                    saldoCartao.setText(String.valueOf("Saldo: " + df.format(result1)));


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fabmenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    constraintLayout.setVisibility(View.GONE);
//
                } else {
//
                    constraintLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        actionButtonitemcartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OpcoesFinanceiraActivity.this, CartaoActivity.class));
            }
        });

        cardCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpcoesFinanceiraActivity.this, CartaoActivity.class);
//                new Intent(OpcoesFinanceiraActivity.this,CartaoActivity.class);
                startActivity(intent);
            }
        });
        cardCarteira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OpcoesFinanceiraActivity.this, CarteiraActivity.class));

            }
        });
        cardContaBancaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OpcoesFinanceiraActivity.this, ContasBancariasActivity.class));
            }
        });
        cardGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OpcoesFinanceiraActivity.this, GrupoActivity.class));

            }
        });


    }

    private void IniciaFirebase() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            idUser = firebaseUser.getUid();
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("financeiro");
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
            Intent intent = new Intent(OpcoesFinanceiraActivity.this, UsuarioConfiguracaoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
