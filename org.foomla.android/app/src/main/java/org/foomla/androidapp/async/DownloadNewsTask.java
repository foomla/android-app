package org.foomla.androidapp.async;

import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.foomla.androidapp.utils.ProgressVisualizationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import android.app.Activity;

import android.os.AsyncTask;

public class DownloadNewsTask extends AsyncTask<URL, Integer, List<SyndEntry>> {

    public interface Handler {
        void onFinish(List<SyndEntry> feedEntries);
    }

    private static final Logger LOG = LoggerFactory.getLogger("DownloadNewsTask");

    private final Activity activity;

    private final Handler handler;

    public DownloadNewsTask(final Activity activity, final Handler handler) {
        this.activity = activity;
        this.handler = handler;
    }

    @Override
    protected List<SyndEntry> doInBackground(final URL... params) {
        try {
            return download(params[0]);

        } catch (FeedException e) {
            LOG.error("Feed wasn't downloaded properly: {}", e.getMessage(), e);
        } catch (IOException e) {
            LOG.error("Feed wasn't downloaded properly: {}", e.getMessage(), e);
        }

        return new ArrayList<SyndEntry>();
    }

    @Override
    protected void onPostExecute(final List<SyndEntry> syndEntries) {
        handler.onFinish(syndEntries);
        ProgressVisualizationUtil.hideProgressbar(activity);
    }

    @Override
    protected void onPreExecute() {
        ProgressVisualizationUtil.showProgressbar(activity);
    }

    private List<SyndEntry> download(final URL param) throws IOException, FeedException {
        LOG.info("Start downloading news feed from '{}'", param.toString());

        SyndFeedInput feedInput = new SyndFeedInput();
        SyndFeed feed = feedInput.build(new XmlReader(param));

        return feed.getEntries();
    }
}
