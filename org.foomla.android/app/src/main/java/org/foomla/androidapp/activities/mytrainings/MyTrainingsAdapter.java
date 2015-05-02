package org.foomla.androidapp.activities.mytrainings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.foomla.androidapp.R;
import org.foomla.androidapp.async.LoadExerciseImageTask;
import org.foomla.androidapp.utils.EnumTextUtil;
import org.foomla.androidapp.utils.ImageUtil.ImageType;

import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.Training;
import org.foomla.api.entities.twizard.TrainingFocus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import android.content.Context;

import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyTrainingsAdapter extends BaseAdapter {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTrainingsAdapter.class);

    private List<Training> trainings;

    public MyTrainingsAdapter(final List<Training> trainings) {
        this.trainings = trainings;
    }

    @Override
    public int getCount() {
        return trainings.size();
    }

    @Override
    public Object getItem(final int position) {
        if (position < getCount()) {
            return trainings.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(R.layout.listitem_training, parent, false);
        final Training training = (Training) getItem(position);

        if (training == null) {
            LOGGER.warn("Try to create list item but item is null!");
            return view;
        }

        initializeDate(view, training);
        initializeTitle(view, training);
        initializeFocus(view, training);
        loadImages(view, training);

        return view;
    }

    private void initializeFocus(final View view, final Training training) {
        List<String> focusTexts = Lists.newArrayList();
        for (TrainingFocus trainingFocus : training.getTrainingFocus()) {
            focusTexts.add(EnumTextUtil.getText(view.getContext(), trainingFocus));
        }

        String focus = Joiner.on(", ").join(focusTexts);
        ((TextView) view.findViewById(R.id.focus)).setText(focus);
    }

    public void setImageView(final View view, final int resId, final Drawable drawable) {
        ImageView iv = (ImageView) view.findViewById(resId);
        if (iv != null) {
            iv.setImageDrawable(drawable);
        }
    }

    public void setTrainings(final List<Training> trainings) {
        this.trainings = trainings;
        notifyDataSetChanged();
    }

    private String formatDate(final Date date) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return df.format(date);
    }

    private void initializeDate(final View view, final Training training) {
        TextView textView = (TextView) view.findViewById(R.id.date);
        textView.setText(formatDate(training.getDate()));
    }

    private void initializeTitle(final View view, final Training training) {
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText(training.getTitle());
    }

    private void loadImage(final View view, final Training training, final int trainingPhase, final int resourceId) {
        List<Exercise> exercises = training.getExercises();
        if (exercises.size() > trainingPhase) {
            Exercise exercise = exercises.get(trainingPhase);
            new LoadExerciseImageTask(view.getContext(), training, ImageType.THUMBNAIL) {
                @Override
                protected void onPostExecute(final Drawable result) {
                    setImageView(view, resourceId, result);
                }
            }.execute(exercise);
        }
    }

    private void loadImages(final View view, final Training training) {
        loadImage(view, training, 0, R.id.exercise_thumb_arrival);
        loadImage(view, training, 1, R.id.exercise_thumb_warmup);
        loadImage(view, training, 2, R.id.exercise_thumb_main_one);
        loadImage(view, training, 3, R.id.exercise_thumb_main_two);
        loadImage(view, training, 4, R.id.exercise_thumb_scrimmage);
    }
}
