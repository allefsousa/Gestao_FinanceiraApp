package br.com.felipedeveloper.gestaofinanceira.View;

import android.animation.Animator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    Thread splashTread;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Animation scaledown;
    Animation scaleup;
    @BindView(R.id.imageviewlogin)
            ImageView imagelogin;
    ImageView imageView;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        firebaseAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        scaledown = AnimationUtils.loadAnimation(this, R.anim.down);
        scaleup = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Pegando a instancia do usuario caso ele esteja logado
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
                     * caso o usuario esteja logado é enviado para a tela de menu , caso nao esteja precisara fazer o login e é direcionado para tela.
                     */
                    if (firebaseUser != null) {
                        imagelogin.setAnimation(scaledown);
                        Intent i = new Intent(SplashActivity.this, OpcoesFinanceiraActivity.class);
                        startActivity(i);
                    } else {
                        imagelogin.setAnimation(scaledown);
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                    }

                } catch (Exception e) {

                }


            }
        };
        splashTread.start();
    }
}
