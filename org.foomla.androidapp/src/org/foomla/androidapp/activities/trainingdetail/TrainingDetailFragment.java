package org.foomla.androidapp.activities.trainingdetail;

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

import com.actionbarsherlock.app.SherlockFragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.graphics.drawable.Drawable;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

public class TrainingDetailFragment extends SherlockFragment {

    public interface ActionHandler {
        Training getTraining();

        void onDisplayExerciseDetails(int trainingPhase);

        void onEditTrainingPhase(int trainingPhase);

        void onRandomizeTrainingPhase(int trainingPhase);

        void onSaveTraining();
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    private ActionHandler actionHandler;

    private FragmentActivity activity;

    private View view;

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    public void hideRandomizeProgessbar(final int trainingPhase) {
        int randomizeButtonId = getRandomizeButtonId(trainingPhase);
        view.findViewById(randomizeButtonId).setVisibility(View.VISIBLE);

        int randomizeProgessbarId = getRandomizeProgessbarId(trainingPhase);
        view.findViewById(randomizeProgessbarId).setVisibility(View.GONE);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        this.activity = getActivity();
        if (activity instanceof ActionHandler) {
            setActionHandler((ActionHandler) activity);
        } else {
            throw new IllegalArgumentException("Parent Activity must implement FragmentCallback interface");
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.edittraining, menu);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_trainingdetail, container, false);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.save_training :
                getActionHandler().onSaveTraining();
                return true;
        }

        return false;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTraining(getActionHandler().getTraining());

        initializeDetailsListener();
        initializeEditButtons();
        initializeRandomButtons();
    }

    public void setActionHandler(final ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    public void setImageView(final int resId, final Drawable drawable) {
        ImageView iv = getImageView(resId);
        if (iv != null) {
            iv.setImageDrawable(drawable);
        }
    }

    public void setTextViewText(final int resId, final String text) {
        TextView tv = getTextView(resId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    public void showRandomizeProgessBar(final int trainingPhase) {
        int randomizeButtonId = getRandomizeButtonId(trainingPhase);
        view.findViewById(randomizeButtonId).setVisibility(View.GONE);

        int randomizeProgessbarId = getRandomizeProgessbarId(trainingPhase);
        view.findViewById(randomizeProgessbarId).setVisibility(View.VISIBLE);
    }

    public void trainingChanged() {
        setTraining(getActionHandler().getTraining());
    }

    private View.OnClickListener createDetailListener(final int trainingPhase) {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getActionHandler().onDisplayExerciseDetails(trainingPhase);
            }
        };
    }

    private View.OnClickListener createEditListener(final int trainingPhase) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getActionHandler().onEditTrainingPhase(trainingPhase);
            }
        };

        return listener;
    }

    private View.OnClickListener createRandomListener(final int trainingPhase) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getActionHandler().onRandomizeTrainingPhase(trainingPhase);
            }
        };

        return listener;
    }

    private String formatDate(final Date date) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return df.format(date);
    }

    private ImageView getImageView(final int resId) {
        View parent = getView();
        if (parent != null) {
            return (ImageView) parent.findViewById(resId);
        }

        return null;
    }

    private int getRandomizeButtonId(final int trainingPhase) {
        int buttonId = 0;
        switch (trainingPhase) {

            case 0 :
                buttonId = R.id.exercise_arrival_random;
                break;

            case 1 :
                buttonId = R.id.exercise_warmup_random;
                break;

            case 2 :
                buttonId = R.id.exercise_main_one_random;
                break;

            case 3 :
                buttonId = R.id.exercise_main_two_random;
                break;

            case 4 :
                buttonId = R.id.exercise_scrimmage_random;
                break;

            default :
                break;
        }

        return buttonId;
    }

    private int getRandomizeProgessbarId(final int trainingPhase) {
        int buttonId = 0;
        switch (trainingPhase) {

            case 0 :
                buttonId = R.id.exercise_arrival_random_pg;
                break;

            case 1 :
                buttonId = R.id.exercise_warmup_random_pg;
                break;

            case 2 :
                buttonId = R.id.exercise_main_one_random_pg;
                break;

            case 3 :
                buttonId = R.id.exercise_main_two_random_pg;
                break;

            case 4 :
                buttonId = R.id.exercise_scrimmage_random_pg;
                break;

            default :
                break;
        }

        return buttonId;
    }

    private TextView getTextView(final int resId) {
        View parent = getView();
        if (parent != null) {
            return (TextView) parent.findViewById(resId);
        }

        return null;
    }

    private void initializeDetailsListener() {
        View view = getView();
        if (view == null) {
            return;
        }

        View arrival = view.findViewById(R.id.arrival_container);
        View warmup = view.findViewById(R.id.warmup_container);
        View mainOne = view.findViewById(R.id.main_one_container);
        View mainTwo = view.findViewById(R.id.main_two_container);
        View scrimmage = view.findViewById(R.id.scrimmage_container);

        arrival.setOnClickListener(createDetailListener(0));
        warmup.setOnClickListener(createDetailListener(1));
        mainOne.setOnClickListener(createDetailListener(2));
        mainTwo.setOnClickListener(createDetailListener(3));
        scrimmage.setOnClickListener(createDetailListener(4));
    }

    private void initializeEditButton(final View root, final int trainingPhase, final int buttonId) {
        View button = root.findViewById(buttonId);
        button.setOnClickListener(createEditListener(trainingPhase));
    }

    private void initializeEditButtons() {
        View view = getView();
        if (view == null) {
            return;
        }

        initializeEditButton(view, 0, R.id.exercise_arrival_edit);
        initializeEditButton(view, 1, R.id.exercise_warmup_edit);
        initializeEditButton(view, 2, R.id.exercise_main_one_edit);
        initializeEditButton(view, 3, R.id.exercise_main_two_edit);
        initializeEditButton(view, 4, R.id.exercise_scrimmage_edit);
    }

    private void initializeRandomButton(final View root, final int trainingPhase, final int buttonId) {
        View button = root.findViewById(buttonId);
        button.setOnClickListener(createRandomListener(trainingPhase));
    }

    private void initializeRandomButtons() {
        View view = getView();
        if (view == null) {
            return;
        }

        initializeRandomButton(view, 0, R.id.exercise_arrival_random);
        initializeRandomButton(view, 1, R.id.exercise_warmup_random);
        initializeRandomButton(view, 2, R.id.exercise_main_one_random);
        initializeRandomButton(view, 3, R.id.exercise_main_two_random);
        initializeRandomButton(view, 4, R.id.exercise_scrimmage_random);
    }

    private void loadImage(final int resId, final Training training, final Exercise exercise) {
        new LoadExerciseImageTask(this.activity, training, ImageType.THUMBNAIL) {
            @Override
            protected void onPostExecute(final Drawable result) {
                setImageView(resId, result);
            }
        }.execute(exercise);
    }

    private void setComment(final Training training) {
        setTextViewText(R.id.comment, training.getComment());
    }

    private void setDate(final Training training) {
        setTextViewText(R.id.date, formatDate(training.getDate()));
    }

    private void setExercises(final Training training) {
        final List<Exercise> exercises = training.getExercises();

        if (exercises != null && !exercises.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                setTrainingPhase(training, i, exercises.get(i));
            }
        }
    }

    private void setHeader(final Training training) {
        if (training.getTitle() == null) {
            if (getView() != null) {
                View view = getView().findViewById(R.id.header);
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        } else {
            setTitle(training);
            setComment(training);
            setDate(training);
        }
    }

    private void setTitle(final Training training) {
        setTextViewText(R.id.title, training.getTitle());
    }

    private void setTraining(final Training training) {
        if (training == null) {
            return;
        }

        setHeader(training);
        setExercises(training);
    }

    private void setTrainingPhase(final Training training, final int phase, final Exercise exercise) {
        TrainingFocus trainingFocus = exercise.getTrainingFocus();
        String exerciseFocusText = trainingFocus != null ? EnumTextUtil.getText(this.activity, trainingFocus) : "";

        switch (phase) {

            case 0 :
                setTextViewText(R.id.exercise_arrival_text, exercise.getTitle());
                setTextViewText(R.id.exercise_arrival_focus_text, exerciseFocusText);
                loadImage(R.id.exercise_arrival_image, training, exercise);
                break;

            case 1 :
                setTextViewText(R.id.exercise_warmup_text, exercise.getTitle());
                setTextViewText(R.id.exercise_warmup_focus_text, exerciseFocusText);
                loadImage(R.id.exercise_warmup_image, training, exercise);
                break;

            case 2 :
                setTextViewText(R.id.exercise_main_one_text, exercise.getTitle());
                setTextViewText(R.id.exercise_main_one_focus_text, exerciseFocusText);
                loadImage(R.id.exercise_main_one_image, training, exercise);
                break;

            case 3 :
                setTextViewText(R.id.exercise_main_two_text, exercise.getTitle());
                setTextViewText(R.id.exercise_main_two_focus_text, exerciseFocusText);
                loadImage(R.id.exercise_main_two_image, training, exercise);
                break;

            case 4 :
                setTextViewText(R.id.exercise_scrimmage_text, exercise.getTitle());
                setTextViewText(R.id.exercise_scrimmage_focus_text, exerciseFocusText);
                loadImage(R.id.exercise_scrimmage_image, training, exercise);
                break;
        }
    }
}
