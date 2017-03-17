package org.foomla.androidapp.activities.mytrainings;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.GoProDialogFragment;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;
import org.foomla.androidapp.activities.edittraining.EditTrainingActivity;
import org.foomla.androidapp.activities.trainingdetail.TrainingDetailIntent;
import org.foomla.androidapp.async.RepositoryLoadTask;
import org.foomla.androidapp.async.RepositoryLoadTrainingsTask;
import org.foomla.androidapp.domain.Training;
import org.foomla.androidapp.exception.FoomlaException;
import org.foomla.androidapp.persistence.Repository;
import org.foomla.androidapp.persistence.TrainingProxyRepository;
import org.foomla.androidapp.utils.ProgressVisualizationUtil;
import org.foomla.androidapp.utils.UiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MyTrainingsActivity extends BaseActivityWithNavDrawer implements MyTrainingsFragment.FragmentCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTrainingsActivity.class);

    private List<Training> myTrainings;
    private MyTrainingsFragment myTrainingsFragment;

    @Override
    public List<Training> getMyTrainings() {
        return myTrainings;
    }

    @Override
    public void onCreate(final Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        myTrainings = new ArrayList<>();
        myTrainingsFragment = buildMyTrainingsFragment();
        getFragmentManager().beginTransaction().replace(R.id.my_trainings, myTrainingsFragment).commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mytrainings;
    }

    @Override
    public void onDeleteTraining(final Training training) {
        final Repository<Training> repository = TrainingProxyRepository.getInstance(getFoomlaApplication(), this);

        new AsyncTask<Training, Void, Void>() {
            @Override
            protected Void doInBackground(final Training... params) {
                if (params != null && params.length > 0 && params[0] != null) {
                    myTrainings.remove(params[0]);

                    try {
                        repository.delete(params[0].getId());
                    } catch (FoomlaException e) {
                        LOGGER.error("Failed to delete training", e);
                        Toast.makeText(MyTrainingsActivity.this, R.string.mytrainings_unable_to_delete,
                                Toast.LENGTH_LONG).show();
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(final Void result) {
                myTrainingsFragment.notifyDataChanged();
            }
        }.execute(training);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!myTrainings.isEmpty()) {

            // quick workaround to reload all trainings because the user might
            // have changed an existing training
            // and returns to this activity. in that case, we have to reload the
            // images of the exercises.
            // a better way would be to update only the modified training. for
            // that, we could use onActivityResult
            // to retrieve the modified training
            initialize();
        }
    }

    @Override
    public void onTrainingSelected(final Training training) {
        Intent i = new TrainingDetailIntent(this, training.getId());
        startActivity(i);
    }

    public void setMyTrainings(final List<Training> myTrainings) {
        this.myTrainings = myTrainings;
        myTrainingsFragment.notifyDataChanged();

        if (myTrainings == null || myTrainings.isEmpty()) {
            displayEmptyListInfo();
        }
    }

    @Override
    public void onAddTraining() {
        if(getFoomlaApplication().isProVersion() || this.myTrainings.size() < FoomlaApplication.MAX_TRAININGS_ON_FREE) {
            startActivity(new Intent(this, EditTrainingActivity.class));
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            GoProDialogFragment filterFragment = new GoProDialogFragment();
            filterFragment.show(fragmentManager, "goPro");
        }
    }

    private MyTrainingsFragment buildMyTrainingsFragment() {
        return new MyTrainingsFragment();
    }

    private void initialize() {
        RepositoryLoadTask.LoadHandler<List<Training>> handler = new RepositoryLoadTask.LoadHandler<List<Training>>() {
            @Override
            public void handle(final List<Training> trainings) {
                setMyTrainings(trainings);
            }
        };

        Repository<Training> repository = TrainingProxyRepository.getInstance(getFoomlaApplication(), this);
        new RepositoryLoadTrainingsTask(this, handler, repository) {
            @Override
            protected void hideProgressAnimation() {
                ProgressVisualizationUtil.hideProgressbar(MyTrainingsActivity.this);
            }

            @Override
            protected void showProgressAnimation(final Context ctx) {
                ProgressVisualizationUtil.showProgressbar(MyTrainingsActivity.this, R.string.activity_my_trainings);
            }
        }.execute(new Object());
    }

    public void displayEmptyListInfo() {
        View parent = findViewById(android.R.id.content);
        TextView infoText = UiUtils.getTextView(parent, R.id.empty_list_info);
        if (infoText != null) {
            ViewGroup.LayoutParams layoutParams = infoText.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            infoText.setVisibility(View.VISIBLE);
        }
    }
}
