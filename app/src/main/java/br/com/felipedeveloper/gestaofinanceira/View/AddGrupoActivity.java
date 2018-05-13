package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.Grupo;
import br.com.felipedeveloper.gestaofinanceira.Model.Usuario;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddGrupoActivity extends BaseActivity {

    @BindView(R.id.floataddgrupo)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.editnomegrupo)
    TextInputEditText inputNomeGrupo;
    @BindView(R.id.editsaldogrupo)
    TextInputEditText inputSaldoGrupo;
    @BindView(R.id.editemailgrupo)
    TextInputEditText inputEmailGrupo;
    @BindView(R.id.textviparticipantes)
    TextView textExibirparticipantes;
    @BindView(R.id.imageaddgrupo)
    ImageView imaAddGrupo;
    @BindView(R.id.imagecancelparticipante)
    ImageView imaCancela;

    private DatabaseReference reference;
    private Context context = AddGrupoActivity.this;
    private Grupo grupo;
    private List<Usuario> usuarioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grupo);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        grupo = new Grupo();
        reference=  configFirebaseUsuario(reference);
        usuarioList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    usuario = snapshot.getValue(Usuario.class);
                    usuarioList.add(usuario);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
        imaAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputEmailGrupo.getText().toString().isEmpty()){
                    String emailDigitado;
                    emailDigitado = inputEmailGrupo.getText().toString();
                    for (Usuario usuario : usuarioList){
                        if (!usuario.getUsuarioEmail().isEmpty()){
                            if (usuario.getUsuarioEmail().equals(emailDigitado)){
                                if (textExibirparticipantes.getText() != "Você"){
                                    textExibirparticipantes.setMaxLines(5);
                                    String todoemail = textExibirparticipantes.getText().toString() + "/"+emailDigitado;
                                  String aa[] = todoemail.split("/");
                                  String resultf = aa[0];
                                  resultf ="\n";
                                  resultf =resultf + aa[1];
                                    textExibirparticipantes.setText(resultf);
                                }else {
                                    textExibirparticipantes.setText(emailDigitado);
                                }

                                inputEmailGrupo.getText().clear();
                            }
                        }
                    }
                }

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
