package org.foomla.androidapp.activities.edittraining;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.common.base.Strings;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.exercisedetail.ExerciseDetailIntent;
import org.foomla.androidapp.activities.trainingdetail.TrainingDetailActivity;
import org.foomla.androidapp.activities.trainingdetail.TrainingDetailFragment;
import org.foomla.androidapp.async.RepositoryLoadTask;
import org.foomla.androidapp.async.RepositoryLoadTrainingTask;
import org.foomla.androidapp.async.RepositorySaveTrainingTask;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.Training;
import org.foomla.androidapp.persistence.Repository;
import org.foomla.androidapp.persistence.TrainingProxyRepository;
import org.foomla.androidapp.service.TrainingService;
import org.foomla.androidapp.widgets.TrainingTitleDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EditTrainingActivity extends TrainingDetailActivity implements TrainingDetailFragment.ActionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditTrainingActivity.class);

    @Override
    public void onCreate(final Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        initializeView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edittraining;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initialize();
    }

    @Override
    public void onDisplayExerciseDetails(final int trainingPhase) {
        Exercise exercise = getTraining().getExercises().get(trainingPhase);
        Intent i = new ExerciseDetailIntent(this, null, exercise);
        startActivity(i);
    }

    @Override
    public void onSaveTraining() {
        TrainingTitleDialog.SaveListener saveListener = new TrainingTitleDialog.SaveListener() {
            @Override
            public void save(final Training training, final String title, final String comment) {
                if (Strings.isNullOrEmpty(title)) {
                    return;
                }

                training.setTitle(title);
                training.setComment(comment);

                Repository<Training> repository = TrainingProxyRepository.getInstance(getFoomlaApplication(),
                        EditTrainingActivity.this);
                RepositoryLoadTask.LoadHandler<Training> handler = new RepositoryLoadTask.LoadHandler<Training>() {
                    @Override
                    public void handle(final Training training) {
                        if (training != null) {
                            Toast.makeText(EditTrainingActivity.this, R.string.training_saved, Toast.LENGTH_LONG)
                                    .show();
                            finish();
                        } else {
                            Toast.makeText(EditTrainingActivity.this, R.string.training_not_saved, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                };

                new RepositorySaveTrainingTask(EditTrainingActivity.this, handler, repository).execute(getTraining());
            }
        };

        new TrainingTitleDialog(this, getTraining()).build(saveListener);
    }

    @Override
    protected void initializeView() {
        trainingDetailFragment = buildTrainingDetailFragment();
        getFragmentManager().beginTransaction().replace(R.id.edit_training, trainingDetailFragment).commit();
    }

    private Integer getTrainingIdFromIntent() {
        Intent i = getIntent();
        return new EditTrainingIntent(i).getTrainingId();
    }

    private void initialize() {
        Integer trainingId = getTrainingIdFromIntent();

        if (trainingId == null) {
            initializeWithRandomTraining();
        } else {
            initializeWithTrainingId(trainingId);
        }
    }

    private void initialize(final Training training) {
        LOGGER.info("Initialize with training: " + training);
        setTraining(training);
        trainingDetailFragment.trainingChanged();
    }

    private void initializeWithRandomTraining() {
        LOGGER.info("Initialize with random training");

        try {
            final TrainingService service = ((FoomlaApplication) getApplication()).getTrainingService();
            training = service.random();
            training.setTitle(null);
            initialize(service.random());
        } catch (IOException ioe) {
            LOGGER.error("Unable to initialize random training", ioe);
            // TODO inform user
        }
    }

    private void initializeWithTrainingId(final int trainingId) {
        LOGGER.info("Initialize with training id: " + trainingId);

        RepositoryLoadTask.LoadHandler<Training> handler = new RepositoryLoadTask.LoadHandler<Training>() {
            @Override
            public void handle(final Training training) {
                initialize(training);
            }
        };

        Repository<Training> trainingProxyRepository = TrainingProxyRepository.getInstance(getFoomlaApplication(),
                this);

        new RepositoryLoadTrainingTask(this, handler, trainingProxyRepository).execute(trainingId);
    }

    private void setTraining(final Training training) {
        this.training = training;
    }
}
