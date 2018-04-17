package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Adapters.AgenciaBancoAdapter;
import br.com.felipedeveloper.gestaofinanceira.Model.ContasBancarias;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContasBancariasActivity extends AppCompatActivity {
    @BindView(R.id.btnnovobanco)
    Button btnaddbanco;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String idUser;
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    List<ContasBancarias> contasBancariasList;
    private DatabaseReference myreference;
    ValueEventListener valueEventListener;
    ContasBancarias contasBancarias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_bancarias);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        IniciaFirebase();
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contasBancariasList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        myreference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dd : dataSnapshot.getChildren()){
                  contasBancarias= dd.getValue(ContasBancarias.class);
                    contasBancariasList.add(contasBancarias);
                }
                AgenciaBancoAdapter agenciaBancoAdapter = new AgenciaBancoAdapter(contasBancariasList,ContasBancariasActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(agenciaBancoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recyclerView = findViewById(R.id.recyclerContasbancarias);
        recyclerView.setLayoutManager(layoutManager);


        btnaddbanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContasBancariasActivity.this, AddBancoActivity.class));
            }
        });

    }

    /**
     * Metodo responsavel por criar o  dialog para o usuario adicionar os dados do Banco/Agencia
     */
    private void AdicionarBancoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContasBancariasActivity.this);
        View alview = getLayoutInflater().inflate(R.layout.activity_addbanco, null);
        final ContasBancarias contasBancarias = new ContasBancarias();

//        titulobanco = alview.findViewById(R.id.titulobanco);
//        agenciaBanco = alview.findViewById(R.id.agencia);
//        saldobanco = alview.findViewById(R.id.saldo);
        Button salvar = alview.findViewById(R.id.btnsalvarbanco);
        builder.setView(alview);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setDimAmount(1.0f); // cor de fundo dialog
        alertDialog.show();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
        myreference = firebaseDatabase.getReference().child("financeiro").child("banco").child(idUser);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
