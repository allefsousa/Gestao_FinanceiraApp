package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import br.com.felipedeveloper.gestaofinanceira.R;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class BaseActivity extends AppCompatActivity {
    private NumberFormat df;
//    df = new DecimalFormat("#00.00");
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

    /**
     * metodo responsavel por  formatar o campo de valor e ja ir adiconando as virgulas
     * @return
     */
    TextWatcher onTextChangedListener(final EditText textvalor) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textvalor.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,##");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    textvalor.setText(formattedString);
                    textvalor.setSelection(textvalor.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                textvalor.addTextChangedListener(this);
            }
        };
    }
}
