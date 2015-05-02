package org.foomla.androidapp.activities;

import org.foomla.androidapp.FoomlaApplication;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class BaseActivity extends SherlockActivity {

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
