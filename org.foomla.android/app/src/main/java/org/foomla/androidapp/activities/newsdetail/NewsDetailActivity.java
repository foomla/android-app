package org.foomla.androidapp.activities.newsdetail;

import org.foomla.androidapp.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class NewsDetailActivity extends SherlockFragmentActivity {

    private static final Logger LOG = LoggerFactory.getLogger("[foomla] NewsDetailActivity");

    private NewsDetailFragment detailFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailFragment = new NewsDetailFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.newsdetail, detailFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        NewsDetailIntent intent = new NewsDetailIntent(getIntent());
        setTitle(intent.getTitle());

        LOG.info("Display news: {}", intent.getTitle());
        detailFragment.displayNews(intent.getTitle(), intent.getDate(), intent.getSummary(), intent.getContent());
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {

            // Respond to the action bar's Up/Home button
            case android.R.id.home :
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
