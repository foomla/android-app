package org.foomla.androidapp.activities.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;


public class InfoActivity extends BaseActivityWithNavDrawer {

    private class InfoFragmentAdapter extends FragmentPagerAdapter {

        public InfoFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ExercisesInfoFragment();
                case 1:
                    return new SymbolsInfoFragment();
                case 2:
                    return new ImprintInfoFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.info_exercises);
                case 1:
                    return getString(R.string.info_symbols);
                case 2:
                    return getString(R.string.info_imprint);
                default:
                    return "";
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setElevation(0);
        FragmentPagerAdapter adapter = new InfoFragmentAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        ViewCompat.setElevation(tabs, 8);
    }

}
