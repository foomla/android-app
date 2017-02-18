package org.foomla.androidapp.async;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import org.foomla.androidapp.utils.ImageUtil;
import org.foomla.androidapp.utils.ImageUtil.ImageType;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadExerciseImageTask extends AsyncTask<Exercise, Void, Drawable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadExerciseImageTask.class);

    private final Context context;
    private final ImageType imageType;
    private Training training;

    public LoadExerciseImageTask(final Context context) {
        this.context = context;
        this.imageType = ImageType.NORMAL;
    }

    public LoadExerciseImageTask(final Context context, final Training training, final ImageType imageType) {
        this.context = context;
        this.training = training;
        this.imageType = imageType;
    }

    @Override
    protected Drawable doInBackground(final Exercise... parameters) {

        Exercise exercise = parameters[0];

        Drawable image = null;

        try {
            image = ImageUtil.getImage(context, training, exercise, imageType);
        } catch (Exception ex) {
            if (exercise != null) {
                LOGGER.warn("Unable to download image for exercise '{}'", exercise.getId(), ex);
            }

            cancel(true);
        }

        return image;
    }
}
