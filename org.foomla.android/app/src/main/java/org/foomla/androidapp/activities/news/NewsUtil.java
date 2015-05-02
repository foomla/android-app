package org.foomla.androidapp.activities.news;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.foomla.androidapp.activities.newsdetail.NewsDetailIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

public class NewsUtil {

    public static final String NEWS_IMAGE_TAG_PATTERN = "<img ";

    private static final Logger LOG = LoggerFactory.getLogger(NewsUtil.class);

    public static NewsDetailIntent createNewsDetailIntent(Context context, SyndEntry entry) {
        return new NewsDetailIntent(context, entry.getTitle(), entry.getPublishedDate().toLocaleString(),
                getDescriptionOfEntry(entry), getContentOfEntry(entry));
    }

    public static String getContentOfEntry(final SyndEntry entry) {
        @SuppressWarnings("rawtypes")
        List contents = entry.getContents();
        if (contents != null && contents.size() > 0) {
            Object o = contents.get(0);

            if (o instanceof SyndContent) {
                return ((SyndContent) o).getValue();
            }
        }

        LOG.warn("No feed entry content provided - return empty string.");
        return "";
    }

    public static String getDescriptionOfEntry(final SyndEntry entry) {
        SyndContent description = entry.getDescription();
        if (description != null) {
            return description.getValue();
        }

        LOG.warn("No feed entry description provided - return empty string.");
        return "";
    }

    public static String stripImagesFromNewsContent(final String content) {
        String strippedContent = content;

        while (strippedContent.contains(NewsUtil.NEWS_IMAGE_TAG_PATTERN)) {
            int start = strippedContent.indexOf(NewsUtil.NEWS_IMAGE_TAG_PATTERN);
            int stop = strippedContent.indexOf('>', start);

            String toStrip = strippedContent.substring(start, stop + 1);
            strippedContent = strippedContent.replaceAll(toStrip, "");
        }

        return strippedContent;
    }

    public static String getImageSourceFromNewsContent(final String content) {
        Matcher matcher = NewsUtil.NEWS_IMAGE_URL_PATTERN.matcher(content);
        if (matcher.find() && matcher.groupCount() > 0) {
            return matcher.group(1);
        }
    
        return null;
    }

    public static final Pattern NEWS_IMAGE_URL_PATTERN = Pattern.compile(
    "(?m)(?s)<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
}
