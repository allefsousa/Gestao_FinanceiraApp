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

/**
 * Classe Base para as outras  activityes para evitar  escrita de codigo redundante
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * Metodo responsavel por exibir mensagem ao usuario
     * @param context activity em questão
     * @param successType tipo de mensagem se é erro ou sucesso
     * @param s // Mensagem que se deve passar ao usuario
     */
    public void ExibirMensagem(Context context, int successType, String s) {
        new SweetAlertDialog(context, successType)
                .setTitleText(getResources().getString(R.string.app_name))
                .setContentText(s)
                .show();
    }

    /**
     * Metodo responsavel por fazer a configuração do firebase e evitar codigo duplicado
     * @param reference
     * @return
     */
    public DatabaseReference configFirebase(DatabaseReference reference) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // pegando a instancia do banco de dados do firebase
        reference = firebaseDatabase.getReference().child("financeiro").child(firebaseUser.getUid());// definindo qual o pont oque  a referencia do firebase ficara
        return reference;
    }

    /**
     * Metodo responsavel por retornar uma instancia do BD do no de usuarios
     * @param reference
     * @return
     */
    public DatabaseReference configFirebaseUsuario(DatabaseReference reference) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // pegando a instancia do banco de dados do firebase
        reference = firebaseDatabase.getReference().child("usuario");// definindo qual o pont oque  a referencia do firebase ficara
        return reference;
    }


    /**
     * Metodo responsavel por retornar a ID do usuario logado
     * que é unica e cada usuario logado no app possui a sua.
     * @return
     */
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
     * quando o usuario começa a digitar em um campo  que trabalha com valores em Reais
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
