package org.foomla.androidapp.activities.exercisebrowser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.common.collect.Lists;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.GoProDialogFragment;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;
import org.foomla.androidapp.activities.exercisedetail.ExerciseDetailIntent;
import org.foomla.androidapp.domain.AgeClass;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.TrainingFocus;
import org.foomla.androidapp.domain.TrainingPhase;
import org.foomla.androidapp.preferences.FoomlaPreferences;
import org.foomla.androidapp.service.ExerciseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciseBrowserActivity extends BaseActivityWithNavDrawer
        implements ExerciseBrowserFragment.FragmentCallback, FilterDialogFragment.FilterDialogListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseBrowserActivity.class);

    private ExerciseBrowserFragment exerciseBrowserFragment;

    private List<Exercise> unfilteredExercises;

    private List<Exercise> exercises;

    private Integer trainingPhase;

    private ExerciseFilter exerciseFilter;

    @Override
    public ExerciseFilter getExerciseFilter() {
        return exerciseFilter;
    }

    @Override
    public List<Exercise> getExercises() {
        return exercises;
    }

    @Override
    public boolean isShowSelectButton() {
        return trainingPhase != null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exercisebrowser;
    }

    @Override
    public void onCreate(final Bundle savedInstanceBundle) {
        trainingPhase = getTrainingPhaseFromIntent();
        super.onCreate(savedInstanceBundle);

        exercises = new ArrayList<>();
        exerciseFilter = FoomlaPreferences.getExerciseFilter(this);

        exerciseBrowserFragment = new ExerciseBrowserFragment();
        getFragmentManager().beginTransaction().replace(R.id.exercise_browser, exerciseBrowserFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercisebrowser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                openFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSelectExercise(final Exercise exercise) {
        Integer trainingPhase = getTrainingPhaseFromIntent();
        Intent i = new ExerciseBrowserIntent(this, exercise, trainingPhase);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onShowExerciseDetails(final Exercise exercise) {
        Intent i = new ExerciseDetailIntent(this, exercise);
        startActivity(i);
    }

    @Override
    public void onClearFilter() {
        this.exerciseFilter = null;
        FoomlaPreferences.clearExerciseFilter(this);
        this.exercises = applyFilter(this.unfilteredExercises);
        exerciseBrowserFragment.notifyDataChanged();
    }

    @Override
    public void onGoPro() {
        openGoProDialog();
    }

    @Override
    public void dismissGoPro() {
        FoomlaPreferences.setBoolean(this, FoomlaPreferences.Preference.DISMISS_GO_PRO_EXERCISES, true);
    }

    private void openFilter() {
        if (getFoomlaApplication().isProVersion()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FilterDialogFragment filterFragment = new FilterDialogFragment();
            if (this.exerciseFilter != null) {
                filterFragment.setExerciseFilter(this.exerciseFilter);
            }
            filterFragment.show(fragmentManager, "filter");

        } else {
            openGoProDialog();
        }
    }

    private void openGoProDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        GoProDialogFragment filterFragment = new GoProDialogFragment();
        filterFragment.show(fragmentManager, "goPro");
    }

    private Integer getTrainingPhaseFromIntent() {
        Intent i = getIntent();
        Integer trainingPhase = i.getIntExtra(ExerciseBrowserIntent.EXTRA_TRAINING_PHASE, -1);
        return trainingPhase >= 0 ? trainingPhase : null;
    }

    private void initialize() {
        if (trainingPhase != null) {
            initializeWithTrainingPhase(trainingPhase);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            initializeWithAllExercises();
        }
    }

    @Override
    protected void initDrawer(Toolbar toolbar) {
        if (trainingPhase != null) {
            // skip init
        } else {
            super.initDrawer(toolbar);
        }
    }

    private void initializeWithAllExercises() {
        LOGGER.info("Initialize with all exercises");

        try {
            final FoomlaApplication foomlaApplication = (FoomlaApplication) getApplication();
            final ExerciseService service = foomlaApplication.getExerciseService();
            final ExerciseService.Callback<List<Exercise>> callback = new ExerciseService.Callback<List<Exercise>>() {
                @Override
                public void onResult(List<Exercise> result) {
                    setExercises(result);
                }

                @Override
                public void onFailure() {
                    setExercises(Lists.<Exercise>newArrayList());
                }
            };

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        service.list(callback, foomlaApplication.isProVersion());
                    } catch (IOException e) {
                        LOGGER.error("Fetching exercises failed", e);
                    }

                    return null;
                }
            }.execute();
        } catch (IOException ioe) {
            LOGGER.error("Unable to read exercises from JSON", ioe);
            // TODO inform user
        }
    }

    private void initializeWithTrainingPhase(final int trainingPhase) {
        LOGGER.info("Initialize with exercises for training phase '" + trainingPhase + "'");

        try {
            ExerciseService service = ((FoomlaApplication) getApplication()).getExerciseService();
            setExercises(service.filter(TrainingPhase.getById(trainingPhase)));
        } catch (IOException ioe) {
            LOGGER.error("Unable to read exercises from JSON", ioe);
            // TODO inform user
        }
    }

    private void setExercises(final List<Exercise> exercises) {
        final ExerciseBrowserActivity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (exercises != null) {
                    LOGGER.info("Initialize with " + exercises.size() + " exercises");
                    activity.unfilteredExercises = exercises;
                    activity.exercises = applyFilter(activity.unfilteredExercises);
                    exerciseBrowserFragment.notifyDataChanged();
                }
            }
        });
    }

    private List<Exercise> applyFilter(List<Exercise> exercises) {
        List<Exercise> filteredExercises = Lists.newArrayList();
        if (this.exerciseFilter != null) {
            for (Exercise exercise : exercises) {

                boolean ageClassFilterMatches = false;
                List<AgeClass> ageClasses = this.exerciseFilter.getAgeClasses();
                if (ageClasses.isEmpty() || !Collections.disjoint(ageClasses, exercise.getAgeClasses())) {
                    ageClassFilterMatches = true;
                }

                boolean focusFilterMatches = false;
                List<TrainingFocus> focuses = this.exerciseFilter.getFocuses();
                if (focuses.isEmpty() || focuses.contains(exercise.getTrainingFocus())) {
                    focusFilterMatches = true;
                }

                if (ageClassFilterMatches && focusFilterMatches) {
                    filteredExercises.add(exercise);
                }
            }
        } else {
            filteredExercises = exercises;
        }
        return filteredExercises;
    }


    @Override
    public void onSaveFilter(ExerciseFilter exerciseFilter) {
        this.exerciseFilter = exerciseFilter;
        FoomlaPreferences.setExerciseFilter(this, exerciseFilter);
        this.exercises = applyFilter(this.unfilteredExercises);
        exerciseBrowserFragment.notifyDataChanged();
    }
}
