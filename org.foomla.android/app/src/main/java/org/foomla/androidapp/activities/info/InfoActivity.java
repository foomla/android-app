package org.foomla.androidapp.activities.info;

import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TitlePageIndicator;

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
                    return "Übungen";
                case 1:
                    return "Symbole";
                case 2:
                    return "Impressum";
                default:
                    return "";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);
        createNavDrawer();

        FragmentPagerAdapter adapter = new InfoFragmentAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

}
