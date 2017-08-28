package org.foomla.androidapp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.common.base.Strings;

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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class);
    private static final Map<String, Drawable> imageCache = new HashMap<>(125);
    private static final Pattern EXERCISE_ID_PATTERN = Pattern.compile(".*exercise_(\\d+)\\.png");

    public static Drawable createImageFromUrl(final String urlPath) {
        Object content;
        try {
            URL url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(true);
            content = connection.getContent();

            InputStream is = (InputStream) content;
            return Drawable.createFromStream(is, "src");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Drawable getImage(final Context context, final Training training, final Exercise exercise,
                                    final ImageType imageType) {
        Drawable drawable;

        if (training != null && training.getId() != null && training.getId() > 0) {
            drawable = getLocalImage(context, training, exercise, imageType);

            if (drawable == null) {
                drawable = getRemoteImage(context, exercise, imageType);
            }
        } else {
            drawable = getRemoteImage(context, exercise, imageType);
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

    private static String getImageUrl(final Context context, final int exerciseId, final ImageType imageType) {
        return context.getString(R.string.exercise_image_url)
                + imageType.getImageFileName(exerciseId);
    }

    private static Drawable getLocalImage(final Context context, final Training training, final Exercise exercise,
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

    private static Drawable getRemoteImage(final Context context, final Exercise exercise, ImageType imageType) {
        if (exercise.getImages() == null || exercise.getImages().isEmpty()) {
            return null;
        }

        final String exerciseImageUrl = exercise.getImages().get(0);

        try {
            final int exerciseId = extractIdFromImageUrl(exerciseImageUrl);
            final String imageUrl = getImageUrl(context, exerciseId, imageType);

            Drawable image = imageCache.get(imageUrl);

            if (image == null) {
                LOGGER.info("Image not found in cache -> load from remote location: {}", imageUrl);

                image = createImageFromUrl(imageUrl);
                if(imageType != ImageType.NORMAL) {
                    imageCache.put(imageUrl, image);
                }
            } else {
                LOGGER.info("Image found in cache: {}", imageUrl);
            }

            return image;
        } catch (ExerciseIdNotFoundException e) {
            LOGGER.error("Did not find exercise id from image url: {}", exerciseImageUrl);
            return null;
        }
    }

    private static File getImageFile(final Exercise exercise, final ImageType imageType, final File imageDirectory) {
        return new File(imageDirectory, imageType.getImageFileName(exercise.getId()));
    }

    private static int extractIdFromImageUrl(String imageUrl) throws ExerciseIdNotFoundException {
        if (imageUrl == null || Strings.isNullOrEmpty(imageUrl)) {
            throw new ExerciseIdNotFoundException();
        }

        Matcher matcher = EXERCISE_ID_PATTERN.matcher(imageUrl);
        if (matcher.matches() && matcher.groupCount() >= 1) {
            return Integer.parseInt(matcher.group(1));
        }

        throw new ExerciseIdNotFoundException();
    }

    public static void calculateImageViewBounds(final ImageView imageView, final Drawable drawable) {
        int currentWidth = imageView.getWidth();
        float i = ((float) currentWidth) / ((float) drawable.getIntrinsicWidth());
        float imageHeight = i * (drawable.getIntrinsicHeight());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(currentWidth, (int) imageHeight));
    }

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

    private static class ExerciseIdNotFoundException extends Exception {
    }
}
