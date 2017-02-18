package org.foomla.androidapp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.foomla.androidapp.R;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class);

    public enum ImageType {

        NORMAL("exercise_%s.png"),
        THUMBNAIL("exercise_%s_thumb.png"),
        X_THUMBNAIL("exercise_%s_xthumb.png");

        private final String imageName;

        ImageType(final String imageName) {
            this.imageName = imageName;
        }

        public String getImageFileName(final int exerciseId) {
            return String.format(imageName, exerciseId);
        }
    }

    public static Drawable createImageFromUrl(final String urlPath) {
        Object content = null;
        try {
            URL url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(true);
            content = connection.getContent();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        InputStream is = (InputStream) content;
        Drawable image = Drawable.createFromStream(is, "src");
        return image;
    }

    public static Drawable getImage(final Context context, final Training training, final Exercise exercise,
                                    final ImageType imageType) {
        Drawable drawable;

        if (training != null && training.getId() != null && training.getId() > 0) {
            drawable = getLocalImage(context, training, exercise, imageType);

            if (drawable == null) {
                drawable = getRemoteImage(context, training, exercise, imageType);
            }
        } else {
            drawable = getRemoteImage(context, training, exercise, imageType);
        }

        return drawable;
    }

    public static String getImageUrl(final Context context, final Training training, final Exercise exercise) {
        return getImageUrl(context, training, exercise, ImageType.NORMAL);
    }

    public static String getImageUrl(final Context context, final Training training, final Exercise exercise,
            final ImageType imageType) {
        String imageName = imageType.getImageFileName(exercise.getId());
        String imageUrl = context.getString(R.string.exercise_image_url);
        return imageUrl + imageName;
    }

    public static Drawable getLocalImage(final Context context, final Training training, final Exercise exercise,
            final ImageType imageType) {

        try {
            File imageDirectory = FileUtils.getImageDirectory(context, training.getId());
            File image = getImageFile(exercise, imageType, imageDirectory);

            LOGGER.info("Load local image: " + image.getAbsoluteFile());
            return Drawable.createFromPath(image.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Unable to load image from local storage.");
        }

        return null;
    }

    public static Drawable getRemoteImage(final Context context, final Training training, final Exercise exercise,
            final ImageType imageType) {
        String url = getImageUrl(context, training, exercise, imageType);
        return createImageFromUrl(url);
    }

    private static File getImageFile(final Exercise exercise, final ImageType imageType, final File imageDirectory) {
        return new File(imageDirectory, imageType.getImageFileName(exercise.getId()));
    }

    public static void calculateImageViewBounds(final ImageView imageView, final Drawable drawable) {
        int currentWidth = imageView.getWidth();
        float i = ((float) currentWidth) / ((float) drawable.getIntrinsicWidth());
        float imageHeight = i * (drawable.getIntrinsicHeight());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(currentWidth, (int) imageHeight));
    }
}
