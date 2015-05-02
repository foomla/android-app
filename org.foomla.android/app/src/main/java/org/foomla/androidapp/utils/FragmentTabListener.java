package org.foomla.androidapp.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class FragmentTabListener<T extends Fragment> implements ActionBar.TabListener {

    private final Activity activity;
    private Fragment fragment;
    private final Bundle fragmentArguments;
    private final Class<T> fragmentClass;
    private final String tag;

    public FragmentTabListener(Activity activity, String tag, Class<T> clz, Bundle fragmentArguments) {
        this.activity = activity;
        this.tag = tag;
        this.fragmentClass = clz;
        this.fragmentArguments = fragmentArguments;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        if (fragment == null) {
            fragment = Fragment.instantiate(activity, fragmentClass.getName(), fragmentArguments);
            fragment.setArguments(fragmentArguments);
            ft.replace(android.R.id.content, fragment, tag);
        }
        else {
            ft.attach(fragment);
        }

        ft.commit();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        if (fragment != null) {
            ft.detach(fragment);
        }

        ft.commit();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }
}
