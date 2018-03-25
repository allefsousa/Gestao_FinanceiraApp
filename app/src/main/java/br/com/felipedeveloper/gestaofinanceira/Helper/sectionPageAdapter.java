package br.com.felipedeveloper.gestaofinanceira.Helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allef on 15/03/2018.
 */

public class sectionPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mfragmentList = new ArrayList<>();
    private final List<String> titulofragment = new ArrayList<>();
    public sectionPageAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(Fragment fragment, String title) {
        mfragmentList.add(fragment);
        titulofragment.add(title);
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
