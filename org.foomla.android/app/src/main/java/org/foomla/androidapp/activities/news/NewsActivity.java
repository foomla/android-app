package org.foomla.androidapp.activities.news;

import android.content.Intent;
import android.os.Bundle;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;
import org.foomla.androidapp.async.DownloadNewsTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NewsActivity extends BaseActivityWithNavDrawer implements NewsListFragment.Callback {

    private static final Logger LOG = LoggerFactory.getLogger("[foomla] NewsActivity");

    private NewsListFragment newsListFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);
        createNavDrawer();

        newsListFragment = new NewsListFragment();
        getFragmentManager().beginTransaction().add(R.id.news_list, newsListFragment).commit();

        loadNewsFeed();
    }

    @Override
    public void onEntryClicked(final SyndEntry entry) {
        Intent i = NewsUtil.createNewsDetailIntent(this, entry);
        startActivity(i);
    }

    private void loadNewsFeed() {
        try {
            new DownloadNewsTask(this, new DownloadNewsTask.Handler() {
                    @Override
                    public void onFinish(final List<SyndEntry> feedEntries) {
                        LOG.info("Received {} feed entries", feedEntries.size());
                        newsListFragment.showFeedEntries(feedEntries);
                    }
                }).execute(new URL(getString(R.string.news_url)));

        } catch (MalformedURLException e) {
            LOG.error("Feed URL is not valid: {}", e.getMessage(), e);
        }
    }

}
