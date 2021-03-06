package org.foomla.androidapp.activities.edittraining;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.exercisedetail.ExerciseDetailIntent;
import org.foomla.androidapp.activities.trainingdetail.TrainingDetailActivity;
import org.foomla.androidapp.activities.trainingdetail.TrainingDetailFragment;
import org.foomla.androidapp.async.DownloadRandomTrainingTask;
import org.foomla.androidapp.async.DownloadTask;
import org.foomla.androidapp.async.RepositoryLoadTask;
import org.foomla.androidapp.async.RepositoryLoadTrainingTask;
import org.foomla.androidapp.async.RepositorySaveTrainingTask;
import org.foomla.androidapp.persistence.Repository;
import org.foomla.androidapp.persistence.TrainingProxyRepository;
import org.foomla.androidapp.widgets.TrainingTitleDialog;

import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.Training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import android.content.Intent;

import android.os.Bundle;

import android.widget.Toast;

public class EditTrainingActivity extends TrainingDetailActivity implements TrainingDetailFragment.ActionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditTrainingActivity.class);

    @Override
    public void onCreate(final Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        createNavDrawer();
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
        setContentView(R.layout.activity_edittraining);
        trainingDetailFragment = buildTrainingDetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.edit_training, trainingDetailFragment).commit();
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

        final FoomlaApplication app = (FoomlaApplication) getApplication();
        DownloadTask.DownloadHandler<Training> handler = new DownloadTask.DownloadHandler<Training>() {
            @Override
            public void handle(final Training training) {
                training.setTitle(null);
                initialize(training);
            }
        };
        new DownloadRandomTrainingTask(this, handler).execute(app.getFoomlaClient());
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
