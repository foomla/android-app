package org.foomla.androidapp.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.foomla.androidapp.R;

public class ProgressVisualizationUtil {

    public static ImageView imageView;

    public static void hideProgressbar(final Activity activity) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                View progressBar = activity.findViewById(R.id.progressBar);
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public static void showProgressbar(final Activity activity) {
        showProgressbar(activity, null);
    }

    public static void showProgressbar(final Activity activity, int textId) {
        showProgressbar(activity, activity.getString(R.string.loading_text, activity.getString(textId)));
    }

    public static void showProgressbar(final Activity activity, final String text) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                View progressBar = activity.findViewById(R.id.progressBar);
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (text != null) {
                        TextView progressBarText = (TextView) activity.findViewById(R.id.progressBarText);
                        progressBarText.setText(text);
                    }
                }
            }
        });
    }

}
