package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.felipedeveloper.gestaofinanceira.R;

public class SplashActivity extends AppCompatActivity {
    Thread splashTread;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();



        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Pegando a instancia do Usuario caso ele esteja logado
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            }
        };

        startAnimation();
    }

    // tempo de pausa da Splsh Screem
    public void startAnimation() {


        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;

                    //

                    while (waited < 2000) {

                        sleep(100);
                        waited += 100;


                    }
                    /**
                     * caso o Usuario esteja logado é enviado para a tela de menu , caso nao esteja precisara fazer o login e é direcionado para tela.
                     */
                    if (firebaseUser != null) {

                        Intent i = new Intent(SplashActivity.this, MenuActivity.class);
                        startActivity(i);

                    } else {

                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                    }

                } catch (Exception e) {

                }


            }
        };
        splashTread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
        startAnimation();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        firebaseAuth.addAuthStateListener(authStateListener);
        startAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
