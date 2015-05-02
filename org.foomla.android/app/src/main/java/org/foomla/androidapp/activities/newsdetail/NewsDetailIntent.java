package org.foomla.androidapp.activities.newsdetail;

import android.content.Context;
import android.content.Intent;

public class NewsDetailIntent extends Intent {

    public static final String EXTRA_TITLE = "newsdetail.extra.title";
    public static final String EXTRA_DATE = "newsdetail.extra.date";
    public static final String EXTRA_SUMMARY = "newsdetail.extra.summary";
    public static final String EXTRA_CONTENT = "newsdetail.extra.content";

    public NewsDetailIntent(final Context context, final String title, final String date, final String summary,
            final String content) {

        super(context, NewsDetailActivity.class);
        putExtra(EXTRA_TITLE, title);
        putExtra(EXTRA_DATE, date);
        putExtra(EXTRA_SUMMARY, summary);
        putExtra(EXTRA_CONTENT, content);
    }

    public NewsDetailIntent(final Intent intent) {
        super(intent);
    }

    public String getTitle() {
        return getStringExtra(EXTRA_TITLE);
    }

    public String getDate() {
        return getStringExtra(EXTRA_DATE);
    }

    public String getSummary() {
        return getStringExtra(EXTRA_SUMMARY);
    }

    public String getContent() {
        return getStringExtra(EXTRA_CONTENT);
    }
}
