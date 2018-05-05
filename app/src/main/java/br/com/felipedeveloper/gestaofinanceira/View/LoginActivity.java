package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.felipedeveloper.gestaofinanceira.Model.Usuario;
import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.views.BannerSlider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    // variaveis globais
    FirebaseAuth auth;
    FirebaseDatabase database;
    private DatabaseReference reference;
    BannerSlider sliderLayout;
    Usuario user;
    @BindView(R.id.btnfacebook)
    Button btnfacebookLogin;
    @BindView(R.id.btngoogle)
    Button btngooglelogin;
    @BindView(R.id.btnemail)
    Button btnemaillogin;
    public static final int SING_IN_CODE = 777;

    private GoogleApiClient mGoogleapi;
    AuthCredential authCredential;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplo_login);
        ButterKnife.bind(this);
        inicializaFirebase(); // chamada de metodo que inicia firebase
        user = new Usuario();
        services_googleLogin();
        List<Banner> banners=new ArrayList<>();
        banners.add(new DrawableBanner(R.drawable.um));
        banners.add(new DrawableBanner(R.drawable.dois));
        banners.add(new DrawableBanner(R.drawable.tres));


        sliderLayout = findViewById(R.id.slider);
        sliderLayout.setBanners(banners);

        btnfacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                //startActivity(new Intent(LoginActivity.this,OpcoesFinanceiraActivity.class));

            }
        });
        btnemaillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,LoginEmailActivity.class));
            }
        });
        btngooglelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleapi);
                startActivityForResult(intent, SING_IN_CODE);
               // loginRealizadocomSucesso();


            }
        });
        firebaseAuth = FirebaseAuth.getInstance(); // pegando a instancia do OAuth  do firebase paar trabalhar com ela.
        firebaseAuth.signOut(); // deslogando

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    firebaseUser.getUid();
                    salvarUsuarioBD(firebaseUser); // TODO: 08/04/2018  testar com google e facebook
                    loginRealizadocomSucesso();
                }
            }
        };
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handlerFacebookToken(loginResult); // metodo para fazer o login no firebase

                    }

                    @Override
                    public void onCancel() {
                        Snackbar.make(findViewById(android.R.id.content), "Erro na Autenticação",
                                Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        if(error instanceof FacebookAuthorizationException){


                        }
                        Snackbar.make(findViewById(android.R.id.content), "Falha na Autenticação" + error.getMessage(),
                                Snackbar.LENGTH_LONG).show();
                    }

                }


        );

    }

    private void inicializaFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("usuario");
    }

    private void salvarUsuarioBD(FirebaseUser firebaseUser) {
        user.setUsuarioEmail(firebaseUser.getEmail());
        user.setUsuarioNome(firebaseUser.getDisplayName());
        user.setFotoUrl(firebaseUser.getPhotoUrl().toString());
        user.setIdUsuario(firebaseUser.getUid());
        reference.child(user.getIdUsuario()).setValue(user);

    }

    private void services_googleLogin() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_id_client_firebase))
                .requestEmail()
                .build();

        mGoogleapi = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    private void handlerFacebookToken(LoginResult loginResult) {
        authCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        SingInFirebase(authCredential);
    }


    private void loginRealizadocomSucesso() {
        Intent i = new Intent(LoginActivity.this, OpcoesFinanceiraActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * metodo responsavel por inicializar firebase
     * neste caso o banco de dados realtime database
     */

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // verificando  o codigo recebido do GoogleSing
        if (requestCode == SING_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handlerGoogleSingResult(result);
        }
    }

    /**
     * metodo responsavel por determinar oque sera feito com o resultado do login
     *
     * @param result
     */
    private void handlerGoogleSingResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            firebaseAuthsignInGoogle(result.getSignInAccount());

        } else {
            Toast.makeText(LoginActivity.this, "Falha ao Iniciar Sessão", Toast.LENGTH_LONG).show();

        }
    }

    private void firebaseAuthsignInGoogle(GoogleSignInAccount signInAccount) {

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        SingInFirebase(credential);
    }

    private void SingInFirebase(AuthCredential cred) {
        firebaseAuth.signInWithCredential(cred).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Snackbar.make(findViewById(android.R.id.content), "Login Realizado Com Sucesso",
                            Snackbar.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, " erro" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
