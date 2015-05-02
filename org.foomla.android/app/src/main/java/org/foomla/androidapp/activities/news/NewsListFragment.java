package org.foomla.androidapp.activities.news;

import java.util.List;

import org.foomla.androidapp.R;
import org.foomla.androidapp.utils.UiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

public class NewsListFragment extends Fragment {

    private static final Logger LOG = LoggerFactory.getLogger("[foomla] NewsListFragment");

    public interface Callback {
        void onEntryClicked(SyndEntry entry);
    }

    private Callback callback;
    private ListView listView;
    private NewsListAdapter listAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newslist, container, false);
        listView = UiUtils.getListView(view, R.id.feed_list);

        setupOnEntryClick(listView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Activity owner = getActivity();

        if (owner instanceof Callback) {
            callback = (Callback) owner;
        }

        if (listAdapter == null) {
            setupListAdapter(owner);
        }
    }

    private void setupListAdapter(final Activity owner) {
        listAdapter = new NewsListAdapter(owner);

        if (listView != null) {
            listView.setAdapter(listAdapter);
        } else {
            LOG.warn("No ListView found for displaying feed entries");
        }
    }

    public void showFeedEntries(final List<SyndEntry> feedEntries) {
        if (listAdapter != null && feedEntries != null) {
            listAdapter.setFeedEntries(feedEntries);
            listAdapter.notifyDataSetChanged();
        }
    }

    private void setupOnEntryClick(final ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view, final int position,
                        final long id) {

                    SyndEntry entry = (SyndEntry) parent.getAdapter().getItem(position);
                    if (callback != null) {
                        callback.onEntryClicked(entry);
                    }
                }
            });
    }
}
