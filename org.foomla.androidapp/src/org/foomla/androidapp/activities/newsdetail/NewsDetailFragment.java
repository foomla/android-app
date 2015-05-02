package org.foomla.androidapp.activities.newsdetail;

import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.news.NewsUtil;
import org.foomla.androidapp.utils.ExtendedTagHandler;
import org.foomla.androidapp.utils.UiUtils;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.common.base.Strings;

public class NewsDetailFragment extends SherlockFragment {

    public void displayNews(final String title, final String date, final String summary, final String content) {
        View view = getView();

        String imageUrl = NewsUtil.getImageSourceFromNewsContent(content);
        if (Strings.isNullOrEmpty(imageUrl)) {
            UiUtils.hideElement(view, R.id.image);
        } else {
            UiUtils.setSafeImageUrl(view, R.id.image, imageUrl, true);
        }

        UiUtils.setSafeText(view, R.id.title, title);
        UiUtils.setSafeText(view, R.id.date, date);
        UiUtils.setSafeText(view, R.id.summary, summary);
        UiUtils.setSafeHtmlText(view, R.id.content,
            Html.fromHtml(NewsUtil.stripImagesFromNewsContent(content), null, new ExtendedTagHandler()));

        enableLinksInContent();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_newsdetail, container, false);
    }

    private void enableLinksInContent() {
        TextView textView = UiUtils.getTextView(getView(), R.id.content);
        if (textView != null) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
