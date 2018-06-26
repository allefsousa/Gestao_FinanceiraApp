package br.com.felipedeveloper.gestaofinanceira.Ajuda;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * classe reponsaver por montar o adapter da pogina de login por email
 * nesta etapa é onde você passa qual adapter e qual nome sera exibido na tabs sobre ele.
 */
public class sectionPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mfragmentList = new ArrayList<>(); // lista de fragments da tab
    private final List<String> titulofragment = new ArrayList<>(); // lista de titulos da tab
    public sectionPageAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(Fragment fragment, String title) {
        mfragmentList.add(fragment); // adiconando fragment
        titulofragment.add(title); // adicionando titulo
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titulofragment.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mfragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mfragmentList.size();
    }
}
