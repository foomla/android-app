package org.foomla.androidapp.utils;

import org.foomla.androidapp.async.LoadImageTask;

import android.text.Spanned;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public final class UiUtils {

    public static ImageView getImageView(final View parent, final int resId) {
        View child = getView(parent, resId);

        if (child instanceof ImageView) {
            return (ImageView) child;
        }

        return null;
    }

    public static ListView getListView(final View parent, final int resId) {
        View child = getView(parent, resId);

        if (child instanceof ListView) {
            return (ListView) child;
        }

        return null;
    }

    public static TextView getTextView(final View parent, final int resId) {
        View child = getView(parent, resId);

        if (child instanceof TextView) {
            return (TextView) child;
        }

        return null;
    }

    public static View getView(final View parent, final int resId) {
        if (parent != null && resId > 0) {
            return parent.findViewById(resId);
        }

        return null;
    }

    public static void hideElement(final View parent, final int resId) {
        View view = getView(parent, resId);
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = 0;
        }
    }

    public static void setSafeHtmlText(final View parent, final int textViewId, final Spanned spanned) {
        TextView textView = getTextView(parent, textViewId);

        if (textView != null) {
            textView.setText(spanned);
        }
    }

    public static void setSafeImageUrl(final View parent, final int imageViewId, final String imageUrl) {
        setSafeImageUrl(parent, imageViewId, imageUrl, false);
    }

    public static void setSafeImageUrl(final View parent, final int imageViewId, final String imageUrl,
            final boolean calculateImageBounds) {
        ImageView imageView = getImageView(parent, imageViewId);

        if (imageView != null) {
            new LoadImageTask(imageView, calculateImageBounds).execute(imageUrl);
        }
    }

    public static void setSafeText(final View parent, final int textViewId, final String text) {
        TextView textView = getTextView(parent, textViewId);

        if (textView != null) {
            textView.setText(text);
        }
    }

    public static String getSafeTextOrNull(final View parent, final int textViewId) {
        TextView textView = getTextView(parent, textViewId);

        if (textView != null) {
            CharSequence text = textView.getText();
            return text == null ? null : text.toString();
        }

        return null;
    }

    private UiUtils() { }
}
