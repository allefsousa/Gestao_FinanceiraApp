package br.com.felipedeveloper.gestaofinanceira.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import br.com.felipedeveloper.gestaofinanceira.Helper.sectionPageAdapter;
import br.com.felipedeveloper.gestaofinanceira.R;

public class LoginEmailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // elementos globais referentes a tabs de Login e inscrição
    sectionPageAdapter adapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Login | Cadastro");

        /**
         * referencias as objetos relacionados a view de login
         */
        mViewPager = (ViewPager) findViewById(R.id.container);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setuViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /**
     * recebo uma view como parametro e informo o quais as views eu vou exibir e sseus
     * respectivos titulos.
     * metodo para montar a sequencia das tabs de login
     * @param uViewPager
     */
    private void setuViewPager(ViewPager uViewPager){
        sectionPageAdapter adapter  = new  sectionPageAdapter(getSupportFragmentManager()); // objeto de adapter para colocar o nome em cima da view certa
        adapter.addFragment(new EmailLoginFragment(),"Login");
        adapter.addFragment(new EmailCadastroFragment(),"Cadastro");
        uViewPager.setAdapter(adapter); // adiciona as tabs a pagina.
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}
