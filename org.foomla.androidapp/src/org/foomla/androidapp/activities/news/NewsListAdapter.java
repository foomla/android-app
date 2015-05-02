package org.foomla.androidapp.activities.news;

import java.util.ArrayList;
import java.util.List;

import org.foomla.androidapp.R;
import org.foomla.androidapp.async.LoadImageTask;
import org.foomla.androidapp.utils.UiUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

public class NewsListAdapter extends BaseAdapter {

    private static final int VIEW_RESOURCE = R.layout.listitem_feedentry;

    private final Context context;

    private List<SyndEntry> feedEntries;

    public NewsListAdapter(final Context context) {
        this.context = context;
        this.feedEntries = new ArrayList<SyndEntry>(0);
    }

    @Override
    public int getCount() {
        return feedEntries.size();
    }

    @Override
    public Object getItem(final int position) {
        if (position < getCount()) {
            return feedEntries.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        SyndEntry feedEntry = (SyndEntry) getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = inflater.inflate(VIEW_RESOURCE, parent, false);

        if (feedEntry != null) {
            if (position == 0) {
                final ImageView imageView = (ImageView) entryView.findViewById(R.id.image);
                String content = NewsUtil.getContentOfEntry(feedEntry);
                if (content != null) {
                    String imageUrl = NewsUtil.getImageSourceFromNewsContent(content);
                    if (imageUrl != null) {
                        new LoadImageTask(imageView) {
                            @Override
                            protected void onPostExecute(android.graphics.drawable.Drawable result) {
                                super.onPostExecute(result);
                                imageView.setVisibility(View.VISIBLE);
                            };
                        }.execute(imageUrl);
                    }
                }
            }
            UiUtils.setSafeText(entryView, R.id.title, feedEntry.getTitle());
            UiUtils.setSafeText(entryView, R.id.abstractText, feedEntry.getDescription().getValue());
            UiUtils.setSafeText(entryView, R.id.creationDate, feedEntry.getPublishedDate().toLocaleString());
        }

        return entryView;
    }

    public void setFeedEntries(final List<SyndEntry> feedEntries) {
        this.feedEntries = feedEntries;
    }
}
