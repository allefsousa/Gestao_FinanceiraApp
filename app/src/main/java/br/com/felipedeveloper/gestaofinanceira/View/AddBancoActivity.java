package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by allef on 16/04/2018.
 */

public class AddBancoActivity extends AppCompatActivity {
    @BindView(R.id.btnnovobanco)
    Button btnaddbanco;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String idUser;
    EditText titulobanco;
    EditText agenciaBanco;
    EditText saldobanco;
    DatabaseReference myreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbanco);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        idUser = auth.getCurrentUser().getUid();
        IniciaFirebase();
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
}
