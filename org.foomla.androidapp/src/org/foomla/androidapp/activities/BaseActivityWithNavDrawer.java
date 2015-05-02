package org.foomla.androidapp.activities;

import org.foomla.androidapp.FoomlaApplication;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class BaseActivityWithNavDrawer extends SherlockFragmentActivity {

    private ActivityNavDrawer navDrawer;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (navDrawer != null) {
                    navDrawer.toggleNavDrawer();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void createNavDrawer() {
        navDrawer = new ActivityNavDrawer(this, getSupportActionBar());
        navDrawer.create();
    }

    protected ActivityNavDrawer getNavDrawer() {
        return navDrawer;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (navDrawer != null) {
            navDrawer.syncState();
        }
    }

    public FoomlaApplication getFoomlaApplication() {
        return (FoomlaApplication) this.getApplication();
    }
}
