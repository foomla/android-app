package org.foomla.androidapp.activities;

import android.app.Activity;
import android.view.MenuItem;

import org.foomla.androidapp.FoomlaApplication;

public class BaseActivity extends Activity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    protected FoomlaApplication getFoomlaApplication() {
        return (FoomlaApplication) this.getApplication();
    }

}
