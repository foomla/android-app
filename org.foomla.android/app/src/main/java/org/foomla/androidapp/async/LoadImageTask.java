package org.foomla.androidapp.async;

import org.foomla.androidapp.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LoadImageTask extends AsyncTask<String, Void, Drawable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageTask.class);
    private final ImageView imageView;
    private final boolean calculateImageBounds;

    public LoadImageTask(final ImageView imageView) {
        this.imageView = imageView;
        this.calculateImageBounds = false;
    }

    public LoadImageTask(final ImageView imageView, final boolean calculateImageBounds) {
        this.imageView = imageView;
        this.calculateImageBounds = calculateImageBounds;
    }

    @Override
    protected Drawable doInBackground(final String... imageUrls) {
        if (imageUrls != null && imageUrls.length > 0) {
            String imageUrl = imageUrls[0];
            return ImageUtil.createImageFromUrl(imageUrl);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Drawable result) {
        if (result != null) {
            imageView.setImageDrawable(result);

            if (calculateImageBounds) {
                ImageUtil.calculateImageViewBounds(imageView, result);
            }
        } else {
            LOGGER.warn("Could not download image.");
        }
    }
}
