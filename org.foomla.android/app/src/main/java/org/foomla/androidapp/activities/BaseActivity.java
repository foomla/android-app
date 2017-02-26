package org.foomla.androidapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.foomla.androidapp.FoomlaApplication;

public class BaseActivity extends AppCompatActivity {

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
