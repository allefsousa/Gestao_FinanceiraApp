package br.com.felipedeveloper.gestaofinanceira.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    // variaveis globais
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myref;
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
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        inicializaFirebase(); // chamada de metodo que inicia firebase

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_id_client_firebase))
                .requestEmail()
                .build();

        mGoogleapi = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        btnfacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,OpcoesFinanceiraActivity.class));

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
            }
        });
        firebaseAuth = FirebaseAuth.getInstance(); // pegando a instancia do OAuth  do firebase paar trabalhar com ela.

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    loginRealizadocomSucesso();
                }
            }
        };
        callbackManager = CallbackManager.Factory.create();

    }


    private void loginRealizadocomSucesso() {
        Intent i = new Intent(LoginActivity.this, OpcoesFinanceiraActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    /**
     * metodo responsavel por inicializar firebase
     * neste caso o banco de dados realtime database
     */
    public void inicializaFirebase(){
        database = FirebaseDatabase.getInstance();
        myref = database.getReference();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  callbackManager.onActivityResult(requestCode, resultCode, data);

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
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
