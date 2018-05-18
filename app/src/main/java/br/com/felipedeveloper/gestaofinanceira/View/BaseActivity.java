package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.felipedeveloper.gestaofinanceira.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by allef on 10/05/2018.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
    public void ExibirMensagem(Context context, int successType, String s) {
        new SweetAlertDialog(context, successType)
                .setTitleText(getResources().getString(R.string.app_name))
                .setContentText(s)
                .show();
    }
    public DatabaseReference configFirebase(DatabaseReference reference) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // pegando a instancia do banco de dados do firebase
        reference = firebaseDatabase.getReference().child("financeiro").child(firebaseUser.getUid());// definindo qual o pont oque  a referencia do firebase ficara
        return reference;
    }
    public DatabaseReference configFirebaseUsuario(DatabaseReference reference) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // pegando a instancia do banco de dados do firebase
        reference = firebaseDatabase.getReference().child("usuario");// definindo qual o pont oque  a referencia do firebase ficara
        return reference;
    }
    public String UserRetornoId(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String retorno = null;
        try{
             retorno = firebaseUser.getUid();
        }catch (Exception e){
            retorno = "Erro retorno uid";
        }

        return retorno;
    }
}
