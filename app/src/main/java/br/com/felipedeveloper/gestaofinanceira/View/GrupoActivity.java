package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.felipedeveloper.gestaofinanceira.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class GrupoActivity extends AppCompatActivity {

    private TextView mTextMessage;
    EditText editText;
    MenuItem itemm;
    BottomNavigationView navigation;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_grupo:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_adduser:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    mensagem();
//                    return true;
//            }
//            return false;
//
//        }
//    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);

        mTextMessage = (TextView) findViewById(R.id.message);
         navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        editText = new EditText(this);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                case R.id.navigation_grupo:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_adduser:
                    mTextMessage.setText(R.string.title_dashboard);
                    mensagem();
                    return true;
            }
                return false;
            }
        });
    }

    private void mensagem() {


        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Custom view")
                .setConfirmText("Ok")
                .setCustomView(editText)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.setTitleText("Deleted!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmText(editText.getText().toString())
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    }
                })
                .show();




    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
