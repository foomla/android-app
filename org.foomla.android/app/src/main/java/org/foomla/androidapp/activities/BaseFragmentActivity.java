package org.foomla.androidapp.activities;

import org.foomla.androidapp.FoomlaApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class BaseFragmentActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
