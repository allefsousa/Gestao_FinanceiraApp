package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.felipedeveloper.gestaofinanceira.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class GrupoActivity extends AppCompatActivity {

    private TextView mTextMessage;

    MenuItem itemm;
    BottomNavigationView navigation;
    Fragment currentFragment = null;
    FragmentTransaction ft;

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





        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                case R.id.navigation_grupo:

                    return true;
                case R.id.navigation_adduser:

                    getSupportFragmentManager().beginTransaction().add(R.id.container,new testeFragment()).commit();
                    return true;
                    case R.id.lancamentos:
                        getSupportFragmentManager().beginTransaction().add(R.id.container,new testeFragment()).commit();
                        break;
            }
                return false;
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();

    }





}
